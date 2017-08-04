package online.dinghuiye.core.resolution.convert;

import online.dinghuiye.api.resolution.convert.Convertor;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * Created by Strangeen on 2017/8/3.
 */
public class ConvertKit {

    public static Object convert(String excelValue, Field field, Map<String, String> excelRecordMap) {
        List<Convertor> convertorList = ConvertFactory.getConvertors(field);
        Object value = excelValue;
        // 转换器转换
        for (Convertor convertor : convertorList) {
            value = convertor.convert(value, field, excelRecordMap);
        }
        // 转换为对应属性类型
        value = ConvertFactory.convertToType(field, value);

        return value;
    }

}
