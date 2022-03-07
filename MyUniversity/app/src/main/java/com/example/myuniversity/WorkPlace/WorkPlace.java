package com.example.myuniversity.WorkPlace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.example.myuniversity.R;
import com.example.myuniversity.WorkPlace.Support.Load.FileLoadingListener;
import com.example.myuniversity.WorkPlace.Support.Load.FileLoadingTask;
import com.example.myuniversity.databinding.ActivityWorkPlaceBinding;
import com.google.android.material.tabs.TabLayout;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class WorkPlace extends AppCompatActivity {
    private NavController nav;
    private ActivityWorkPlaceBinding binding;
    private String url;

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
        Log.d("debug", "https://www.sevsu.ru" + url);
        WorkPlace.verifyStoragePermissions(this);

        File f = new File(Environment.getExternalStorageDirectory() + "/Download/schedule.xls");
        new LoadFile("https://www.sevsu.ru" + url, f).start();
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWorkPlaceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle arguments = getIntent().getExtras();
        url = (String) arguments.get("url");

        fullscreen();
        init();
    }

    private class LoadFile extends Thread {
        private final String src;
        private final File dest;

        LoadFile(String src, File dest) {
            this.src = src;
            this.dest = dest;
        }

        @Override
        public void run() {
            Log.d("debug", "START");
            try {
                FileUtils.copyURLToFile(new URL(src), dest);
                Log.d("debug", "GOOD");
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("debug", "FAIL: ", e);
            }
        }
    }
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

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
}