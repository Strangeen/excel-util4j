package online.dinghuiye.core.annotation.convert;

import java.lang.annotation.*;

/**
 * Created by Strangeen on 2017/6/27.<br>
 * <br>
 * 标注为转换注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Convert {
    /**
     * @return 对应的转换实现类类全名，默认无
     */
    String value() default "";
}
