package com.example.myuniversity.WorkPlace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.myuniversity.R;
import com.example.myuniversity.WorkPlace.Support.Downloader;
import com.example.myuniversity.WorkPlace.Support.Excel.ExcelManager;
import com.example.myuniversity.WorkPlace.Support.Info;
import com.example.myuniversity.WorkPlace.Support.Load.FileLoadingListener;
import com.example.myuniversity.WorkPlace.Support.Load.FileLoadingTask;
import com.example.myuniversity.databinding.ActivityWorkPlaceBinding;
import com.example.myuniversity.databinding.WelcomBinding;
import com.google.android.material.tabs.TabLayout;

import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.formula.functions.Column;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Iterator;

public class WorkPlace extends AppCompatActivity {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private NavController nav;
    private Info info;
    private Downloader downloader;
    private ExcelManager manager;

    private ActivityWorkPlaceBinding binding;
    private WelcomBinding welcomBinding;

    private Context context;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fullscreen();
        general_init();

        if (info.isFirstStart()){
            setContentView(welcomBinding.getRoot());

            welcomBinding.btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (info.isFirstStart()) {
                        setManager();

                        if (!WorkPlace.hasConnection(context))
                            Toast.makeText(context, "Нет подключения к интернету", Toast.LENGTH_SHORT).show();

                        downloader.execute();
                    } else {
                        setContentView(binding.getRoot());
                        init();
                    }
                }
            });

            downloader.execute();

            return;
        }

        setContentView(binding.getRoot());
        init();
    }

    //Общая инициализация
    private void general_init(){
        context = this;
        welcomBinding = WelcomBinding.inflate(getLayoutInflater());
        binding = ActivityWorkPlaceBinding.inflate(getLayoutInflater());

        info = new Info(this);
        setManager();
    }

    //Участок инициализации
    public void setManager(){
        downloader = (Downloader) new Downloader(new FileLoadingListener() {
            @Override
            public void onBegin() {
                Log.d("debug", "Begin FileLoadingListener");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        welcomBinding.btnNext.setEnabled(false);
                        welcomBinding.btnNext.setVisibility(View.INVISIBLE);

                        welcomBinding.progressBar.setEnabled(true);
                        welcomBinding.progressBar.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onSuccess() {
                Log.d("debug", "Success FileLoadingListener");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        welcomBinding.btnNext.setEnabled(true);
                        welcomBinding.btnNext.setVisibility(View.VISIBLE);

                        welcomBinding.progressBar.setEnabled(false);
                        welcomBinding.progressBar.setVisibility(View.INVISIBLE);

                        welcomBinding.btnNext.setText(getResources().getString(R.string.btnNextPage2));
                    }
                });

                info.setFirstStartNAME(false);
                info.setUrlList(downloader.getUrlList());
                info.setContentList(downloader.getContentList());

            }

            @Override
            public void onFailure(Throwable cause) {
                Log.d("debug", "Begin FileLoadingListener");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        welcomBinding.btnNext.setEnabled(true);
                        welcomBinding.btnNext.setVisibility(View.VISIBLE);

                        welcomBinding.progressBar.setEnabled(false);
                        welcomBinding.progressBar.setVisibility(View.INVISIBLE);

                        welcomBinding.btnNext.setText(getResources().getString(R.string.btnAgainPage2));
                    }
                });

                info.setFirstStartNAME(true);
            }

            @Override
            public void onEnd() {
                Log.d("debug", "End FileLoadingListener");
            }
        });
    }

    //Инициализация
    private void init(){
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.box);

        if (navHostFragment != null)
            nav = navHostFragment.getNavController();

        binding.tabList.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            TabLayout.Tab lastTab;
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (lastTab == null)
                    lastTab = tab;

                switch (tab.getPosition()){
                    case 0:
                        if (lastTab.getPosition() == 1)
                            nav.navigate(R.id.action_homeWork_to_schedule);
                        else
                            nav.navigate(R.id.action_setting_to_schedule);
                    break;
                    case 1:
                        if (lastTab.getPosition() == 2)
                            nav.navigate(R.id.action_setting_to_homeWork);
                        else
                            nav.navigate(R.id.action_schedule_to_homeWork);
                    break;
                    case 2:
                        if (lastTab.getPosition() == 0)
                            nav.navigate(R.id.action_schedule_to_setting);
                        else
                            nav.navigate(R.id.action_homeWork_to_setting);
                    break;
                }
                lastTab = tab;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        WorkPlace.verifyStoragePermissions(this);

        Log.d("debug", "str = " + info.getDirectoryList());
        if (info.getFileName() == null) {
            //Первый запуск
        } else {
            //Второй запуск
        }
        //До сюда всё нормально
        /*
        Log.d("debug", "Before load");
        if (info.getFilePath() == null)
            downloader = (Downloader) new Downloader(new FileLoadingListener() {
                @Override
                public void onBegin() {
                    Log.d("debug", "Begin Download");
                }

                @Override
                public void onSuccess() {
                    Log.d("debug", "Success Download: ");

                    info.setContentList(downloader.getContentList());
                    info.setUrlList(downloader.getUrlList());
                    info.setFilePath(Environment.getExternalStorageDirectory() + "/Download/MyUniversity/" + new File(downloader.getUrlList().get(2)).getName());

                    info.save();

                    new FileLoadingTask(
                            "https://www.sevsu.ru" + downloader.getUrlList().get(2),
                            new File(info.getFilePath()),
                            new FileLoadingListener() {
                                @Override
                                public void onBegin() {
                                    Log.d("debug", "Begin FileLoadingTask");
                                }

                                @Override
                                public void onSuccess() {
                                    Log.d("debug", "Success FileLoadingTask");
                                    try {
                                        manager.startLoad();
                                    } catch (FileNotFoundException e) {
                                        Log.d("debug", "File Not Found");
                                    }
                                }

                                @Override
                                public void onFailure(Throwable cause) {
                                    //Ошибка в загрузки данных с эксель файла
                                    Log.d("debug", "Faild FileLoadingTask: ", cause);
                                }

                                @Override
                                public void onEnd() {
                                    Log.d("debug", "End FileLoadingTask");
                                }
                            }
                    ).execute();
                }

                @Override
                public void onFailure(Throwable cause) {
                    //Нет соединения с интернетом при первом заходи
                    Log.d("debug", "Faild Download: ", cause);
                }

                @Override
                public void onEnd() {
                    Log.d("debug", "End Download");
                }
            }).execute();
        else {
            try {
                manager.startLoad();
            } catch (FileNotFoundException e) {
                new FileLoadingTask(
                        "https://www.sevsu.ru" + downloader.getUrlList().get(2),
                        new File(info.getFilePath()),
                        new FileLoadingListener() {
                            @Override
                            public void onBegin() {
                                Log.d("debug", "Begin FileLoadingTask");
                            }

                            @Override
                            public void onSuccess() {
                                Log.d("debug", "Success FileLoadingTask");
                                try {
                                    manager.startLoad();
                                } catch (FileNotFoundException e) {
                                    Log.d("debug", "File Not Found");
                                }
                            }

                            @Override
                            public void onFailure(Throwable cause) {
                                //Ошибка в загрузки данных с эксель файла
                                Log.d("debug", "Faild FileLoadingTask: ", cause);
                            }

                            @Override
                            public void onEnd() {
                                Log.d("debug", "End FileLoadingTask");
                            }
                        }
                ).execute();
            }
        }
        */


        /*
        new FileLoadingTask(
            "https://www.sevsu.ru" + url,
                f,
            new FileLoadingListener() {
                @Override
                public void onBegin() {
                    Log.d("debug","Begin");
                }

                @Override
                public void onSuccess() {
                    Log.d("debug","Success");

                    manager = new ExcelManager(f, new FileLoadingListener() {
                        @Override
                        public void onBegin() {

                        }

                        @Override
                        public void onSuccess() {
                            Bundle bundle = new Bundle();

                            bundle.putSerializable("manager", (Serializable) manager);

                            nav.navigate(R.id.schedule,bundle);
                        }

                        @Override
                        public void onFailure(Throwable cause) {

                        }

                        @Override
                        public void onEnd() {

                        }
                    });

                    manager.startLoad();
                }

                @Override
                public void onFailure(Throwable cause) {
                    Log.d("debug","Fail: ", cause);
                }

                @Override
                public void onEnd() {
                    Log.d("debug","End");
                }
            }
        ).execute();
        */
    }

    //Проверить доступ к файловому мессенджеру
    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    //Настройка окна
    private void fullscreen(){
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        if(Build.VERSION.SDK_INT >= 19){
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }
    }

    //Проверка соендинения
    public static boolean hasConnection(final Context context)
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected())
            return true;

        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected())
            return true;

        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected())
            return true;

        return false;
    }
}