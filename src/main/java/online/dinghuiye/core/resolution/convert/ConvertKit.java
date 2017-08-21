package online.dinghuiye.core.resolution.convert;

import online.dinghuiye.api.resolution.convert.Convertor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * 转换器工具包
 *
 * @author Strangeen
 * on 2017/8/3
 */
public class ConvertKit {

    private static final Logger logger = LoggerFactory.getLogger(ConvertKit.class);

    public static Object convert(Object excelValue, Field field, Map<String, Object> excelRecordMap) {
        List<Convertor> convertorList = ConvertorFactory.getConvertors(field);
        Object value = excelValue;
        // 转换器转换
        for (Convertor convertor : convertorList) {
            value = convertor.convert(value, field, excelRecordMap);
        }
        // 转换为对应属性类型
        value = convertToType(field, value);

        return value;
    }

    /**
     * <p>基本类型和String转换器，在{@link Convertor}执行之后执行，
     * 类型不同时，均将obj转为String进行转换</p>
     */
    static Object convertToType(Field field, Object obj) {

        logger.trace("field: " + field.getName() +
                        ", obj: " + obj +
                        " run ConvertorFactory.convertToType");

        Class<?> fieldTypeClass = field.getType();

        if (obj == null) return null;

        Class<?> objClazz = obj.getClass();
        if (fieldTypeClass.isAssignableFrom(objClazz))
            return obj; // 如果类型是要求类型或子类型，则直接返回，泛型只在编译层有效，因此无法比对泛型

        if (fieldTypeClass.isArray()) {

            if (!objClazz.isArray())
                throw new RuntimeException("转换对象不是" + fieldTypeClass.getName());
            Class<?> componentClazz = fieldTypeClass.getComponentType();
            if (objClazz.getComponentType() != componentClazz)
                throw new RuntimeException("转换对象元素不是" + componentClazz.getName());
            return obj;

        } else {

            if (fieldTypeClass == Integer.class || fieldTypeClass == int.class) {
                return Integer.valueOf(obj.toString());
            }
            else if (fieldTypeClass == Byte.class || fieldTypeClass == byte.class) {
                return Byte.valueOf(obj.toString());
            }
            else if (fieldTypeClass == Character.class || fieldTypeClass == char.class) {
                return (char) Integer.valueOf(obj.toString()).intValue();
            }
            else if (fieldTypeClass == Short.class || fieldTypeClass == short.class) {
                return Short.valueOf(obj.toString());
            }
            else if (fieldTypeClass == Long.class || fieldTypeClass == long.class) {
                return Long.valueOf(obj.toString());
            }
            else if (fieldTypeClass == Float.class || fieldTypeClass == float.class) {
                return Float.valueOf(obj.toString());
            }
            else if (fieldTypeClass == Double.class || fieldTypeClass == double.class) {
                return Double.valueOf(obj.toString());
            }
            else if (fieldTypeClass == Boolean.class || fieldTypeClass == boolean.class) {
                return Boolean.valueOf(obj.toString());
            }
            else if (fieldTypeClass == String.class) {
                return String.valueOf(obj);
            }
            else {
                throw new RuntimeException("转换对象不是" + fieldTypeClass.getName() + "或不支持的转换对象类型" + fieldTypeClass.getName());
            }
            /*else if (Collection.class.isAssignableFrom(fieldTypeClass)) { // 集合
                Class<?> objClazz = obj.getClass();
                if (fieldTypeClass.isAssignableFrom(objClazz))
                    throw new RuntimeException("转换对象不是" + fieldTypeClass.getName());
                return obj;

            } else if (Map.class.isAssignableFrom(fieldTypeClass)) { // Map
                Class<?> objClazz = obj.getClass();
                if (fieldTypeClass.isAssignableFrom(objClazz))
                    throw new RuntimeException("转换对象不是" + fieldTypeClass.getName());
                return obj;

            } else */

        }
    }

}
