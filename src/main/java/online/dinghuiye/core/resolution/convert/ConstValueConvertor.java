package online.dinghuiye.core.resolution.convert;

import online.dinghuiye.api.resolution.Convertor;
import online.dinghuiye.core.annotation.convert.ConstValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * {@link ConstValueConvertor}注解实现类
 *
 * @author Strangeen on 2017/8/3
 *
 * @author Strangeen on 2017/9/3
 * @version 2.1.0
 */
public class ConstValueConvertor implements Convertor {

    private static final Logger logger = LoggerFactory.getLogger(ConstValueConvertor.class);

    @Override
    public Object convert(Object obj, Field field, Map<String, Object> excelRecordMap) {

        logger.trace("field: " + field.getName() +
                        ", obj: " + obj +
                        " run ConstValueConvertor");

        return field.getAnnotation(ConstValue.class).value();
    }
}
