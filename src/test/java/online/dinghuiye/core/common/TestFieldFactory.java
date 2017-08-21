package online.dinghuiye.core.common;

import online.dinghuiye.core.common.testcase.A;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Strangeen on 2017/08/16
 */
public class TestFieldFactory {

    @Test
    public void testGetFileds() {

        Field[] fields1 = FieldFactory.getFields(new A().getClass());
        Field[] fields2 = FieldFactory.getFields(new A().getClass());

        Assert.assertEquals(1, FieldFactory.fieldCache.size());
        Assert.assertTrue(fields1 == fields2);
        Assert.assertEquals("a", fields1[0].getName());
        Assert.assertEquals("b", fields1[1].getName());
    }
}
