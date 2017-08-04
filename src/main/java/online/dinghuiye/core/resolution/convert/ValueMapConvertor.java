package online.dinghuiye.core.resolution.convert;

import com.alibaba.fastjson.JSONObject;
import online.dinghuiye.api.resolution.convert.Convertor;
import online.dinghuiye.core.annotation.convert.ValueMap;
import online.dinghuiye.core.entity.RowRecord;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by Strangeen on 2017/6/27.
 */
public class ValueMapConvertor implements Convertor {

    private static final Logger logger = LoggerFactory.getLogger(ValueMapConvertor.class);

    @Override
    public Object convert(Object obj, Field field, Map<String, String> excelRecordMap) {

        logger.trace(new StringBuffer()
                .append("field: ").append(field.getName())
                .append(", obj: ").append(obj)
                .append(" run ValueMapConvertor").toString());

        String jsonStr = field.getAnnotation(ValueMap.class).value();
        if (StringUtils.isBlank(jsonStr))
            throw new RuntimeException("field注解ValueMap值为空，filed：" + field.getName());

        logger.debug(jsonStr);

        JSONObject json;
        try {
            json = JSONObject.parseObject(jsonStr);
        } catch (Exception e) {
            throw new RuntimeException("field注解ValueMap值不是有效json，filed：" + field.getName(), e);
        }

        if (obj != null) {
            obj = json.get(obj.toString());
        }
        //return ConvertFactory.convertToType(field, obj);
        return obj;
    }
}
