package online.dinghuiye.core.common;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Strangeen on 2017/08/16
 */
public class AnnotationFactory {

    static Map<Field, Annotation[]> annoCache = new HashMap<>();;

    /**
     * 通过Field获取Annotation[]
     *
     * @param field <p>需要获取注解列表的属性</p>
     *              <p>建议field通过FieldFactory获取</p>
     * @return
     */
    public static Annotation[] getAnnotations(Field field) {

        Annotation[] annos = annoCache.get(field);
        if (annos == null) {
            annos = field.getAnnotations();
            annoCache.put(field, annos);
        }

        return annos;
    }

}
