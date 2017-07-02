package online.dinghuiye.core.annotation.convert;

import online.dinghuiye.core.consts.Consts;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Strangeen on 2017/6/27.
 *
 * POJO属性为java.util.Date类型的转换注解标注
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Convert(value = Consts.CONVERTOR_PACKAGE + "DateFormatConvertor")
public @interface DateFormat {

    /**
     * @return SimpleDateFormat格式形式的字符串
     */
    String value();
}
