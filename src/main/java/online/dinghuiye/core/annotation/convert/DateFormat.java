package online.dinghuiye.core.annotation.convert;

import online.dinghuiye.api.annotation.convert.Convert;
import online.dinghuiye.core.resolution.convert.DateFormatConvertor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * java.util.Date类型转换
 *  当excel传入Date属性值类型为String时，需要使用注解转换
 *
 * @author Strangeen
 * on 2017/6/27
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Convert(DateFormatConvertor.class)
public @interface DateFormat {

    /**
     * @return SimpleDateFormat格式形式的字符串
     */
    String value();
}
