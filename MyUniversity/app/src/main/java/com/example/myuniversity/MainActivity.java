package com.example.myuniversity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.example.myuniversity.SignOn.Support.Downloader;
import com.example.myuniversity.SignOn.Support.ItemClickListener;
import com.example.myuniversity.SignOn.Support.ListItemsAdapter;

public class MainActivity extends AppCompatActivity {
    //Для анимации переходов между nav_graph https://habr.com/ru/company/funcorp/blog/521340/
    public static Downloader downloader;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        if(Build.VERSION.SDK_INT >= 19){
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                  | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }
        downloader = new Downloader();
        downloader.execute("https://www.sevsu.ru/univers/shedule");

    }





}
