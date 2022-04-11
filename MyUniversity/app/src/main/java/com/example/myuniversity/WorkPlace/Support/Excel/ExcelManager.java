package com.example.myuniversity.WorkPlace.Support.Excel;

import android.util.Log;

import com.example.myuniversity.WorkPlace.Support.Load.FileLoadingListener;
import com.example.myuniversity.WorkPlace.Support.RecView.Day;
import com.example.myuniversity.WorkPlace.Support.RecView.Element;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Locale;

/*
* Дополнительные ошибки:
* SignOnPage2.java Строчка №81 Неоригинальный поток для UI
*/

/*
* Ошибки при работе алгоритма
* №1 -> Интститут Информацонных технологий и управления в технических системах;
* Проблема -> Sheet(ИС 4к,маг) не заполняется потому что высота выше строки
* понедельник не 1, а 2.
* №2 -> Юридический институт;
* Проблема -> ЮИ ОЗФО, хайл не найден, про причине отсутсвия на сайте
* Для решения необходимо обработать исключение
*/

public class ExcelManager implements Serializable {
    private FileLoadingListener fileLoadingListener;
    private ArrayList<SheetBlock> sheetList;
    private Boolean isXLS;
    private File file;

    //Конструктор
    public ExcelManager(File file, FileLoadingListener fileLoadingListener) {
        this.fileLoadingListener = fileLoadingListener;
        this.sheetList = new ArrayList<>();
        this.file = file;
    }

    // Начать парсить данные с файла
    public void startParser() {
        this.fileLoadingListener.onBegin();

        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            isXLS = fileName.substring(fileName.lastIndexOf(".")+1).equals("xls");

        try {

            if(isXLS){
                Log.i("ExcelManager", "Work with xls file.");

                Workbook book = WorkbookFactory.create(file);
                for(int i = 0; i < book.getNumberOfSheets(); i++)
                    fillSheetList(book.getSheetAt(i));
            } else {
                Log.i("ExcelManager", "Work with xlsx file.");

                XSSFWorkbook book = new XSSFWorkbook(file);
                for(int i = 0; i < book.getNumberOfSheets(); i++)
                    fillSheetList(book.getSheetAt(i));
            }

            for(int i = 0; i < sheetList.size(); i++)
                Log.i("ExcelManager", sheetList.get(i).toString(i));

        } catch (InvalidFormatException e) {
            fileLoadingListener.onFailure(e);
            fileLoadingListener.onEnd();
            return;
        } catch (EncryptedDocumentException e) {
            fileLoadingListener.onFailure(e);
            fileLoadingListener.onEnd();
            return;
        } catch (FileNotFoundException e){
            fileLoadingListener.onFailure(e);
            fileLoadingListener.onEnd();
            return;
        } catch (IOException e){
            fileLoadingListener.onFailure(e);
            fileLoadingListener.onEnd();
            return;
        } catch (Exception e){
            fileLoadingListener.onFailure(e);
            fileLoadingListener.onEnd();
            return;
        }

        fileLoadingListener.onSuccess();
        fileLoadingListener.onEnd();
    }

    // Пробежка по всем вкладкам
    private void fillSheetList(Sheet sheet) throws Exception{
        Log.i("ExcelManager", "Start fill sheet list = " + sheet.getSheetName() + ".");

        ArrayList<Group> groupList = new ArrayList<>();
        Cell cell = null;

        //Начало таблицы
        if ((cell = getTableStart(sheet)) == null)
            throw new Exception("Failed parser, can not find the left up cell in the tab.");

        //Получить список групп
        if ( (groupList = getGroupList(cell)) == null)
            throw new Exception("Failed parser, can not parse data in table.");

        sheetList.add(new SheetBlock(sheet, groupList));
        Log.i("ExcelManager", "Stop fill sheet list = " + sheet.getSheetName() + ".");
    }

    //Определям верхний левый уровинь таблицы
    public Cell getTableStart(Sheet sheet){
        Row row = null;
        Cell cell = null;

        for(int j = 0; j < 10; j++)
            for(int i = 0; i < 20; i++){
                if ((row = sheet.getRow(i)) == null || (cell = row.getCell(j)) == null)
                    continue;

                if (cell.getCellType() == cell.CELL_TYPE_STRING &&
                    !cell.getStringCellValue().equals("") &&
                    cell.getStringCellValue().toLowerCase().equals("понедельник"))
                {
                    Log.i("ExcelManager", "Find Left Right cell in " + String.valueOf(i) + "; (row = " + cell.getRowIndex() + "; col = " + cell.getColumnIndex() + ").");
                    return cell;
                }
            }
        return null;
    }

    //Получить список групп в таблице
    public ArrayList<Group> getGroupList(Cell cell){
        ArrayList<Group> groupList = new ArrayList<>();

        Row row = cell.getRow();
        String name = null;

        for(int i = cell.getColumnIndex(); i < row.getLastCellNum(); i++){
            cell = row.getCell(i);

            if (cell != null && cell.getCellType() == cell.CELL_TYPE_STRING)
                name = cell.getStringCellValue();
            else continue;

            if(name.equals("") || name.equals("Время") || name.toLowerCase(Locale.ROOT).equals("понедельник"))
                continue;

            groupList.add(new Group(cell.getStringCellValue(), i));
        }

        return groupList;
    }

    public ArrayList<Day> getDays(int sheetIndex){
/*
        SheetBlock sheet = sheetList.get(sheetIndex);

        //for(int i = 0; i < sheetList.size(); i++)
        //    if(sheetList.get(i).getBlockList() != null)
        //        for(int j = 0; j < sheetList.get(i).getBlockList().size(); j++)


        ArrayList<Day> days = new ArrayList<>();
        ArrayList<Element> elements = new ArrayList<>();

        for(int i = 0; i < sheetList.size(); i++){
            if(sheetList.get(i).getBlockList() != null)
            for(int j = 0; j < sheetList.get(i).getBlockList().size(); j++)
                for(int k = 0; k < sheetList.get(i).getBlockList().get(j).getGroupList().size(); k++)
                    elements.add(new Element(sheetList.get(i).getBlockList().get(j).getGroupList().get(k).getName(),"0"));

            days.add(new Day(sheetList.get(i).getSheetName(), (ArrayList<Element>) elements.clone()));
            elements.clear();
        }
*/
        return null;
    }
}
