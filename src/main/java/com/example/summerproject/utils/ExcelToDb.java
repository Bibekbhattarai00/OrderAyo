package com.example.summerproject.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExcelToDb {
    public static <T> List<T> createExcel(MultipartFile file, Class<T> entityType) throws IOException, IllegalAccessException, InstantiationException {
        InputStream inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);//Creates a XSSFWorkbook from the input stream.
        Sheet sheet = workbook.getSheetAt(0);//first sheet
        List<T> entities = new ArrayList<>();

        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {// Iterates through each row of the sheet, starting from index 1 to skip the header row.
            Row row = sheet.getRow(rowIndex);//Get current row.

//The newInstance() method is a method of the Class 'class' and is part of Java's reflection API.
// It's typically used when the type of the class is not known at compile time.

            T entity = entityType.newInstance();  //creates the object of entity
            setEntityFieldsFromRow(entity, row);//set value to the fields
            entities.add(entity);//add the value to the empty list entities
        }
        return entities;//return list of data stores in entity
    }

    private static <T> void setEntityFieldsFromRow(T entity, Row row) throws IllegalAccessException {
        Field[] fields = entity.getClass().getDeclaredFields();

        for (int cellIndex = 0; cellIndex < fields.length; cellIndex++) {//iterates over each field in the array fields
            Field field = fields[cellIndex];// get the current field from the array Field at the index cellIndex such that field stores the current field

            field.setAccessible(true);

            Cell cell = row.getCell(cellIndex);
            field.set(entity, getCellValue(cell, field.getType()));//set the value of the current field of provided entity with the value obtained from the cell.
        }
    }

    private static Object getCellValue(Cell cell, Class<?> fieldType) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
                if (fieldType == String.class) {
                    return cell.getStringCellValue();
                } else {
                    return null;
                }
            case NUMERIC:
                if (fieldType == Integer.class || fieldType == int.class) {
                    return cell.getNumericCellValue();
                }else if (fieldType == Double.class || fieldType == double.class) {
                    return (int) cell.getNumericCellValue();
                } else if (fieldType == Date.class) {
                    return cell.getDateCellValue();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return cell.getBooleanCellValue();
            default:
                return null;
        }
    }

}
