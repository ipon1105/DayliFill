package com.example.myuniversity.WorkPlace.Support;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

@RequiresApi(api = Build.VERSION_CODES.N)
public class Info implements Serializable {
    private final String courceMatchNAME[] = {
        "c_1", "c_2", "c_3", "c_4", "c_5",
        "c_6", "c_7", "c_8", "c_9", "c_10"
    };
    private final String urlMatchNAME[] = {
        "u_1", "u_2", "u_3", "u_4", "u_5",
        "u_6", "u_7", "u_8", "u_9", "u_10"
    };

    private final String courceIndexNAME = "courceIndex";
    private final String groupIndexNAME = "groupIndex";

    private final String sheetName = "sheet";

    private final String homeWorkName = "HomeWork";

    private final String fileFilePathNAME = "fileFilePath";
    private final String firstStartNAME = "firstStart";
    private final String fileNameNAME = "filePath";
    private final String groupName = "group";
    private final String groupId = "groupId";

    private SharedPreferences preferences;

    public String getHomeWork(){
        return preferences.getString(homeWorkName, "");
    }

    public void setHomeWork(String str){
        preferences.edit().putString(homeWorkName, str).apply();
    }

    //Индекс выбранного курса
    public Integer getSheet(){
        return preferences.getInt(sheetName, 0);
    }

    //Установить индекс выбранного курса
    public void setSheet(Integer set){
        preferences.edit().putInt(sheetName, set).apply();
    }

    //Индекс выбранного курса
    public Boolean getGroupId(){
        return preferences.getBoolean(groupId, true);
    }

    //Установить индекс выбранного курса
    public void setGroupId(Boolean set){
        preferences.edit().putBoolean(groupId, set).apply();
    }

    public Info(Context context){
        preferences = context.getSharedPreferences("info", Context.MODE_PRIVATE);
    }

    //Первый запуск
    public Boolean isFirstStart() {
        return preferences.getBoolean(firstStartNAME, true);
    }

    //Установить новое значение первого запуска
    public void setFirstStart(Boolean set){
        preferences.edit().putBoolean(firstStartNAME, set).apply();
    }

    //Индекс выбранного курса
    public Integer getCourceIndex(){
        return preferences.getInt(courceIndexNAME, 0);
    }

    //Установить индекс выбранного курса
    public void setCourceIndex(Integer set){
        preferences.edit().putInt(courceIndexNAME, set).apply();
    }

    //Получить имя расписания
    public String getFileName(){
        return preferences.getString(fileNameNAME, null);
    }

    //Установить имя расписания
    public void setFileName(String set){
        preferences.edit().putString(fileNameNAME, set).apply();
    }

    //Получить путь к файла
    @RequiresApi(api = Build.VERSION_CODES.N)
    public File getFilePath(){
        if (getFileName() == null)
            return null;
        return new File(getPath() + getDir() + getFileName());
    }

    public File getFilePath(String fileName){
        if (getFileName() == null)
            setFileName(fileName);
        return new File(getPath() + getDir() + fileName);
    }

    public String getWebSiteStr(){
        return "https://www.sevsu.ru" + getUrlList().get(getCourceIndex());
    }
    ///////////////////////////////////////////////////////////////////////////////


    //Получить группу (0 - группа 1;
    public Integer getGroup(){
        return preferences.getInt(groupName, 0);
    }

    //Установить группу
    public void setGroup(Integer set){
        preferences.edit().putInt(groupName, set).apply();
    }

    //Получает список содержащий курсы
    public ArrayList<String> getCourceList() {
        ArrayList<String> courceList = new ArrayList<>();
        String tmp = null;

        for(int i = 0; i < 10; i++) {
            if ((tmp = preferences.getString(courceMatchNAME[i], null)) == null)
                continue;
            courceList.add(tmp);
        }
        return courceList;
    }

    //Сохраняет список содержащий курсы
    public void setCourceList(ArrayList<String> courceList) {
        for (int i = 0; i < courceList.size(); i++)
            preferences.edit().putString(courceMatchNAME[i], courceList.get(i)).apply();
    }

    //Получает список содержащий ссылки
    public ArrayList<String> getUrlList() {
        ArrayList<String> urlList = new ArrayList<>();
        String tmp = null;

        for(int i = 0; i < 10; i++) {
            if ((tmp = preferences.getString(urlMatchNAME[i], null)) == null)
                continue;
            urlList.add(tmp);
        }
        return urlList;
    }

    //Сохраняет список содержащий ссылки
    public void setUrlList(ArrayList<String> urlList) {
        for (int i = 0; i < urlList.size(); i++)
            preferences.edit().putString(urlMatchNAME[i], urlList.get(i)).apply();
    }

    //Получить список файлов в папке
    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<String> getDirectoryList(String str){
        File tmp = new File(getPath() + str + File.separator);
        if (!tmp.exists())
            tmp.mkdir();

        ArrayList<String> dirList = new ArrayList<>();
        ArrayList<File> files = new ArrayList<>();
        for (File f : new File(getPath() + str).listFiles())
            files.add(f);

        files.sort(new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                return Long.compare(f1.lastModified(), f2.lastModified());
            }
        });

        if (files != null)
            for (File f : files)
                dirList.add(f.getName());

        return dirList;
    }

    //Получить коренной путь
    public String getPath(){
        return Environment.getExternalStorageDirectory().toString() + "/Download/MyUniversity/";
    }

    //Получить папку, которая выбрана
    public String getDir(){
        String tmp = getCourceList().get(getCourceIndex()) + File.separator;
        tmp = tmp.replaceAll("\\s","");
        return tmp;
    }

    @Override
    public String toString() {
        return "Info{" + "\n" +
                "fileName = " + getFileName() + "\n" +
                "urlList = " + getUrlList() + "\n" +
                '}';
    }
}