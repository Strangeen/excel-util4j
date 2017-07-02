package online.dinghuiye.core.resolution.convert;

import online.dinghuiye.api.resolution.convert.Convertor;
import online.dinghuiye.core.annotation.convert.BlankToNull;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by Strangeen on 2017/7/1.
 *
 * 空串或null转换为null
 */
public class BlankToNullConvertor implements Convertor {

    /*private static BlankToNullConvertor btnc;

    public BlankToNullConvertor getInstance() {
        if (btnc == null) btnc = new BlankToNullConvertor();
        return btnc;
    }*/

    @Override
    public Object convert(Object obj, Field field, Map<String, String> excelRecordMap) {
        if (obj == null) return obj;
        if (StringUtils.isBlank(obj.toString())) return null;
        return ConvertFactory.convertToType(field, obj);
    }
}
