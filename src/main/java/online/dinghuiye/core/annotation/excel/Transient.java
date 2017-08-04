package online.dinghuiye.core.annotation.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Strangeen on 2017/6/27.
 *
 * 按POJO属性解析RowRecord原始数据时，忽略标注的属性
 * 即属性作为其他注释或者其他不作为pojo导入数据的用途时使用
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Transient {
}
