package online.dinghuiye.core.resolution.torowrecord;

import online.dinghuiye.api.entity.ResultStatus;
import online.dinghuiye.api.entity.RowRecord;
import online.dinghuiye.api.entity.RowRecordHandleResult;
import online.dinghuiye.core.annotation.excel.SheetTitleName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * RowRecord工具包
 *
 * @author Strangeen
 * on 2017/8/4
 */
public class RowRecordKit {

    /**
     * 通过属性{@link SheetTitleName}注解获取excel的表头名称
     *
     * @param field 属性Field
     * @return 属性对应的excel表头名称
     */
    public static String getSheetTitleNameByFieldName(Field field) {
        try {
            String sheetTitleName = field.getName();
            SheetTitleName sheetTitleNameAnno = field.getAnnotation(SheetTitleName.class);
            if (sheetTitleNameAnno != null && !"".equals(sheetTitleNameAnno.value())) {
                sheetTitleName = sheetTitleNameAnno.value();
            }

            return sheetTitleName;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /*
     * 通过类和属性名获取该属性的excel表头名称
     * @param clazz
     * @param fieldName
     * @return
     */
    /*public static String getSheetTitleNameByFieldName(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            return getSheetTitleNameByFieldName(field);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }*/


    /**
     * 创建{@link RowRecord}，初始化基础数据
     *
     * @param excelRowData excel行数据
     * @param row excel行号
     * @return {@link RowRecord}对象
     */
    public static RowRecord createRowRecord(Map<String, Object> excelRowData, Integer row) {
        RowRecord rowRecord = new RowRecord();
        rowRecord.setRowNo(row);
        rowRecord.setExcelRecordMap(excelRowData);
        rowRecord.setResult(new RowRecordHandleResult(ResultStatus.SUCCESS, null));
        return rowRecord;
    }

}
