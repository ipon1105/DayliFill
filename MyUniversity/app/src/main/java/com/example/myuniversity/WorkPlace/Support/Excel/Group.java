package com.example.myuniversity.WorkPlace.Support.Excel;

public class Group {
    private String name;
    private int cellStart;

    public Group(String name, int cellStart) {
        this.name = name;
        this.cellStart = cellStart;
    }

    public int getCellStart() {
        return cellStart;
    }

    public void setCellStart(int cellStart) {
        this.cellStart = cellStart;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Group{" +
                ", name='" + name + '\'' +
                ", cellStart=" + cellStart +
                '}';
    }
}
