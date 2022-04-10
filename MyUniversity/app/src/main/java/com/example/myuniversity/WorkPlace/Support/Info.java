package com.example.myuniversity.WorkPlace.Support;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Info implements Serializable {
    private final String firstStartNAME = "firstStart";
    private final String fileNameNAME = "filePath";
    private final String contentListNAME = "contentList";
    private final String urlListNAME = "urlList";

    private SharedPreferences preferences;

    public Info(Context context){
        preferences = context.getSharedPreferences("info", Context.MODE_PRIVATE);
    }

    //Получить имя расписания
    public String getFileName(){
        return preferences.getString(fileNameNAME, null);
    }

    //Установить имя расписания
    public void setFileName(String set){
        preferences.edit().putString(fileNameNAME, set).apply();
    }

    //Первый запуск
    public Boolean isFirstStart() {
        return preferences.getBoolean(firstStartNAME, true);
    }

    //Установить новое значение первого запуска
    public void setFirstStartNAME(Boolean set){
        preferences.edit().putBoolean(firstStartNAME, set).apply();
    }

    public ArrayList<String> getContentList() {
        ArrayList<String> contentList = new ArrayList<>();

        Set<String> contentListSet = preferences.getStringSet(contentListNAME, null);
        if (contentListSet != null)
            for (String s : contentListSet)
                contentList.add(s);
        return contentList;
    }

    public void setContentList(ArrayList<String> contentList) {
        Set<String> contentListSet = new HashSet<>();
        if (contentList != null)
            for(String s : contentList)
                contentListSet.add(s);
        preferences.edit().putStringSet(contentListNAME, contentListSet).apply();
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

    //Получить список файлов в папке
    public ArrayList<String> getDirectoryList(){
        ArrayList<String> dirList = new ArrayList<>();
        File[] files = new File(Environment.getExternalStorageDirectory().toString() + "/Download/MyUniversity").listFiles();
        if (files != null)
            for (File f : files)
                dirList.add(f.getName());
        return dirList;
    }

    @Override
    public String toString() {
        return "Info{" + "\n" +
                "fileName = " + getFileName() + "\n" +
                "contentList = " + getContentList() + "\n" +
                "urlList = " + getUrlList() + "\n" +
                '}';
    }
}