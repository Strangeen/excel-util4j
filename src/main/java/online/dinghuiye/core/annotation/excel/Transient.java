package online.dinghuiye.core.annotation.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>解析excel原始数据时，忽略属性，
 * 即属性为其他不作为pojo导入数据的用途</p>
 *
 * @author Strangeen
 * on 2017/6/27
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Transient {
}
