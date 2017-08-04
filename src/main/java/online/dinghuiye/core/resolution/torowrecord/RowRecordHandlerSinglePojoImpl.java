package online.dinghuiye.core.resolution.torowrecord;

import online.dinghuiye.api.resolution.torowrecord.RowRecordHandler;
import online.dinghuiye.core.annotation.excel.SheetTitleName;
import online.dinghuiye.core.annotation.excel.Transient;
import online.dinghuiye.core.entity.ResultStatus;
import online.dinghuiye.core.entity.RowRecord;
import online.dinghuiye.core.entity.RowRecordHandleResult;
import online.dinghuiye.core.resolution.convert.ConvertKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Strangeen on 2017/6/27.
 *
 * 实现只考虑单表单pojo的插入操作，TODO 后期重新实现多表一对一关联插入
 */
public class RowRecordHandlerSinglePojoImpl implements RowRecordHandler {

    private static final Logger logger = LoggerFactory.getLogger(RowRecordHandlerSinglePojoImpl.class);

    @Override
    public List<RowRecord> handle(List<Map<String, String>> excelRowDataList, Class<?>... pojos) {

        List<RowRecord> rowRecordList = new ArrayList<RowRecord>();
        for (int row = 0; row < excelRowDataList.size(); row ++) {

            // +2才对应excel行号，row起始为0，+1表示其实从1开始，再+1表示表头行，所以共+2
            RowRecord rowRecord = rowRecordCreate(excelRowDataList.get(row), row + 2);
            pojoHandle(rowRecord, pojos);
            rowRecordList.add(rowRecord);
        }

        return rowRecordList;
    }

    /**
     * 创建rowRecord，初始化基础数据
     * @param excelRowData
     * @param row
     * @return
     */
    private RowRecord rowRecordCreate(Map<String, String> excelRowData, int row) {
        RowRecord rowRecord = new RowRecord();
        rowRecord.setRowNo(row);
        rowRecord.setExcelRecordMap(excelRowData);
        return rowRecord;
    }

    /**
     * 装载pojo，TODO 暂时只实现单个pojo
     * @param rowRecord
     * @param pojos
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private RowRecord pojoHandle(RowRecord rowRecord, Class<?>... pojos) {

        try {
            Class<?> pojo = pojos[0];
            Object pojoObj = pojo.newInstance();
            rowRecord.set(pojo, pojoObj);

            Field[] fields = pojo.getDeclaredFields();
            for (Field field : fields) {

                Transient transientAnno = field.getAnnotation(Transient.class);
                if (transientAnno != null) continue;

                /*String sheetTitleName = field.getName();
                SheetTitleName sheetTitleNameAnno = field.getAnnotation(SheetTitleName.class);
                if (sheetTitleNameAnno != null && !"".equals(sheetTitleNameAnno.value())) {
                    sheetTitleName = sheetTitleNameAnno.value();
                }*/
                String sheetTitleName = RowRecordKit.getSheetTitleNameByFieldName(field);

                // 按照pojo属性转换
                String excelValue = rowRecord.get(sheetTitleName);
                Object fieldValue = ConvertKit.convert(excelValue, field, rowRecord.getExcelRecordMap());
                field.setAccessible(true);
                field.set(pojoObj, fieldValue);

                // 成功后写入状态
                rowRecord.setResult(new RowRecordHandleResult(ResultStatus.SUCCESS, null));
            }
        } catch (Exception e) {
            logger.debug("pojo对象装载出错", e);
            rowRecord.setResult(new RowRecordHandleResult(ResultStatus.FAIL, "解析错误"));
        }

        return rowRecord;
    }

}
