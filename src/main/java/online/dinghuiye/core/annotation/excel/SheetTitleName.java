package online.dinghuiye.core.annotation.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Strangeen on 2017/6/27.
 *
 * POJO属性对应的excel表头名称，不填写value表示表头名称为属性名
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SheetTitleName {
    String value() default "";
}
