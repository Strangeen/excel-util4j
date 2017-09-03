package online.dinghuiye.core.resolution.convert;

import online.dinghuiye.api.resolution.Convertor;
import online.dinghuiye.core.annotation.convert.DateFormat;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * {@link DateFormat}注解实现类，根据注解配置的时间格式转换为对应的Date对象
 *
 * @author Strangeen on 2017/6/27
 *
 * @author Strangeen on 2017/9/3
 * @version 2.1.0
 */
public class DateFormatConvertor implements Convertor {

    private static final Logger logger = LoggerFactory.getLogger(DateFormatConvertor.class);

    @Override
    public Object convert(Object obj, Field field, Map<String, Object> excelRecordMap) {

        logger.trace("field: " + field.getName() +
                        ", obj: " + obj +
                        " run DateFormatConvertor");

        if (obj == null) return null;
        if (obj instanceof Date) return obj;
        Date date;
        try {
            String dateFormat = field.getAnnotation(DateFormat.class).value();
            if (StringUtils.isBlank(dateFormat))
                throw new RuntimeException("field注解DateFormat值为空，filed：" + field.getName());

            logger.debug(dateFormat);

            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            date = sdf.parse(obj.toString());
            return date;

        } catch (Exception e) {
            throw new RuntimeException("field注解DateFormat转换错误，filed：" + field.getName(), e);
        }
    }
}
