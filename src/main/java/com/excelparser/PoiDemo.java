package com.excelparser;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author yanguangx
 * @date 2019/7/25
 */
public class PoiDemo {

    /** 远程地址 */
    private static final String FILE_PATH = "D:\\test.xlsx";

    /** excel标题 */
    public static final String[] COLUMN = {"name","age","birth","score"};

    public static void main(String[] args) {
        // 存放数据
        List<Map<String, String>> list = new ArrayList<>();

        Workbook workbook = null;

        try(FileInputStream inputStream = new FileInputStream(FILE_PATH)) {
            workbook = new XSSFWorkbook(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (workbook != null) {
            // 获取第一个sheet
            Sheet sheet = workbook.getSheetAt(0);

            // 获取行数
            int numberOfRows = sheet.getPhysicalNumberOfRows();

            // 获取(第一行)列数
            int numberOfCells = sheet.getRow(0).getPhysicalNumberOfCells();

            Row row = null;
            String cellValue = "";
            Map<String, String> map = null;
            for (int i = 1; i < numberOfRows; i++) {
                row = sheet.getRow(i);
                if (row != null) {
                    map = new LinkedHashMap<>();
                    for (int j = 0; j < numberOfCells; j++) {
                        Cell cell = row.getCell(j);
                        cell.setCellType(CellType.STRING);
                        cellValue = cell.getStringCellValue();
                        map.put(COLUMN[j], cellValue);
                    }
                    list.add(map);
                }
            }
        }
        System.out.println(list.toString());
    }
}
