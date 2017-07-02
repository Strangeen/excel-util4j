package online.dinghuiye.core.resolution.convert;

import online.dinghuiye.api.resolution.convert.Convertor;
import online.dinghuiye.core.annotation.convert.Convert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by Strangeen on 2017/6/30.
 */
public class ConvertFactory {

    private static final Logger logger = LoggerFactory.getLogger(ConvertFactory.class);

    /**
     * 按照field的注解顺序获取，得到顺序的Convertor（转换器），并返回Convertor的集合
     * 注：值转换按照注解的顺序进行
     * @param field
     * @return Convertor集合
     */
    public static List<Convertor> getConvertors(Field field) {

        try {
            List<Convertor> convertorList = new ArrayList<>();
            Annotation[] annos = field.getAnnotations();
            for (Annotation anno : annos) {
                Convert convertAnno = anno.annotationType().getAnnotation(Convert.class);
                if (convertAnno == null) {
                    continue;
                }

                Class<? extends Convertor> convertorClazz =
                        (Class<? extends Convertor>)
                                Class.forName(convertAnno.value());
                convertorList.add(convertorClazz.newInstance()); // TODO 如何要求实现类使用单例模式
            }

            return convertorList;

        } catch (Exception e) {
            logger.error("获取转换器失败", e);
            throw new RuntimeException(e);
        }
    }


    // 类型不同，均将obj转为String进行转换
    public static <T> Object convertToType(Field field, Object obj) {

        Class<?> fieldTypeClass = field.getType();

        if (obj == null) return obj;

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
