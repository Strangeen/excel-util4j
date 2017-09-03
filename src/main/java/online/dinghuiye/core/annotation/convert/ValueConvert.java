package online.dinghuiye.core.annotation.convert;

import online.dinghuiye.api.annotation.convert.Convert;
import online.dinghuiye.api.resolution.Convertor;
import online.dinghuiye.core.resolution.convert.ValueConvertConvertor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义转换
 *
 * @author Strangeen on 2017/6/27
 *
 * @author Strangeen on 2017/9/3
 * @version 2.1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Convert(ValueConvertConvertor.class)
public @interface ValueConvert {

    /**
     * 按照数组顺序执行Convertor
     *
     * @return Convertor数组
     */
    Class<? extends Convertor>[] value();
}
