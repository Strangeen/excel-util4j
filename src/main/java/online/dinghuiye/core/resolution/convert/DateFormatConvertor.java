package online.dinghuiye.core.resolution.convert;

import online.dinghuiye.api.resolution.convert.Convertor;
import online.dinghuiye.core.annotation.convert.DateFormat;
import online.dinghuiye.core.entity.RowRecord;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by Strangeen on 2017/6/27.
 *
 * 根据注解配置的时间格式转换为对应的
 */
public class DateFormatConvertor implements Convertor {

    private static final Logger logger = LoggerFactory.getLogger(DateFormatConvertor.class);

    @Override
    public Object convert(Object obj, Field field, Map<String, String> excelRecordMap) {
        if (obj == null) return obj;
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
