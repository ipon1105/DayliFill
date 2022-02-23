package com.example.myuniversity.SignOn.Support;


import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Downloader extends AsyncTask<String, Void, ArrayList<String>> {
    private final String instituteBlocks = ".su-column-content";
    private final String instituteNames = ".su-spoiler-title";

    private ArrayList<String> instituteFullTime;
    private ArrayList<String> institutePartTime;
    private ArrayList<String> instituteSession;

    public interface OnFinish{
        public void ProcessIsFinish();
    }

    private OnFinish finish;

    public Downloader(){
        instituteFullTime = new ArrayList<>();
        institutePartTime = new ArrayList<>();
        instituteSession  = new ArrayList<>();

        finish = null;
    }

    @Override
    protected ArrayList<String> doInBackground(String... strings) {
        Document doc;
        Elements contents;

        try {
            doc = Jsoup.connect(strings[0]).get();
            contents = doc.select(instituteBlocks);

            downloadFullTime(contents.get(0));
            downloadPartTime(contents.get(1));
            downloadSession (contents.get(2));

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("debug", "exception", e);
        }

        if(finish != null)
            finish.ProcessIsFinish();
        return null;
    }

    //Скачать из блока ДФО
    void downloadFullTime(Element block){
        if(block == null)
            return;

        Elements listName = block.select(instituteNames);

        for (Element el : listName)
            instituteFullTime.add(el.text());
    }

    //Скачать из блока ЗФО
    void downloadPartTime(Element block){
        if(block == null)
            return;

        Elements listName = block.select(instituteNames);

        for (Element el : listName)
            institutePartTime.add(el.text());
    }

    //Скачать из блока Сессия
    void downloadSession(Element block){
        if(block == null)
            return;

        Elements listName = block.select(instituteNames);

        for (Element el : listName)
            instituteSession.add(el.text());
    }

    public ArrayList<String> getInstituteFullTime() {
        return instituteFullTime;
    }

    public ArrayList<String> getInstitutePartTime() {
        return institutePartTime;
    }

    public ArrayList<String> getInstituteSession() {
        return instituteSession;
    }

    public void setFinish(OnFinish finish){
        this.finish = finish;
    }

}
