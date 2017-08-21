package online.dinghuiye.core.validation;

import online.dinghuiye.api.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import online.dinghuiye.api.annotation.validate.Validate;


/**
 * 根据{@link Validate#validator()}获取{@link Validator}，拥有对象缓存器，避免重复常见新的对象
 *
 * @author Strangeen on 2017/08/16
 */
public class ValidatorFactory {

    private static final Logger logger = LoggerFactory.getLogger(ValidatorFactory.class);
    private static Map<Class<? extends Validator>, Validator> validatorCache = new HashMap<>();

    /**
     * 根据Class&lt;? extends Validator&gt;获取{@link Validator}
     *
     * @param clazz {@link Validator}的Class
     * @return {@link Validator}对象
     */
    public static Validator getValidator(Class<? extends Validator> clazz) {

        try {
            Validator validator = validatorCache.get(clazz);
            if (validator == null) {
                validator = clazz.newInstance();
                validatorCache.put(clazz, validator);
            }

            return validator;

        } catch (Exception e) {
            logger.warn("获取验证器失败", e);
            throw new RuntimeException(e);
        }
    }
}
