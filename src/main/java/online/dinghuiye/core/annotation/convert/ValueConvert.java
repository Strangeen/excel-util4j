package online.dinghuiye.core.annotation.convert;

import online.dinghuiye.api.resolution.convert.Convertor;
import online.dinghuiye.core.resolution.convert.ValueConvertConvertor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Strangeen on 2017/6/27.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Convert(ValueConvertConvertor.class)
public @interface ValueConvert {

    /**
     * 按照数组顺序执行Convertor
     * @return Convertor数组
     */
    Class<? extends Convertor>[] value();
}
