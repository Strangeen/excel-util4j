package online.dinghuiye.core.validation;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import java.lang.reflect.Field;

/**
 * 验证工具包
 *
 * @author Strangeen
 * on 2017/08/09
 */
class ValidateKit {

    /**
     * 通过根类和hibernate validator错误信息获取当前验证失败的field对象
     *
     * @param rootClass pojo根类，
     *                  如：User.class类包含属性info：Info.class，User即rootClass
     * @param cv hibernate validator验证结果对象{@link ConstraintViolation}
     * @return 当前失败的field对象
     */
    static Field getConstraintViolationField(Class<?> rootClass, ConstraintViolation<Object> cv) {

        try {
            Field field = null;
            for (Path.Node node : cv.getPropertyPath()) {
                String fieldName = node.getName();
                field = rootClass.getDeclaredField(fieldName);
                rootClass = field.getType();
            }
            return field;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
