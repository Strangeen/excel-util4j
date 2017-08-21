package online.dinghuiye.core.validation;

import online.dinghuiye.api.validation.Validator;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Strangeen on 2017/08/16
 */
public class CertCardUniqueValidator implements Validator {

    protected static Set<Object> certCardCache;

    /*
     创建时写入数据库已有数据缓存，这里模拟从数据库里读出来的数据
     值得注意的是：
        1. 并发操作数据库时，需要同步缓存，并更新缓存
        2. 成功添加一条数据后，需要更新缓存
      */
    public CertCardUniqueValidator() {

        // 模拟数据库查询的数据
        resetCache();
    }

    public static void resetCache() {
        certCardCache = new HashSet<>();
        certCardCache.add("666666");
        certCardCache.add("777777");
        certCardCache.add("888888");
        certCardCache.add("999999");
    }


    @Override
    public <User> boolean validate(Object fieldValue, Field field, User obj) {

        if (certCardCache.contains(fieldValue)) return false;
        certCardCache.add(fieldValue);
        return true;
    }
}
