package online.dinghuiye.core.validation;

import online.dinghuiye.api.annotation.validate.Validate;
import online.dinghuiye.api.validation.Validator;
import online.dinghuiye.core.common.FieldFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 验证工具包
 *
 * @author Strangeen on 2017/08/09
 */
class ValidateKit {

    private static Logger logger = LoggerFactory.getLogger(ValidateKit.class);

    /**
     * 通过根类和hibernate validator错误信息获取当前验证失败的field对象
     *
     * @param rootClass pojo根类， 如：User.class类包含属性info：Info.class，User即rootClass
     * @param cv        hibernate validator验证结果对象{@link ConstraintViolation}
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


    /**
     * 复杂验证
     */
    static <T> Map<Field, String> validate(T obj) {

        Map<Field, String> resultMap = new HashMap<>();
        for (Field field : FieldFactory.getFields(obj.getClass())) {
            try {
                Valid validAnno = field.getAnnotation(Valid.class);
                Validate validateAnno = field.getAnnotation(Validate.class);
                field.setAccessible(true);
                Object fieldValue = field.get(obj);
                if (fieldValue == null) continue;

                // TODO @Valid和@Validate需要互斥，后期版本更改为预先扫描Class检测和缓存field和annotation
                if (validateAnno != null) { // 属性为java类型
                    Validator validator = ValidatorFactory.getValidator(validateAnno.validator());
                    if (validator != null) {
                        if (!validator.validate(fieldValue, field, obj))
                            resultMap.put(field, validateAnno.message());
                    }
                }
                else if (validAnno != null) { // 属性为自定义对象引用
                    resultMap.putAll(validate(fieldValue));
                }

            } catch (Exception e) {
                logger.warn("field： " + field.getName() + " 验证失败", e);
                resultMap.put(field, "验证错误");
            }
        }
        return resultMap;
    }
}
