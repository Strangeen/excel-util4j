package online.dinghuiye.core.excel;


import online.dinghuiye.api.excel.AbstractExcel;
import online.dinghuiye.core.entity.SheetInfo;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Strangeen on 2017/6/26.
 */
public class XlsExcel extends AbstractExcel {

    private static final Logger logger = LoggerFactory.getLogger(XlsExcel.class);

    private static final int firstSheetNo = 0;
    private static final int firstRowNo = 0;

    private Workbook wb;
    private List<Sheet> sheetList = new ArrayList<>();
    private Map<String, Integer> sheetNameIndexMap = new HashedMap<>(); // map<sheetName, sheetIndex>

    private Sheet currentSheet;

    protected Workbook getWb() {
        return wb;
    }

    protected List<Sheet> getSheetList() {
        return sheetList;
    }

    protected Sheet getCurrentSheet() {
        return currentSheet;
    }

    /**
     * 打开excel文件，默认选中第一个sheet
     * @param excelFile xls文件
     */
    @Override
    public void open(File excelFile) {
        try {
            super.excelFile = excelFile;
            super.fis = new FileInputStream(excelFile);
            readWorkbook(fis);
            readSheetMap();
            switchSheet(firstSheetNo); // 默认第一个sheet
            logger.debug("excel打开成功");

        } catch (Exception e) {
            logger.error("excel打开失败");
            throw new RuntimeException(e);

        }
    }

    private void readWorkbook(InputStream fis) {
        try {
            this.wb = new HSSFWorkbook(fis);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void readSheetMap() {
        int sheetNum = wb.getNumberOfSheets();
        for (int i = 0; i < sheetNum; i ++) {
            Sheet sheet = wb.getSheetAt(i);
            //this.sheetMap.put(sheet.getSheetName(), sheet);
            this.sheetList.add(sheet);
            this.sheetNameIndexMap.put(sheet.getSheetName(), i);
        }
    }

    /**
     * 注意：切换sheet时会设置行数和列数，并认为excel为关系型表
     * @param sheetIndex 从0开始
     * @return 切换到的sheet信息
     */
    @Override
    public SheetInfo switchSheet(int sheetIndex) {
        this.currentSheet = sheetList.get(sheetIndex);
        this.rowHashMap = new HashMap<>(); // 切换sheet后重置row缓存hashMap
        super.currentSheetTitleNameList = new ArrayList<>(); // 切换sheet后重置titleNameList

        readSheetRowNum(currentSheet);
        readSheetColNum(currentSheet);
        readSheetTitleNameList(currentSheet);

        SheetInfo sheetInfo = new SheetInfo(sheetIndex, currentSheet.getSheetName());
        return sheetInfo;
    }

    /**
     *
     * @param sheetName sheet名称，大小写敏感
     * @return 切换到的sheet信息
     */
    public SheetInfo switchSheet(String sheetName) {
        //this.currentSheet = sheetMap.get(sheetName);
        int sheetIndex = this.sheetNameIndexMap.get(sheetName);
        return switchSheet(sheetIndex);
    }

    private void readSheetRowNum(Sheet sheet) {
        super.currentSheetRowNum = sheet.getPhysicalNumberOfRows();
    }


    // 认为excel为关系型表，因此取第一行计算列数
    private void readSheetColNum(Sheet sheet) {
        Row firstRow = sheet.getRow(firstRowNo);
        super.currentSheetColNum = 0;
        if (firstRow != null) super.currentSheetColNum = firstRow.getPhysicalNumberOfCells();
    }

    // 认为第一行为表头行
    private void readSheetTitleNameList(Sheet sheet) {
        Row firstRow = sheet.getRow(firstRowNo);
        if (firstRow != null) {
            int rowNo = firstRowNo;
            for (int colNo = 0; colNo < currentSheetColNum; colNo ++) {
                super.currentSheetTitleNameList.add(readCell(rowNo, colNo));
            }
        }
    }

    /*
     * 根据poi读取cell的原理进行缓存读取到的row：
     * poi使用TreeMap保存row的集合，执行getRow(index)将到TreeMap中查找。
     * XlsExcel继承AbstractExcel需要实现readCell(rowNo, colNo)，与getRow方法不一致，
     * 实现需要每读取一个表格就执行一次getRow，即查找一次TreeMap，
     * 而TreeMap的查找速度不及HashMap，因此缓存row的集合到HashMap
     */
    private HashMap<Integer, Row> rowHashMap = new HashMap<>();

    /**
     *
     * @param rowNo 从0开始
     * @param colNo 从0开始
     * @return 单元格字符串形式的内容
     */
    @Override
    public String readCell(int rowNo, int colNo) {
        Row row = rowHashMap.get(rowNo);
        if (row == null) {
            row = currentSheet.getRow(rowNo);
            rowHashMap.put(rowNo, row);
        }
        Cell cell = row.getCell(colNo);
        return cell.getStringCellValue();
    }

    @Override
    public void close() {
        try {
            if (wb != null) {
                wb.close();
                logger.debug("workbook关闭成功");
            }
        } catch (Exception e) {
            logger.error("workbook关闭失败");
            throw new RuntimeException(e);
        }
        try {
            if (fis != null) {
                fis.close();
                logger.debug("fileinputstream关闭成功");
            }
        } catch (Exception e) {
            logger.error("fileinputstream关闭失败");
            throw new RuntimeException(e);
        }
    }
}
