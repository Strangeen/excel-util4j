package online.dinghuiye.core.resolution.convert.testcase;

import online.dinghuiye.api.resolution.convert.Convertor;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;

/**
 * Created by Strangeen on 2017/8/4.
 */
public class CurrentTimeConvertor implements Convertor {

    @Override
    public Object convert(Object obj, Field field, Map<String, Object> excelRecordMap) {

        return new Date();
    }
}
