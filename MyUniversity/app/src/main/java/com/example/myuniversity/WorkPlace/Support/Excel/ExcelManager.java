package com.example.myuniversity.WorkPlace.Support.Excel;

import android.util.Log;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class ExcelManager {
    private ArrayList<Block> blockList;

    public ExcelManager() {
        blockList = new ArrayList<Block>();
    }

    public void SearchBlocks(XSSFWorkbook book){
        XSSFSheet sheet = book.getSheetAt(0);
        Row row = sheet.getRow(9);
        Cell cell = null;
        String name;

        for(int i = 4; i < row.getPhysicalNumberOfCells(); i++){
            cell = row.getCell(i);

            name = cell.getStringCellValue();
            if(!name.equals("") && !name.equals("Время"))
                blockList.add(new Block(name, i, i + 3));

        }

        for(int i = 0; i < blockList.size(); i++)
            Log.d("debug", "block " + String.valueOf(i) + ": " + blockList.get(i).toString());
    }

    //Чтение данных их файла
    public void ReadXLSX(File path) {
        Log.d("debug","Start read Excel");

        try {
            XSSFWorkbook myWorkBook = new XSSFWorkbook(path);

            SearchBlocks(myWorkBook);

            //Row row = null;
            //Cell cell = null;
            //for (int i = 9; i < 58; i++){
            //    row = sheet.getRow(i);
            //}
            //Log.d("debug", "row Count = " + String.valueOf(row.getPhysicalNumberOfCells()));

            //Log.d("debug", "cell = " + cell.getStringCellValue());

            /*
            Log.d("debug", "Info:");
            Log.d("debug", "\tFile have " + String.valueOf(myWorkBook.getNumberOfSheets()) + " sheets.");
            for(int i = 0; i < myWorkBook.getNumberOfSheets(); i++)
            {
                sheet = myWorkBook.getSheetAt(i);
                Log.d("debug", "\t" + String.valueOf(i) + " sheet have name = " + sheet.getSheetName() + ".");
            }

            XSSFSheet mySheet = myWorkBook.getSheetAt(0);
            Iterator<Row> rowIterator = mySheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_STRING:
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            break;
                        case Cell.CELL_TYPE_BOOLEAN:
                            break;
                        default:
                    }
                    //printlnToUser(cell);
                }
            }
            */
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            Log.d("debug", "Error File Format: ", e);
        }

        Log.d("debug","Stop read Excel");
    }
}
