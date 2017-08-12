package online.dinghuiye.core.annotation.convert;

import online.dinghuiye.api.annotation.convert.Convert;
import online.dinghuiye.core.resolution.convert.ConstValueConvertor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 常量值转换
 *  比如：用户的启用和停用，导入数据时并没有传入值，需要手动设置常量值
 *
 * @author Strangeen
 * on 2017/8/3
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Convert(ConstValueConvertor.class)
public @interface ConstValue {

    /**
     * @return 常量值
     */
    String value();
}
