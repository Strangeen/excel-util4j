package online.dinghuiye.core.excel;

import online.dinghuiye.api.excel.AbstractExcel;
import online.dinghuiye.core.entity.SheetInfo;
import org.apache.poi.ss.usermodel.Row;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Strangeen on 2017/6/26.
 */
public class TestXlsExcel {

    private AbstractExcel excel;

    @Before
    public void testOpen() throws FileNotFoundException {
        File xls = new File("/test/test.xls");
        //File xls = new File("/test/test_empty.xls");
        this.excel = new XlsExcel();
        excel.open(xls);
    }

    @Test
    public void testSwitchSheetByIndex() {
        int index = 0;
        SheetInfo sheet = excel.switchSheet(index);
        Assert.assertEquals(index, sheet.getSheetIndex());
    }

    @Test
    public void testRowNumAfterSwitchSheetByIndex() {
        excel.switchSheet(0);
        Assert.assertEquals(((XlsExcel)excel).getCurrentSheet().getPhysicalNumberOfRows(),
                excel.getCurrentSheetRowNum());
    }

    @Test
    public void testColNumAfterSwitchSheetByIndex() {
        excel.switchSheet(0);
        int colNum = 0;
        Row row = ((XlsExcel)excel).getCurrentSheet().getRow(0);
        if (row != null) colNum = row.getPhysicalNumberOfCells();
        Assert.assertEquals(colNum, excel.getCurrentSheetRowNum());
    }

    @Test
    public void testTitleNameListAfterSwitchSheetByIndex() {
        excel.switchSheet(0);
        List<String> titleNameList = new ArrayList<>();
        titleNameList.add("test");
        Assert.assertArrayEquals(
                titleNameList.toArray(new Object[titleNameList.size()]),
                excel.getCurrentSheetTitleNameList().toArray(new Object[titleNameList.size()]));
    }

    @Test
    public void testSwitchSheetByName() {
        String name = "Sheet1";
        SheetInfo sheet = ((XlsExcel)excel).switchSheet(name);
        Assert.assertEquals(name, sheet.getSheetName());
    }

    @Test
    public void testReadCell() throws IOException {
        String value = excel.readCell(0, 0);
        Assert.assertEquals("test", value);
    }

    @After
    public void testClose() {
        excel.close();
    }
}
