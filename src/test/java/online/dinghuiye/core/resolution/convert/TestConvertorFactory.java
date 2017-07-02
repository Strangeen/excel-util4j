package online.dinghuiye.core.resolution.convert;

import online.dinghuiye.api.resolution.convert.Convertor;
import online.dinghuiye.core.consts.Consts;
import online.dinghuiye.core.resolution.convert.testcase.*;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by Strangeen on 2017/7/1.
 */
public class TestConvertorFactory {


    @Test
    public void testGetConvertors() throws NoSuchFieldException {

        Field field = User.class.getDeclaredField("sex");
        List<Convertor> convertorList = ConvertFactory.getConvertors(field);

        List<String> convertorClazzNameList = new ArrayList<>();
        convertorClazzNameList.add(Consts.CONVERTOR_PACKAGE + "ValueMapConvertor");
        convertorClazzNameList.add(Consts.CONVERTOR_PACKAGE + "BlankToNullConvertor");
        Assert.assertEquals(true, Util.listEquals(convertorList, convertorClazzNameList,
                new ListValueConvertor() {
                    @Override
                    public Object change(Object obj) {
                        return ((Convertor) obj).getClass().getName();
                    }
                })
        );
    }

    @Test
    public void testConvertToType() throws NoSuchFieldException, IllegalAccessException, InstantiationException {

        // 基本类型
        Assert.assertEquals(Byte.class, ConvertFactory.convertToType(TestClazz0.class.getDeclaredField("b"), "1").getClass());
        Assert.assertEquals(Character.class, ConvertFactory.convertToType(TestClazz0.class.getDeclaredField("c"), "1").getClass());
        Assert.assertEquals(Short.class, ConvertFactory.convertToType(TestClazz0.class.getDeclaredField("s"), "1").getClass());
        Assert.assertEquals(Integer.class, ConvertFactory.convertToType(TestClazz0.class.getDeclaredField("i"), "1").getClass());
        Assert.assertEquals(Long.class, ConvertFactory.convertToType(TestClazz0.class.getDeclaredField("l"), "1").getClass());
        Assert.assertEquals(Float.class, ConvertFactory.convertToType(TestClazz0.class.getDeclaredField("f"), "1").getClass());
        Assert.assertEquals(Double.class, ConvertFactory.convertToType(TestClazz0.class.getDeclaredField("d"), "1").getClass());
        Assert.assertEquals(Boolean.class, ConvertFactory.convertToType(TestClazz0.class.getDeclaredField("bl"), "true").getClass());

        // 包装类型
        Field[] fields = TestClazz1.class.getDeclaredFields();
        for (Field field : fields) {
            Assert.assertEquals(field.getType(), ConvertFactory.convertToType(field, "1").getClass());
        }

        // 集合，Map类型，自定义类型
        Assert.assertEquals(int[].class, ConvertFactory.convertToType(TestClazz2.class.getDeclaredField("is"), new int[0]).getClass());
        Assert.assertEquals(Integer[].class, ConvertFactory.convertToType(TestClazz2.class.getDeclaredField("iObjs"), new Integer[0]).getClass());
        Assert.assertEquals(User[].class, ConvertFactory.convertToType(TestClazz2.class.getDeclaredField("users"), new User[0]).getClass());

        // 无法检测集合泛型，实际为List<Integer>
        Assert.assertEquals(true, List.class.isAssignableFrom(
                ConvertFactory.convertToType(TestClazz2.class.getDeclaredField("iList"),new ArrayList<Integer>()).getClass()));
        Assert.assertEquals(true, List.class.isAssignableFrom(
                ConvertFactory.convertToType(TestClazz2.class.getDeclaredField("iList"), new ArrayList<Long>()).getClass()));
        // 无法检测集合泛型，实际为Set<Integer>
        Assert.assertEquals(true, Set.class.isAssignableFrom(
                ConvertFactory.convertToType(TestClazz2.class.getDeclaredField("iSet"), new HashSet<Integer>()).getClass()));
        Assert.assertEquals(true, Set.class.isAssignableFrom(
                ConvertFactory.convertToType(TestClazz2.class.getDeclaredField("iSet"), new HashSet<Long>()).getClass()));
        // 无法检测集合泛型
        Assert.assertEquals(true, Map.class.isAssignableFrom(
                ConvertFactory.convertToType(TestClazz2.class.getDeclaredField("map"), new HashMap<Integer, String>()).getClass()));
        Assert.assertEquals(true, User.class.isAssignableFrom(
                ConvertFactory.convertToType(TestClazz2.class.getDeclaredField("user"), new User()).getClass()));
        // 无法检测集合泛型
        Assert.assertEquals(true, List.class.isAssignableFrom(
                ConvertFactory.convertToType(TestClazz2.class.getDeclaredField("userList"), new ArrayList<Integer>()).getClass()));

    }
}

