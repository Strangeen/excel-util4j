package online.dinghuiye.core.resolution.convert;

import online.dinghuiye.api.resolution.convert.Convertor;
import online.dinghuiye.core.annotation.convert.ValueConvert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by Strangeen on 2017/6/27.
 */
public class ValueConvertConvertor implements Convertor {

    private static final Logger logger = LoggerFactory.getLogger(ValueConvertConvertor.class);

    @Override
    public Object convert(Object obj, Field field, Map<String, String> excelRecordMap) {

        logger.trace(new StringBuffer()
                .append("field: ").append(field.getName())
                .append(", obj: ").append(obj)
                .append(" run ValueConvertConvertor").toString());

        try {
            Class<? extends Convertor>[] convertorClazzes = field.getAnnotation(ValueConvert.class).value();
            for (Class<? extends Convertor> convertorClazz : convertorClazzes) {
                Convertor convertor = convertorClazz.newInstance();
                obj = convertor.convert(obj, field, excelRecordMap);
            }
            //return ConvertFactory.convertToType(field, obj);
            return obj;

        } catch (Exception e) {
            throw new RuntimeException("field注解ValueConvert转换错误，filed：" + field.getName(), e);
        }
    }
}
