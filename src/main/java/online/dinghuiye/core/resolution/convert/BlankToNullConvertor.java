package online.dinghuiye.core.resolution.convert;

import online.dinghuiye.api.resolution.convert.Convertor;
import online.dinghuiye.core.annotation.convert.BlankToNull;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * {@link BlankToNull}注解实现类
 *
 * @author Strangeen
 * on 2017/7/1
 */
public class BlankToNullConvertor implements Convertor {

    private static final Logger logger = LoggerFactory.getLogger(BlankToNullConvertor.class);

    @Override
    public Object convert(Object obj, Field field, Map<String, Object> excelRecordMap) {

        logger.trace("field: " + field.getName() +
                        ", obj: " + obj +
                        " run BlankToNullConvertor");

        if (obj == null) return null;
        if (StringUtils.isBlank(obj.toString())) return null;
        return obj;
    }
}
