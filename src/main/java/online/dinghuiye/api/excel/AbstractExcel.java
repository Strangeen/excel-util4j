package online.dinghuiye.api.excel;

import online.dinghuiye.core.entity.SheetInfo;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Strangeen on 2017/6/26.
 */
public abstract class AbstractExcel {

    protected File excelFile;
    protected InputStream fis;

    protected int currentSheetRowNum; // 包含表头行，数据行为 rowNum - 1
    protected int currentSheetColNum;
    protected List<String> currentSheetTitleNameList = new ArrayList<>();


    public File getExcelFile() {
        return excelFile;
    }

    public int getCurrentSheetRowNum() {
        return currentSheetRowNum;
    }

    public int getCurrentSheetColNum() {
        return currentSheetColNum;
    }

    public List<String> getCurrentSheetTitleNameList() {
        return currentSheetTitleNameList;
    }

    public abstract void open(File excelFile);

    public abstract void close();

    public abstract SheetInfo switchSheet(int sheetIndex);

    public abstract String readCell(int rowNo, int colNo);
}
