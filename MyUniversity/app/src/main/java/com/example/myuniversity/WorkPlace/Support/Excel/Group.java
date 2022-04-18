package com.example.myuniversity.WorkPlace.Support.Excel;

import com.example.myuniversity.WorkPlace.Support.RecView.Day;
import com.example.myuniversity.WorkPlace.Support.RecView.Element;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.ArrayList;

public class Group {
    private String name;
    private int rowStart;
    private int colStart;

    public Group(String name, int rowStart, int colStart) {
        this.name = name;
        this.rowStart = rowStart;
        this.colStart = colStart;
    }

    public int getColStart() {
        return colStart;
    }

    public void setColStart(int colStart) {
        this.colStart = colStart;
    }

    public int getRowStart() {
        return rowStart;
    }

    public void setRowStart(int rowStart) {
        this.rowStart = rowStart;
    }

    /////////////////////////////////////////////////////////////////

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
                ", cellStart=" + colStart +
                '}';
    }

    public String consist(Sheet sheet, int row, int col){
        CellRangeAddress region = null;
        Row r = sheet.getRow(row);
        Cell c = r.getCell(col);

        if (c.getCellType() == Cell.CELL_TYPE_STRING && c.getStringCellValue() != null && !c.getStringCellValue().equals(""))
            return c.getStringCellValue();

        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            region = sheet.getMergedRegion(i);
            //if (region.getFirstRow() <= row && row <= region.getLastRow() &&
            //    region.getFirstColumn() <= col && col <= region.getLastColumn()){
            if (region.isInRange(row, col)) {
                c = sheet.getRow(region.getFirstRow()).getCell(region.getFirstColumn());

                if (c == null)
                    return null;

                return (c.getCellType() == Cell.CELL_TYPE_STRING) ? c.getStringCellValue() : null;
            }
        }

        return null;
    }

    public Day getDay(Sheet sheet, int dayIndex, boolean group_1){
        Row row = null;
        Cell cell = null;

        String name = null;

        ArrayList<Element> monday = new ArrayList<>();

        for (int i = 1; i <= 8; i++){
            name = consist(sheet, (dayIndex * 8) + (rowStart + i), (group_1 ? colStart : colStart + 1));

            monday.add(new Element(i, name, null));
        }

        return (new Day("А", monday));
    }
}
