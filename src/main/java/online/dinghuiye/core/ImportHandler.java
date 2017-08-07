package online.dinghuiye.core;

import online.dinghuiye.api.AbstractExcel;
import online.dinghuiye.api.entity.RowRecord;
import online.dinghuiye.api.persistence.RowRecordPersistencor;
import online.dinghuiye.api.resolution.torowrecord.RowRecordHandler;
import online.dinghuiye.api.validation.RowRecordValidator;
import online.dinghuiye.core.resolution.torowrecord.RowRecordHandlerSinglePojoImpl;
import online.dinghuiye.core.validation.RowRecordValidatorImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observer;

/**
 * Created by Strangeen on 2017/8/6.
 *
 * 导入handler
 * 先实现持久操作对象传入，后期更改为SpringBean
 */
public class ImportHandler {

    private RowRecordHandler convertorAndHandler;
    private RowRecordValidator validator;
    private RowRecordPersistencor persistencor;

    public ImportHandler(RowRecordPersistencor persistencor) {
        this.convertorAndHandler = new RowRecordHandlerSinglePojoImpl();
        this.validator = new RowRecordValidatorImpl();
        this.persistencor = persistencor;
    }

    /**
     * 读取第一张sheet中的数据
     * @param excel 需要导入的excel对象
     * @param processObserver 进度处理器
     * @param pojos 导入对象的pojo类
     * @return RowRecord的list，通过getResult可以获取导入结果
     */
    public List<RowRecord> importExcel(AbstractExcel excel, Observer processObserver, Class<?>... pojos) {

        List<Map<String, String>> excelDataList = excel.readExcel(0);

        List<RowRecord> rowRecordList = new ArrayList<RowRecord>();
        for (int i = 0; i < excelDataList.size(); i ++) {
            Map<String, String> excelData = excelDataList.get(i);
            rowRecordList.add(convertorAndHandler.rowRecordCreate(excelData, i + 2));
        }

        convertorAndHandler.handle(rowRecordList, pojos);
        validator.valid(rowRecordList);
        persistencor.persist(rowRecordList, processObserver);

        // 返回rowRecord信息
        return rowRecordList;
    }
}
