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
    private final String firstStartNAME = "firstStart";
    private final String filePathNAME = "filePath";
    private final String contentListNAME = "contentList";
    private final String urlListNAME = "urlList";
    private final String fileListNAME = "fileList";

    private String filePath;
    private ArrayList<String> contentList;
    private ArrayList<String> fileList;

    private SharedPreferences preferences;

    public Info(Context context){
        preferences = context.getSharedPreferences("info", Context.MODE_PRIVATE);

        contentList = new ArrayList<>();
        fileList = new ArrayList<>();
    }

    //Первый запуск
    public Boolean isFirstStart() {
        return preferences.getBoolean(firstStartNAME, true);
    }

    //Установить новое значение первого запуска
    public void setFirstStartNAME(Boolean set){
        preferences.edit().putBoolean(firstStartNAME, set).apply();
    }

    public void load(){
        filePath = preferences.getString(filePathNAME, null);

        Set<String> contentListSet = preferences.getStringSet(contentListNAME, null);
        if (contentListSet != null)
            for (String s : contentListSet)
                contentList.add(s);


        Set<String> fileListSet = preferences.getStringSet(fileListNAME, null);
        if (fileListSet != null)
            for (String s : fileListSet)
                fileList.add(s);
    }

    public void save(){
        SharedPreferences.Editor ed = preferences.edit();

        Set<String> contentListSet = new HashSet<>();
        if (contentList != null)
            for(String s : contentList)
                contentListSet.add(s);


        Set<String> fileListSet = new HashSet<>();
        if (fileList != null)
            for(String s : fileList)
                fileListSet.add(s);

        ed.putString(filePathNAME, filePath);
        ed.putStringSet(contentListNAME, contentListSet);
        ed.putStringSet(fileListNAME, fileListSet);
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
        ArrayList<String> urlList = new ArrayList<>();

        Set<String> urlListSet = preferences.getStringSet(urlListNAME, null);
        if (urlListSet != null)
            for (String s : urlListSet)
                urlList.add(s);
        return urlList;
    }

    public void setUrlList(ArrayList<String> urlList) {
        Set<String> urlListSet = new HashSet<>();
        if (urlList != null)
            for(String s : urlList)
                urlListSet.add(s);
        preferences.edit().putStringSet(urlListNAME, urlListSet).apply();
    }

}
