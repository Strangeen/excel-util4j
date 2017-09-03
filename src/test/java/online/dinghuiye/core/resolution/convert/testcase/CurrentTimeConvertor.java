package online.dinghuiye.core.resolution.convert.testcase;

import online.dinghuiye.api.resolution.Convertor;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;

/**
 * Created by Strangeen on 2017/8/4
 *
 * @author Strangeen on 2017/9/3
 * @version 2.1.0
 */
public class CurrentTimeConvertor implements Convertor {

    @Override
    public Object convert(Object obj, Field field, Map<String, Object> excelRecordMap) {

        return new Date();
    }
}
