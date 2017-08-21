package online.dinghuiye.core.common;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Field工厂
 *
 * @author Strangeen on 2017/08/14
 */
public class FieldFactory {

    static Map<Class<?>, Field[]> fieldCache = new HashMap<>();

    /**
     * 通过Class获取Field[]
     *
     * @param clazz
     * @return
     */
    public static Field[] getFields(Class<?> clazz) {

        Field[] fields = fieldCache.get(clazz);
        if (fields == null) {
            fields = clazz.getDeclaredFields();
            fieldCache.put(clazz, fields);
        }

        return fields;
    }
}
