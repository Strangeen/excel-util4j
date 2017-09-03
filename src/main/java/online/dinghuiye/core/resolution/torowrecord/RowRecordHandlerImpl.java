package online.dinghuiye.core.resolution.torowrecord;

import online.dinghuiye.api.entity.ResultStatus;
import online.dinghuiye.api.entity.RowRecord;
import online.dinghuiye.core.annotation.excel.Transient;
import online.dinghuiye.core.common.FieldFactory;
import online.dinghuiye.core.resolution.convert.ConvertKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>多层关联pojo解析</p>
 *
 * <p>按照hibernate关联pojo方式进行解析，即pojos可能只包含一个类，但类对象中引用其他类对象作为属性
 * （pojos可以包含N个类，是可以全部解析的）</p>
 *
 * <p>需要注意：pojos无法解析引用类为集合或数组的情况，即只能解析一对一关系的数据</p>
 *
 * @author Strangeen on 2017/08/09
 *
 * @author Strangeen on 2017/9/3
 * @version 2.1.0
 */
public class RowRecordHandlerImpl extends AbstractRowRecordHandler {

    private static final Logger logger = LoggerFactory.getLogger(RowRecordHandlerSinglePojoImpl.class);

    /**
     * 装载pojo数组对象
     *
     * @param rowRecord {@link RowRecord}对象
     * @param pojos pojo类数组
     * @return true - RowRecord解析成功{@link ResultStatus#SUCCESS}
     *         false - RowRecord解析失败{@link ResultStatus}
     */
    @Override
    protected boolean pojoHandle(RowRecord rowRecord, Class<?>... pojos) {

        if (pojos.length <= 0) throw new RuntimeException("pojos未定义");
        boolean allSuccess = true;
        for (Class<?> rootPojo : pojos) {
            // 填充pojo属性为自定义类型的情况，优先从cache中查找，否则解析rowRecord.excelRecordMap
            Map<Class<?>, Object> pojoObjCache = new HashMap<>();
            try {
                pojoHandle(rowRecord, rootPojo, pojoObjCache, true);
            } catch (RuntimeException e) {
                logger.warn("pojo对象装载出错", e);
                allSuccess = false;
            }
        }
        return allSuccess;
    }

    /**
     * 装载单个pojo
     *
     * @param rowRecord {@link RowRecord}
     * @param pojo pojo类
     * @param pojoObjCache pojo对象缓存，用于创建pojo对象关联关系
     */
    private void pojoHandle(RowRecord rowRecord, Class<?> pojo, Map<Class<?>, Object> pojoObjCache, boolean isRootClass) {
        String sheetTitleName = null;
        Object fieldValue = null;
        try {
            Object mainPojoObj = pojo.newInstance();
            pojoObjCache.put(pojo, mainPojoObj);
            if (isRootClass)
                rowRecord.set(pojo, mainPojoObj);

            Field[] fields = FieldFactory.getFields(pojo);
            for (Field field : fields) {

                Transient transientAnno = field.getAnnotation(Transient.class);
                if (transientAnno != null) continue;

                Class<?> linkPojo = field.getType();
                if (linkPojo != null && linkPojo.getClassLoader() != null) { // 自定义类型
                    Object linkPojoObj = pojoObjCache.get(linkPojo);
                    if (linkPojoObj == null) {
                        pojoHandle(rowRecord, linkPojo, pojoObjCache, false);
                        linkPojoObj = pojoObjCache.get(linkPojo);
                    }
                    field.setAccessible(true);
                    field.set(mainPojoObj, linkPojoObj);
                    continue;
                }

                // 获取属性对应的excel表头名称
                sheetTitleName = RowRecordKit.getSheetTitleNameByField(field);

                // 按照pojo属性转换
                Object excelValue = rowRecord.get(sheetTitleName);
                fieldValue = ConvertKit.convert(excelValue, field, rowRecord.getExcelRecordMap());
                field.setAccessible(true);
                field.set(mainPojoObj, fieldValue);
            }

        } catch (Exception e) {
            if (!e.getMessage().contains("解析错误")) { // 只提示当前错误，避免随后的解析的跟随报错
                rowRecord.getResult().setResult(ResultStatus.FAIL).setMsg(sheetTitleName + "解析错误，行：" + rowRecord.getRowNo());
                throw new RuntimeException(sheetTitleName + "解析错误，值：" + fieldValue + "，行：" + rowRecord.getRowNo(), e);
            }
            else
                throw new RuntimeException(e);
        }
    }
}
