package com.teg.analysis.util;

import com.teg.analysis.exception.BusinessException;
import com.google.common.collect.Maps;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtils {

    protected static final Logger logger = LoggerFactory.getLogger(ExcelUtils.class);


    public static Map readExcelFromUpload(MultipartFile file) throws Exception {
        return readExcel(parseMultipartFile(file));
    }

    public static Map readExcelFromPath(String path) throws Exception {
        return readExcel(parseFilePath(path));
    }


    public static Map<Integer, Map<Integer, String>> readExcel(Workbook wb) {
        Map<Integer, Map<Integer, String>> content = Maps.newHashMap();
        Sheet sheet = wb.getSheetAt(0);
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        Row row = sheet.getRow(0);
        int colNum = row.getPhysicalNumberOfCells();
        // 正文内容应该从第二行开始,第一行为表头的标题
        for (int i = 1; i <= rowNum; i++) {
            row = sheet.getRow(i);
            int j = 0;
            Map<Integer, String> cellValue = new HashMap<>();
            while (j < colNum) {
                String obj = getCellFormatValue(row.getCell(j));
                cellValue.put(j, obj);
                j++;
            }
            content.put(i, cellValue);
        }
        return content;
    }

    //根据Cell类型设置数据
    private static String getCellFormatValue(Cell cell) {
        String cellvalue = "";
        if (cell != null) {
            switch (cell.getCellTypeEnum()) {
                case NUMERIC:
                    cellvalue = String.valueOf(cell.getNumericCellValue());
                    break;
                case FORMULA: {
                    cellvalue = String.valueOf(cell.getDateCellValue());
                    break;
                }
                case STRING:
                    cellvalue = cell.getRichStringCellValue().getString();
                    break;
                default:
                    cellvalue = "";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;
    }


    private static Workbook parseFilePath(String filepath) throws FileNotFoundException {
        String ext = filepath.substring(filepath.lastIndexOf("."));
        return getWb(ext, new FileInputStream(new File(filepath)));
    }

    private static Workbook parseMultipartFile(MultipartFile mf) throws IOException {
        String filepath = mf.getOriginalFilename();
        String ext = filepath.substring(filepath.lastIndexOf("."));
        return getWb(ext, mf.getInputStream());
    }

    private static Workbook getWb(String ext, InputStream is) {
        Workbook wb = null;
        try {
            if (".xls".equals(ext)) {
                wb = new HSSFWorkbook(is);
            } else if (".xlsx".equals(ext)) {
                wb = new XSSFWorkbook(is);
            } else {
                wb = null;
            }
        } catch (FileNotFoundException e) {
            logger.error("FileNotFoundException", e);
        } catch (IOException e) {
            logger.error("IOException", e);
        }
        if (wb == null) {
            throw new BusinessException("无法获取文件");
        }
        return wb;
    }


    /**
     * excel导出
     */
    public static Workbook generateWorkbook(String[] titles, List<List<String>> dataList) {
        try {
            // 第一步，创建一个workbook，对应一个Excel文件
            HSSFWorkbook workbook = new HSSFWorkbook();

            // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
            HSSFSheet hssfSheet = workbook.createSheet("sheet1");

            // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
            HSSFRow row = hssfSheet.createRow(0);
            row.setHeight((short) (5 * row.getHeight()));
            // 第四步，创建单元格，并设置值表头 设置表头居中
            HSSFCellStyle hssfCellStyle = workbook.createCellStyle();
            hssfCellStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
            hssfCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
            hssfCellStyle.setFillForegroundColor((short) 70);// 设置背景色 //todo
            hssfCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            //  row.setHeight((short) 25);

            HSSFFont font = workbook.createFont();
            font.setFontName("等线");
            font.setFontHeightInPoints((short) 14);//设置字体大小

            font.setBold(true);//粗体显示


            hssfCellStyle.setFont(font);//选择需要用到的字体格式

            HSSFCell hssfCell;
            for (int i = 0; i < titles.length; i++) {
                hssfCell = row.createCell(i);//列索引从0开始
                hssfCell.setCellValue(titles[i]);//列名1
                hssfCell.setCellStyle(hssfCellStyle);//列居中显示
            }


            for (int i = 0; i < dataList.size(); i++) {
                row = hssfSheet.createRow(i + 1);
                List<String> rowList = dataList.get(i);
                int count = 0;
                for (String s : rowList) {
                    row.createCell(count).setCellValue(s);
                    count++;
                }
            }

            return workbook;

        } catch (Exception e) {
            logger.error("生成excel失败", e);
            throw new BusinessException("生成excel失败");
        }
    }


    public static void export(String url, Workbook workbook) {
        FileOutputStream fileOut;
        try {
            File exportFile = new File(url);
            exportFile.delete();
            exportFile.createNewFile();
            fileOut = new FileOutputStream(url);
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();

        } catch (Exception e) {
            logger.error("导出excel失败", e);
            throw new BusinessException("导出excel失败");
        }
    }

}
