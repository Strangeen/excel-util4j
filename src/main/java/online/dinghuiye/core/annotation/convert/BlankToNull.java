package online.dinghuiye.core.annotation.convert;

import online.dinghuiye.core.resolution.convert.BlankToNullConvertor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Strangeen on 2017/7/1.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Convert(BlankToNullConvertor.class)
public @interface BlankToNull {
}
