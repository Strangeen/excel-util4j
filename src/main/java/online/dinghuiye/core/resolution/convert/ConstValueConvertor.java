package online.dinghuiye.core.resolution.convert;

import online.dinghuiye.api.resolution.convert.Convertor;
import online.dinghuiye.core.annotation.convert.ConstValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by Strangeen on 2017/8/3.
 */
public class ConstValueConvertor implements Convertor {

    private static final Logger logger = LoggerFactory.getLogger(ConstValueConvertor.class);

    @Override
    public Object convert(Object obj, Field field, Map<String, String> excelRecordMap) {

        logger.trace(new StringBuffer()
                .append("field: ").append(field.getName())
                .append(", obj: ").append(obj)
                .append(" run ConstValueConvertor").toString());

        String constValue = field.getAnnotation(ConstValue.class).value();

        //return ConvertFactory.convertToType(field, obj);
        return constValue;
    }
}
