package online.dinghuiye.core.entity;

/**
 * Created by Strangeen on 2017/6/26.
 */
public class SheetInfo {

    private int sheetIndex;
    private String sheetName;

    public SheetInfo(int sheetIndex, String sheetName) {
        this.sheetIndex = sheetIndex;
        this.sheetName = sheetName;
    }

    public int getSheetIndex() {
        return sheetIndex;
    }

    public void setSheetIndex(int sheetIndex) {
        this.sheetIndex = sheetIndex;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }
}
