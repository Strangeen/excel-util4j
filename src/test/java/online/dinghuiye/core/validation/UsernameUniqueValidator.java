package online.dinghuiye.core.validation;

import online.dinghuiye.api.validation.Validator;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 测试判重Validator
 *
 * @author Strangeen on 2017/08/16
 */
public class UsernameUniqueValidator implements Validator {

    private static Set<Object> usernameCache;

    /*
     创建时写入数据库已有数据缓存，这里模拟从数据库里读出来的数据
     值得注意的是：
        1. 并发操作数据库时，需要同步缓存，并更新缓存
        2. 成功添加一条数据后，需要更新缓存
      */
    public UsernameUniqueValidator() {

        // 模拟数据库查询的数据
        resetCache();
    }

    public static void resetCache() {
        usernameCache = new HashSet<>();
        usernameCache.add("123456");
        usernameCache.add("123457");
        usernameCache.add("123458");
        usernameCache.add("123459");
    }

    @Override
    public <User> boolean validate(Object fieldValue, Field field, User obj) {

        if (usernameCache.contains(fieldValue)) return false;
        usernameCache.add(fieldValue);
        return true;
    }
}
