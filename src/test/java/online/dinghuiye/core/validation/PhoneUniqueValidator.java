package online.dinghuiye.core.validation;

import online.dinghuiye.api.validation.Validator;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Strangeen on 2017/08/16
 */
public class PhoneUniqueValidator implements Validator {

    private static Set<Object> phoneCache;

    /*
     创建时写入数据库已有数据缓存，这里模拟从数据库里读出来的数据
     值得注意的是：
        1. 并发操作数据库时，需要同步缓存，并更新缓存
        2. 成功添加一条数据后，需要更新缓存
      */
    public PhoneUniqueValidator() {

        // 模拟数据库查询的数据
        resetCache();
    }

    public static void resetCache() {
        phoneCache = new HashSet<>();
        phoneCache.add("12345678901");
        phoneCache.add("12345678902");
        phoneCache.add("12345678903");
        phoneCache.add("123456789014");
    }


    @Override
    public <SchoolMan> boolean validate(Object fieldValue, Field field, SchoolMan obj) {

        if (phoneCache.contains(fieldValue)) return false;
        phoneCache.add(fieldValue);
        return true;
    }
}
