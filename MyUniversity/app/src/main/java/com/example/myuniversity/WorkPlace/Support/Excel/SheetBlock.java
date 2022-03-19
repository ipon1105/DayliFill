package com.example.myuniversity.WorkPlace.Support.Excel;

import java.util.ArrayList;

public class SheetBlock {
    private ArrayList<Block> blockList;
    private String sheetName;

    public SheetBlock(String sheetName) {
        this.sheetName = sheetName;
    }

    public SheetBlock(String sheetName, ArrayList<Block> blockList) {
        this.sheetName = sheetName;
        this.blockList = blockList;
    }

    public ArrayList<Block> getBlockList() {
        return blockList;
    }

    public void setBlockList(ArrayList<Block> blockList) {
        this.blockList = blockList;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    @Override
    public String toString() {
        return "SheetBlock{" +
                "blockList=" + blockList +
                ", sheetName='" + sheetName + '\'' +
                '}';
    }

    public String toString(int i) {
        return "Sheet № " + String.valueOf(i) + "{" +
                "sheetName='" + sheetName + '\'' +
                ", blockList=" + blockList +
                '}';
    }
}