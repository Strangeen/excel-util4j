package online.dinghuiye.core.excel;

import online.dinghuiye.api.excel.AbstractExcel;
import online.dinghuiye.api.excel.ExcelHandler;
import online.dinghuiye.core.entity.RowRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Strangeen on 2017/6/26.
 */
public class ExcelPoiHandler implements ExcelHandler {

    /**
     * 将excel数据按行转换为RowRecord对象
     * 注：该方法默认表头为第一行
     */
    /*@Override
    public List<RowRecord> handle(AbstractExcel excel) {

        List<RowRecord> rowRecordsList = new ArrayList<>();
        int sheetRowNum = excel.getCurrentSheetRowNum();
        List<String> sheetTitleNameList = excel.getCurrentSheetTitleNameList();

        for (int rowNo = 1; rowNo < sheetRowNum; rowNo ++) {
            RowRecord rowRecord = new RowRecord();
            for (int colNo = 0; colNo < sheetTitleNameList.size(); colNo ++) {
                rowRecord.set(sheetTitleNameList.get(colNo), excel.readCell(rowNo, colNo));
            }
            rowRecordsList.add(rowRecord);
        }
        return rowRecordsList;
    }*/

    @Override
    public RowRecord handle(AbstractExcel excel, int rowNo) {

        List<String> sheetTitleNameList = excel.getCurrentSheetTitleNameList();
        RowRecord rowRecord = new RowRecord();
        for (int colNo = 0; colNo < sheetTitleNameList.size(); colNo ++) {
            rowRecord.set(sheetTitleNameList.get(colNo), excel.readCell(rowNo, colNo));
        }

        return rowRecord;
    }
}
