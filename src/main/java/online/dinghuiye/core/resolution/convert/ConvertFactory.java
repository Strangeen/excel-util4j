package online.dinghuiye.core.resolution.convert;

import online.dinghuiye.api.annotation.convert.Convert;
import online.dinghuiye.api.resolution.convert.Convertor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 根据{@link Convert#value()}匹配转换器实现类
 *
 * @author Strangeen on 2017/6/30.
 */
class ConvertFactory {

    private static final Logger logger = LoggerFactory.getLogger(ConvertFactory.class);
    private static Map<Class<? extends Convertor>, Convertor> convertorCache = new HashMap<>();

    /**
     * <p>按照field的注解顺序获取，得到顺序的{@link Convertor}（转换器），
     * 并返回{@link Convertor}的集合</p>
     * <p>注：值转换按照注解的顺序进行</p>
     *
     * @param field 属性字段
     * @return {@link Convertor}集合
     */
    static List<Convertor> getConvertors(Field field) {

        try {
            List<Convertor> convertorList = new ArrayList<>();
            Annotation[] annos = field.getAnnotations();
            for (Annotation anno : annos) {
                Convert convertAnno = anno.annotationType().getAnnotation(Convert.class);
                if (convertAnno == null) continue;

                Class<? extends Convertor> convertorClazz = convertAnno.value();
                // convertor通过缓存实现单例模式
                Convertor convertor = convertorCache.get(convertorClazz);
                if (convertor == null) {
                    convertorCache.put(convertorClazz, convertorClazz.newInstance());
                    convertor = convertorCache.get(convertorClazz);
                }
                convertorList.add(convertor);
            }

            return convertorList;

        } catch (Exception e) {
            logger.error("获取转换器失败", e);
            throw new RuntimeException(e);
        }
    }
}
