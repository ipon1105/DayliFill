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
    private final String contentIndexNAME = "contentIndex";
    private final String groupIndexNAME = "groupIndex";

    private final String fileFilePathNAME = "fileFilePath";
    private final String contentListNAME = "contentList";
    private final String firstStartNAME = "firstStart";
    private final String fileNameNAME = "filePath";
    private final String urlListNAME = "urlList";
    private final String groupName = "group";

    private SharedPreferences preferences;

    public Info(Context context){
        preferences = context.getSharedPreferences("info", Context.MODE_PRIVATE);
    }

    //Индекс выбранной группы
    public Integer getContentIndex(){
        return preferences.getInt(contentIndexNAME, -1);
    }

    //Установить индекс выбранной группы
    public void setContentIndex(Integer set){
        preferences.edit().putInt(contentIndexNAME, set).apply();
    }

    //Индекс выбранной группы
    public Integer getGroupIndex(){
        return preferences.getInt(groupIndexNAME, -1);
    }

    //Установить индекс выбранной группы
    public void setGroupIndex(Integer set){
        preferences.edit().putInt(groupIndexNAME, set).apply();
    }

    //Получить путь к файла
    public File getFilePath(){
        if (getFileName() == null)
            return null;
        return new File(Environment.getExternalStorageDirectory() + "/Download/MyUniversity/" + getFileName());
    }

    //Получить группу (0 - группа 1;
    public Integer getGroup(){
        return preferences.getInt(groupName, 0);
    }

    //Установить группу
    public void setGroup(Integer set){
        preferences.edit().putInt(groupName, set).apply();
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

    //Получает список содержащий курсы
    public ArrayList<String> getContentList() {
        ArrayList<String> contentList = new ArrayList<>();

        Set<String> contentListSet = preferences.getStringSet(contentListNAME, null);
        if (contentListSet != null)
            for (String s : contentListSet)
                contentList.add(s);
        return contentList;
    }

    //Сохраняет список содержащий курсы
    public void setContentList(ArrayList<String> contentList) {
        Set<String> contentListSet = new HashSet<>();
        if (contentList != null)
            for(String s : contentList)
                contentListSet.add(s);
        preferences.edit().putStringSet(contentListNAME, contentListSet).apply();
    }

    //Получает список содержащий ссылки
    public ArrayList<String> getUrlList() {
        ArrayList<String> urlList = new ArrayList<>();

        Set<String> urlListSet = preferences.getStringSet(urlListNAME, null);
        if (urlListSet != null)
            for (String s : urlListSet)
                urlList.add(s);
        return urlList;
    }

    //Сохраняет список содержащий ссылки
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