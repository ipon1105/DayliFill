package com.example.myuniversity.WorkPlace.Support;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.example.myuniversity.WorkPlace.Support.Load.FileLoadingListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Downloader extends AsyncTask<String, Void, Void> {
    private final String urlPage = "https://www.sevsu.ru/univers/shedule/";
    private final String instituteBlocks = ".su-column-content";
    private final String instituteContent = ".su-spoiler-content.su-clearfix";

    private FileLoadingListener fileLoadingListener;

    private ArrayList<String> contentList;
    private ArrayList<String> urlList;

    private Elements contents;

    public Downloader(FileLoadingListener fileLoadingListener){
        this.fileLoadingListener = fileLoadingListener;

        contentList = new ArrayList<>();
        urlList = new ArrayList<>();

        contents = null;
    }

    @Override
    protected Void doInBackground(String... strings) {
        fileLoadingListener.onBegin();
        Document doc;

        try {
            doc = Jsoup.connect(urlPage).get();
            contents = doc.select(instituteBlocks);

            downloadGroup();

        } catch (IOException e) {
            fileLoadingListener.onFailure(e);
        }

        fileLoadingListener.onEnd();
        return null;
    }

    public void downloadGroup(){
        if (!contentList.isEmpty())
            contentList.clear();

        if (!urlList.isEmpty())
            urlList.clear();

        if(contents == null) {
            fileLoadingListener.onFailure(new Exception("Не получилось загрузить данные с сайта."));
            return;
        }

        Element block = contents.get(0);
        Elements institute = block.select(instituteContent);

        if(institute == null) {
            fileLoadingListener.onFailure(new Exception("Не получилось загрузить данные с блока."));
            return;
        }

        String txt = null;
        for (Element e : institute.get(0).getElementsByTag("a")) {
            txt = e.text();
            if (txt.equals("") ||
                txt.equals(" ")||
                txt == null
            ) continue;

            contentList.add(txt);
            urlList.add(e.attr("href"));
        }

        fileLoadingListener.onSuccess();
    }

    public ArrayList<String> getContentList() {
        return contentList;
    }

    public ArrayList<String> getUrlList() {
        return urlList;
    }
}
