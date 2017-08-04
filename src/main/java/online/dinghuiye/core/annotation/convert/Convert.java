package online.dinghuiye.core.annotation.convert;

import online.dinghuiye.api.resolution.convert.Convertor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Strangeen on 2017/6/27.<br>
 * <br>
 * 标注为转换注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Convert {

    /**
     * @return 对应的转换实现类，默认无
     */
    Class<? extends Convertor> value();
}
