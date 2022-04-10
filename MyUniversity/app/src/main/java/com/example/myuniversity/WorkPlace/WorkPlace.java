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

import com.example.myuniversity.R;
import com.example.myuniversity.WorkPlace.Support.Downloader;
import com.example.myuniversity.WorkPlace.Support.Excel.ExcelManager;
import com.example.myuniversity.WorkPlace.Support.Info;
import com.example.myuniversity.WorkPlace.Support.Load.FileLoadingListener;
import com.example.myuniversity.WorkPlace.Support.Load.FileLoadingTask;
import com.example.myuniversity.databinding.ActivityWorkPlaceBinding;
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
    private ActivityWorkPlaceBinding binding;
    private Info info;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWorkPlaceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fullscreen();
        init();
    }

    //Инициализация
    private void init(){
        info = new Info(this);
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

        Log.d("debug", "Before load");
        if (info.getFilePath() == null)
            new Downloader(new FileLoadingListener() {
                @Override
                public void onBegin() {
                    Log.d("debug", "Begin Download");
                }

                @Override
                public void onSuccess() {
                    Log.d("debug", "Success Download: ");
                }

                @Override
                public void onFailure(Throwable cause) {
                    Log.d("debug", "Faild Download: ", cause);
                }

                @Override
                public void onEnd() {
                    Log.d("debug", "End Download");
                }
            }).execute();
        //Log.d("debug", "https://www.sevsu.ru" + url);
        //File f = new File(Environment.getExternalStorageDirectory() + "/Download/MyUniversity/" + new File(url).getName());

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