package online.dinghuiye.core.validation;

import online.dinghuiye.core.validation.testcase.User;
import online.dinghuiye.core.validation.testcase.UserExtraInfo;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Strangeen on 2017/08/16
 */
public class TestValidateKit {

    @Test
    public void testValidateError() {

        ResetTestValue.reset();

        User user = new User();
        user.setUsername("123456");

        UserExtraInfo info = new UserExtraInfo();
        info.setCertCard("666666");
        info.setUser(user);

        user.setInfo(info);

        Map<Field, String> res = ValidateKit.validate(user);

        System.out.println(res);

        Map<String, String> expect = new HashMap<>();
        expect.put("username", "用户名已被注册");
        expect.put("certCard", "身份证重复");
        res.forEach((key, value) -> Assert.assertEquals(expect.get(key.getName()), value));

    }

    @Test
    public void testValidateSuccess() {

        ResetTestValue.reset();

        User user = new User();
        user.setUsername("223456");

        UserExtraInfo info = new UserExtraInfo();
        info.setCertCard("766666");
        info.setUser(user);

        user.setInfo(info);

        Map<Field, String> res = ValidateKit.validate(user);

        System.out.println(res);
        Assert.assertEquals(0, res.size());
    }

}
