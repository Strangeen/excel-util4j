package online.dinghuiye.api.resolution.convert;

import online.dinghuiye.core.entity.RowRecord;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by Strangeen on 2017/6/27.
 */
public interface Convertor {

    /**
     * 将excel元数据转换为对应pojo属性类型的值
     * @param obj 需要转换的值，如果是excel元数据则为String
     * @param field pojo属性字段
     * @param excelRecordMap excel元数据map<表头名称, 单元格值>（预留参数）
     * @return 转换后的值
     */
    Object convert(Object obj, Field field, Map<String, String> excelRecordMap);

    //<T> T getInstance();
}
