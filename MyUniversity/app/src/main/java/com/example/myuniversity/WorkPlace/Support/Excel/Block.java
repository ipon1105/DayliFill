package com.example.myuniversity.WorkPlace.Support.Excel;

public class Block {
    private final int ROW_INDEX_START = 9;
    private final int ROW_INDEX_STOP = 57;

    private String name;
    private int cellStart;
    private int cellStop;

    public Block(String name, int cellStart, int cellStop) {
        this.name = name;
        this.cellStart = cellStart;
        this.cellStop = cellStop;
    }

    public int getCellStart() {
        return cellStart;
    }

    public void setCellStart(int cellStart) {
        this.cellStart = cellStart;
    }

    public int getCellStop() {
        return cellStop;
    }

    public void setCellStop(int cellStop) {
        this.cellStop = cellStop;
    }

    @Override
    public String toString() {
        return "Block{" +
                "ROW_INDEX_START=" + ROW_INDEX_START +
                ", ROW_INDEX_STOP=" + ROW_INDEX_STOP +
                ", name='" + name + '\'' +
                ", cellStart=" + cellStart +
                ", cellStop=" + cellStop +
                '}';
    }
}
