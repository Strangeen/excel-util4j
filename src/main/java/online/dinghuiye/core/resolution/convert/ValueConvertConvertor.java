package online.dinghuiye.core.resolution.convert;

import online.dinghuiye.api.resolution.convert.Convertor;
import online.dinghuiye.core.annotation.convert.ValueConvert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * {@link ValueConvert}注解实现类
 *
 * @author Strangeen
 * on 2017/6/27
 */
public class ValueConvertConvertor implements Convertor {

    private static final Logger logger = LoggerFactory.getLogger(ValueConvertConvertor.class);

    @Override
    public Object convert(Object obj, Field field, Map<String, Object> excelRecordMap) {

        logger.trace("field: " + field.getName() +
                        ", obj: " + obj +
                        " run ValueConvertConvertor");

        try {
            Class<? extends Convertor>[] convertorClazzes = field.getAnnotation(ValueConvert.class).value();
            for (Class<? extends Convertor> convertorClazz : convertorClazzes) {
                Convertor convertor = convertorClazz.newInstance();
                obj = convertor.convert(obj, field, excelRecordMap);
            }
            return obj;

        } catch (Exception e) {
            throw new RuntimeException("field注解ValueConvert转换错误，filed：" + field.getName(), e);
        }
    }
}
