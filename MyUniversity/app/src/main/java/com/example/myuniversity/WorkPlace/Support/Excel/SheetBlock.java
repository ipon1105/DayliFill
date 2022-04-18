package com.example.myuniversity.WorkPlace.Support.Excel;

import com.example.myuniversity.WorkPlace.Support.RecView.Day;

import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;

public class SheetBlock {
    private ArrayList<Group> groupList;
    private Sheet sheet;

    public SheetBlock(Sheet sheet, ArrayList<Group> groupList) {
        this.groupList = groupList;
        this.sheet = sheet;
    }

    //Получить имя вкладки
    public String getSheetName() {
        return sheet.getSheetName();
    }

    public ArrayList<Day> getWeek(int groupIndex){
        ArrayList<Day> dayArrayList = new ArrayList<>();

        dayArrayList.add(groupList.get(groupIndex).getDay(sheet, 0, true));

        return dayArrayList;
    }

    @Override
    public String toString() {
        return "SheetBlock{" + "\n" +
                "groupList=" + groupList +
                ", sheet=" + sheet + "\n" +
                '}';
    }

    public String toString(int i) {
        return String.valueOf(i) + ": " + toString();
    }
}