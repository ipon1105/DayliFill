package com.example.myuniversity.WorkPlace.Support;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Info implements Serializable {
    private final String filePathNAME = "filePath";
    private final String contentListNAME = "contentList";
    private final String urlListNAME = "contentList";

    private String filePath;
    private ArrayList<String> contentList;
    private ArrayList<String> urlList;

    private SharedPreferences preferences;

    public Info(Context context){
        preferences = context.getSharedPreferences("info", Context.MODE_PRIVATE);

        contentList = new ArrayList<>();
        urlList = new ArrayList<>();

        load();
    }

    public void load(){
        filePath = preferences.getString(filePathNAME, null);

        Set<String> contentListSet = preferences.getStringSet(contentListNAME, null);
        if (!contentListSet.isEmpty())
            for (String s : contentListSet)
                contentList.add(s);


        Set<String> urlListSet = preferences.getStringSet(urlListNAME, null);
        if (!urlListSet.isEmpty())
            for (String s : urlListSet)
                urlList.add(s);

    }

    public void save(){
        SharedPreferences.Editor ed = preferences.edit();

        Set<String> contentListSet = new HashSet<>();
        if (!contentList.isEmpty())
            for(String s : contentList)
                contentListSet.add(s);

        Set<String> urlListSet = new HashSet<>();
        if (!urlList.isEmpty())
            for(String s : urlList)
                urlListSet.add(s);

        ed.putString(filePathNAME, filePath);
        ed.putStringSet(contentListNAME, contentListSet);
        ed.putStringSet(urlListNAME, urlListSet);

        ed.apply();
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public ArrayList<String> getContentList() {
        return contentList;
    }

    public void setContentList(ArrayList<String> contentList) {
        this.contentList = contentList;
    }

    public ArrayList<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(ArrayList<String> urlList) {
        this.urlList = urlList;
    }
}
