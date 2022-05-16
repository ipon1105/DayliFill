package com.example.myuniversity.WorkPlace;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.myuniversity.R;
import com.example.myuniversity.WorkPlace.Support.Downloader;
import com.example.myuniversity.WorkPlace.Support.Excel.ExcelManager;
import com.example.myuniversity.WorkPlace.Support.Info;
import com.example.myuniversity.WorkPlace.Support.Load.FileLoadingListener;
import com.example.myuniversity.databinding.ActivityWorkPlaceBinding;
import com.example.myuniversity.databinding.WelcomBinding;
import com.google.android.material.tabs.TabLayout;

public class WorkPlace extends AppCompatActivity {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static Context workPlaceContext;
    public static ExcelManager manager;
    public static Info info;

    private NavController nav;
    private Downloader downloader;

    private ActivityWorkPlaceBinding binding;
    private WelcomBinding welcomBinding;

    @RequiresApi(api = Build.VERSION_CODES.N)
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
                    verifyStoragePermissions((WorkPlace) workPlaceContext);

                    if (info.isFirstStart())
                        loadData();
                    else {
                        setContentView(binding.getRoot());
                        init();
                    }
                }
            });

            loadData();

            return;
        }

        setContentView(binding.getRoot());
        init();
    }

    //Общая инициализация
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void general_init(){
        workPlaceContext = this;

        welcomBinding = WelcomBinding.inflate(getLayoutInflater());
        binding = ActivityWorkPlaceBinding.inflate(getLayoutInflater());

        info = new Info(workPlaceContext);

        initExcel();
    }

    //Данный участок отвечает за инициализацию переменной для загрузки данных с сайта
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void loadData(){
        Log.i("WorkPlace", "Begin setManager");

        if (!hasConnection(workPlaceContext)) {
            Toast.makeText(workPlaceContext, "Нет подключения к интернету", Toast.LENGTH_SHORT).show();
            Log.i("WorkPlace", "Internet connection not exist");

            return;
        }


        downloader = (Downloader) new Downloader(new FileLoadingListener() {
            @Override
            public void onBegin() {
                Log.i("WorkPlace", "Begin FileLoadingListener");

                verifyStoragePermissions((WorkPlace) workPlaceContext);

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
                Log.i("WorkPlace", "Success FileLoadingListener");

                if (!verifyStoragePermissions((WorkPlace) workPlaceContext)) {
                    Log.i("WorkPlace", "No permissions");

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

                    info.setFirstStart(true);
                    return;
                }

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

                info.setFirstStart(false);
                info.setUrlList(downloader.getUrlList());
                info.setContentList(downloader.getContentList());

                initExcel();
            }

            @Override
            public void onFailure(Throwable cause) {
                Log.e("WorkPlace", "Exception FileLoadingListener: ",  cause);

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

                info.setFirstStart(true);
            }

            @Override
            public void onEnd() {
                Log.i("WorkPlace", "End FileLoadingListener");
            }
        });

        Log.i("WorkPlace", "Downloader execute");
        downloader.execute();

        Log.i("WorkPlace", "End setManager");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void initExcel(){
        if (info.getFilePath() != null)
            manager = new ExcelManager(
                    info.getFilePath(),
                    new FileLoadingListener() {
                        @Override
                        public void onBegin() {
                            Log.i("ExcelManager", "Begin Excel Manager parser.");
                        }
                        @Override
                        public void onSuccess() {
                            Log.i("ExcelManager", "Success Excel Manager parser.");
                        }
                        @Override
                        public void onFailure(Throwable cause) {
                            Log.e("ExcelManager", "Failed Excel Manager parser: " + cause);
                        }
                        @Override
                        public void onEnd() {
                            Log.i("ExcelManager", "End Excel Manager parser.");
                        }
                    }
            );
    }

    //Инициализация
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void init(){
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.box);

        if (navHostFragment != null)
            nav = navHostFragment.getNavController();

        binding.tabList.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            int lastPos = -1;
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (lastPos == -1)
                    lastPos = 0;

                try {
                    switch (tab.getPosition()){
                        case 0:
                            if (lastPos == 1)
                                nav.navigate(R.id.action_homeWork_to_schedule);
                            else
                                nav.navigate(R.id.action_setting_to_schedule);
                            break;
                        case 1:
                            if (lastPos == 2)
                                nav.navigate(R.id.action_setting_to_homeWork);
                            else
                                nav.navigate(R.id.schedule_0_1);
                            break;
                        case 2:
                            if (lastPos == 0)
                                nav.navigate(R.id.schedule_0_2);
                            else
                                nav.navigate(R.id.action_homeWork_to_setting);
                            break;

                    }
                    lastPos = tab.getPosition();
                } catch (Exception e){
                    Log.e("WorkPlace", "Error: " + e);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        WorkPlace.verifyStoragePermissions((WorkPlace) workPlaceContext);

        generalUpdate();

    }

    //Попытаться обновить данные
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void generalUpdate(){
        if (!hasConnection(workPlaceContext)) {
            Toast.makeText(workPlaceContext, "Невозможно подключиться к интернету.", Toast.LENGTH_SHORT).show();
            return;
        }

        initExcel();
        loadData();
    }

    //Проверить доступ к файловому мессенджеру
    public static boolean verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            );
            return false;
        }
        return true;
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

        //Запрещаю вертерь экран
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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