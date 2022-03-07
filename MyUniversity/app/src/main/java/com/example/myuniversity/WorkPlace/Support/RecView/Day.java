package com.example.myuniversity.WorkPlace.Support.RecView;

import java.util.ArrayList;

public class Day {
    private String day;
    private ArrayList<Element> elementList;

    public Day(String day, ArrayList<Element> elementList) {
        this.day = day;
        this.elementList = elementList;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public ArrayList<Element> getElementList() {
        return elementList;
    }

    public void setElementList(ArrayList<Element> elementList) {
        this.elementList = elementList;
    }
}
