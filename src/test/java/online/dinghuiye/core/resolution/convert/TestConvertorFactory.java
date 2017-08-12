package online.dinghuiye.core.resolution.convert;

import online.dinghuiye.api.resolution.convert.Convertor;
import online.dinghuiye.core.resolution.convert.testcase.User;
import online.dinghuiye.core.resolution.convert.testcase.Util;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Strangeen on 2017/7/1.
 */
public class TestConvertorFactory {


    @Test
    public void testGetConvertors() throws NoSuchFieldException {

        Field field = User.class.getDeclaredField("sex");
        List<Convertor> convertorList = ConvertFactory.getConvertors(field);

        List<Class<?>> convertorClazzList = new ArrayList<>();
        convertorClazzList.add(ValueMapConvertor.class);
        convertorClazzList.add(BlankToNullConvertor.class);
        Assert.assertEquals(true, Util.listEquals(convertorList, convertorClazzList, o -> o.getClass()));
    }
}

