package online.dinghuiye.core.resolution.convert;

import online.dinghuiye.api.resolution.convert.Convertor;
import online.dinghuiye.core.annotation.convert.ValueConvert;
import online.dinghuiye.core.entity.RowRecord;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by Strangeen on 2017/6/27.
 */
public class ValueConvertConvertor implements Convertor {

    @Override
    public Object convert(Object obj, Field field, Map<String, String> excelRecordMap) {

        try {
            Class<? extends Convertor>[] convertorClazzes = field.getAnnotation(ValueConvert.class).value();
            for (Class<? extends Convertor> convertorClazz : convertorClazzes) {
                Convertor convertor = convertorClazz.newInstance();
                obj = convertor.convert(obj, field, excelRecordMap);
            }
            return ConvertFactory.convertToType(field, obj);

        } catch (Exception e) {
            throw new RuntimeException("field注解ValueConvert转换错误，filed：" + field.getName(), e);
        }
    }
}
