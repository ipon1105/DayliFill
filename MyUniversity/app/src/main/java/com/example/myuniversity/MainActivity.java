package com.example.myuniversity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.io.Resources;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //id кнопки, на которое приходилось нажатие
    int index = R.id.btnFullTime;

    int a = 0,b = 0,c = 0;
    private Button btn;
    private Button btnFullTime;
    private Button btnPartTime;
    private Button btnSession;

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

        btnFullTime = (Button) findViewById(R.id.btnFullTime);
        btnPartTime = (Button) findViewById(R.id.btnPartTime);
        btnSession = (Button) findViewById(R.id.btnSession);
        Spinner courseSpinner = (Spinner) findViewById(R.id.courceSpinner);
        Spinner universitySpinner = (Spinner) findViewById(R.id.universitySpinner);
        Spinner groupSpinner = (Spinner) findViewById(R.id.groupSpinner);

        onClick(findViewById(index));

        ArrayAdapter<CharSequence> courseAdapter = ArrayAdapter.createFromResource(this,R.array.courseList, android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> universityAdapter = ArrayAdapter.createFromResource(this,R.array.universityList, android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> groupAdapter = ArrayAdapter.createFromResource(this,R.array.groupList, android.R.layout.simple_spinner_dropdown_item);

        courseSpinner.setAdapter(courseAdapter);
        universitySpinner.setAdapter(universityAdapter);
        groupSpinner.setAdapter(groupAdapter);

        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                a = (i != 0 ? 1 : 0);
                checkBtn();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        universitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                b = (i != 0 ? 1 : 0);
                checkBtn();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                c = (i != 0 ? 1 : 0);
                checkBtn();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ( btn = (Button) findViewById(R.id.btnNext)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        AsyncTask<String, Void, ArrayList<String>> a = new Downloader().execute("https://www.sevsu.ru/univers/shedule", "1");
    }

    public void onClick(View view){
        index = view.getId();
        btnFullTime.setTextColor(getResources().getColor((view.getId() == R.id.btnFullTime ? R.color.whiteOn : R.color.whiteOff)));
        btnPartTime.setTextColor(getResources().getColor((view.getId() == R.id.btnPartTime ? R.color.whiteOn : R.color.whiteOff)));
        btnSession.setTextColor(getResources().getColor((view.getId() == R.id.btnSession ? R.color.whiteOn : R.color.whiteOff)));
    }

    void checkBtn(){
        btn.setEnabled(a+b+c == 3 ? true : false);
    }
}

class Downloader extends AsyncTask<String, Void, ArrayList<String>> {
    private final String instituteBlocks = ".su-column-content";
    private final String instituteNames = ".su-spoiler-title";

    private ArrayList<String> instituteFullTime;
    private ArrayList<String> institutePartTime;
    private ArrayList<String> instituteSession;

    private ArrayList<String> instituteList;

    public Downloader(){
        if(instituteList != null)
            return;

        instituteList     = new ArrayList<>();
        instituteFullTime = new ArrayList<>();
        institutePartTime = new ArrayList<>();
        instituteSession  = new ArrayList<>();
    }

    @Override//2 агрументы, где 1 - это адресс, а 2 - это имя класса
    protected ArrayList<String> doInBackground(String... strings) {
        Document doc;
        Elements contents;

        try {
            doc = Jsoup.connect(strings[0]).get();
            contents = doc.select(instituteBlocks);

            switch (strings[1]){
                case "1": downloadFullTime(contents.get(0));  break;
                case "2": downloadPartTime(contents.get(1));  break;
                case "3": downloadSession (contents.get(2));  break;
            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("debug", "exception", e);
        }

        switch (strings[1]){
            case "1": return instituteFullTime;
            case "2": return institutePartTime;
            case "3": return instituteSession;
        }

        return null;
    }

    //Скачать из блока ДФО
    void downloadFullTime(Element block){
        if(block != null)
            return;

        Elements listName = block.select(instituteNames);

        for (Element el : listName)
            instituteFullTime.add(el.text());
    }

    //Скачать из блока ЗФО
    void downloadPartTime(Element block){
        if(block != null)
            return;

        Elements listName = block.select(instituteNames);

        for (Element el : listName)
            institutePartTime.add(el.text());
    }

    //Скачать из блока Сессия
    void downloadSession(Element block){
        if(block != null)
            return;

        Elements listName = block.select(instituteNames);

        for (Element el : listName)
            instituteSession.add(el.text());
    }
}