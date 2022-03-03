package com.example.myuniversity.WorkPlace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.example.myuniversity.R;
import com.example.myuniversity.databinding.ActivityWorkPlaceBinding;
import com.google.android.material.tabs.TabLayout;

public class WorkPlace extends AppCompatActivity {
    private NavController nav;
    private ActivityWorkPlaceBinding binding;

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
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWorkPlaceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fullscreen();
        init();
    }

}