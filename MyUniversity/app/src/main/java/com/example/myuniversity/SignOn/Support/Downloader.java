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
    private final String courseNames = ".su-spoiler-content su-clearfix";

    private ArrayList<String> instituteFullTime;
    private ArrayList<String> institutePartTime;
    private ArrayList<String> instituteSession;
    private ArrayList<String> instituteGroup;

    private Elements contents;

    public interface OnFinish{
        public void ProcessIsFinish();
    }

    private OnFinish finish;

    public Downloader(){
        instituteFullTime = new ArrayList<>();
        institutePartTime = new ArrayList<>();
        instituteSession  = new ArrayList<>();
        instituteGroup    = new ArrayList<>();

        finish = null;
    }

    @Override
    protected ArrayList<String> doInBackground(String... strings) {
        Document doc;

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

    public void downloadGroup(int blockIndex, int institutePos){
        Element block = contents.get(blockIndex);
        Elements institute = block.select(instituteNames);

        Elements group = null; int i = -1;
        for (Element e : institute) {
            if(++i == institutePos) {
                group = e.select(courseNames);
                break;
            }
        }

        if(group == null)
            return;

        for (Element e : group)
            instituteGroup.add(e.text());

        finish.ProcessIsFinish();
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

    public ArrayList<String> getGroupList() {
        return instituteGroup;
    }

    public void setFinish(OnFinish finish){
        this.finish = finish;
    }

}
