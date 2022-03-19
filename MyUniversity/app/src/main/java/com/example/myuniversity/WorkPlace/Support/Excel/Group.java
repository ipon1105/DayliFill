package com.example.myuniversity.WorkPlace.Support.Excel;

public class Group {
    private final int ROW_INDEX_START = 9;
    private final int ROW_INDEX_STOP = 57;

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
                "ROW_INDEX_START=" + ROW_INDEX_START +
                ", ROW_INDEX_STOP=" + ROW_INDEX_STOP +
                ", name='" + name + '\'' +
                ", cellStart=" + cellStart +
                '}';
    }
}
