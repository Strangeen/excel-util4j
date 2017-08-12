package online.dinghuiye.core;

import online.dinghuiye.api.AbstractExcel;
import online.dinghuiye.api.entity.RowRecord;
import online.dinghuiye.api.entity.TransactionMode;
import online.dinghuiye.api.persistence.RowRecordPerPersistentRepairer;
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
 * <p>excel导入入口类</p>
 *
 * @author Strangeen
 * on 2017/8/6
 */
public class ImportHandler {

    private RowRecordHandler handler;
    private RowRecordValidator validator;
    private RowRecordPersistencor persistencor;
    private TransactionMode mode;
    private RowRecordPerPersistentRepairer repairer;

    public void setHandler(RowRecordHandler handler) { this.handler = handler; }

    public void setValidator(RowRecordValidator validator) {
        this.validator = validator;
    }

    public void setPersistencor(RowRecordPersistencor persistencor) {
        this.persistencor = persistencor;
    }

    public void setMode(TransactionMode mode) {
        this.mode = mode;
    }

    public void setRepairer(RowRecordPerPersistentRepairer repairer) {
        this.repairer = repairer;
    }


    public ImportHandler() {}

    /**
     *
     * @param handler excel解析实现<br>
     *                            实现：
     *                            {@link RowRecordHandlerSinglePojoImpl}，
     *                            {@link RowRecordHandler}
     * @param validator pojo对象验证实现<br>
     *                  实现：{@link RowRecordValidatorImpl}
     * @param persistencor 持久化实现<br>
     *                     实现：配套包persistence-hibernate-impl中RowRecordPersistencorHibernateImpl类
     * @param mode 事务形式，参见{@link TransactionMode}
     * @param repairer 持久化前修正List&lt;RowRecord&gt;数据实现
     */
    public ImportHandler(RowRecordHandler handler, RowRecordValidator validator, RowRecordPersistencor persistencor, TransactionMode mode, RowRecordPerPersistentRepairer repairer) {
        this.handler = handler;
        this.validator = validator;
        this.persistencor = persistencor;
        this.mode = mode;
        this.repairer = repairer;
    }

    /**
     * <p>{@link RowRecordHandler}默认使用{@link RowRecordHandlerSinglePojoImpl}</p>
     * <p>{@link RowRecordValidator}默认使用{@link RowRecordValidatorImpl}</p>
     *
     * @param persistencor 持久化实现<br>
     *                     备选参见：配套包persistence-hibernate-impl中RowRecordPersistencorHibernateImpl类
     * @param mode 事务形式，参见{@link TransactionMode}
     */
    public ImportHandler(RowRecordPersistencor persistencor, TransactionMode mode) {
        this.handler = new RowRecordHandlerSinglePojoImpl();
        this.validator = new RowRecordValidatorImpl();
        this.persistencor = persistencor;
        this.mode = mode;
    }

    /**
     * <p>{@link RowRecordValidator}默认使用{@link RowRecordValidatorImpl}</p>
     *
     * @param handler excel解析实现<br>
     *                            实现：
     *                            {@link RowRecordHandlerSinglePojoImpl}，
     *                            {@link RowRecordHandler}
     * @param persistencor 持久化实现<br>
     *                     实现：配套包persistence-hibernate-impl中RowRecordPersistencorHibernateImpl类
     * @param mode 事务形式，参见{@link TransactionMode}
     */
    public ImportHandler(RowRecordHandler handler, RowRecordPersistencor persistencor, TransactionMode mode) {
        this.handler = handler;
        this.validator = new RowRecordValidatorImpl();
        this.persistencor = persistencor;
        this.mode = mode;
    }

    /**
     * <p>导入excel方法，默认读取第一张sheet中的数据</p>
     *
     * @param excel 需要导入的excel对象
     * @param processObserver 进度处理器
     * @param pojos 导入对象的pojo类
     * @return RowRecord的list，通过getResult可以获取导入结果
     */
    public List<RowRecord> importExcel(AbstractExcel excel, Observer processObserver, Class<?>... pojos) {

        return importExcel(excel, 0, processObserver, pojos);
    }


    /**
     * <p>导入excel方法</p>
     *
     * @param excel 需要导入的excel对象
     * @param sheetNo excel中sheet的编号
     * @param processObserver 进度处理器
     * @param pojos 导入对象的pojo类
     * @return RowRecord的list，通过getResult可以获取导入结果
     */
    public List<RowRecord> importExcel(AbstractExcel excel, int sheetNo, Observer processObserver, Class<?>... pojos) {

        List<Map<String, Object>> excelDataList = excel.readExcel(sheetNo);

        List<RowRecord> rowRecordList = new ArrayList<>();
        for (int i = 0; i < excelDataList.size(); i ++) {
            Map<String, Object> excelData = excelDataList.get(i);
            rowRecordList.add(handler.rowRecordCreate(excelData, i + 2));
        }

        if (!handler.handle(rowRecordList, pojos) && mode == TransactionMode.MULTIPLE)
            return rowRecordList;
        if (!validator.valid(rowRecordList) && mode == TransactionMode.MULTIPLE)
            return rowRecordList;
        if (repairer != null) repairer.repaire(rowRecordList); // 持久化前修正rowRecord，如：密码md5加密转换
        persistencor.persist(rowRecordList, mode, processObserver);
        return rowRecordList;
    }
}
