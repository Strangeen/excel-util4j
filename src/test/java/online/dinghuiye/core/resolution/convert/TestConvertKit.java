package online.dinghuiye.core.resolution.convert;

import online.dinghuiye.api.resolution.convert.Convertor;
import online.dinghuiye.core.resolution.convert.testcase.User;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Strangeen on 2017/8/4.
 */
public class TestConvertKit {

    @Test
    // @ConstValue
    // @DateFormat
    // 复合convertor
    public void testConvert() throws NoSuchFieldException, ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Field field = User.class.getDeclaredField("modifyTime");
        Assert.assertEquals(sdf.parse("2017-12-12"), ConvertKit.convert(null, field, null));
    }

    @Test
    // @CurrentTimeConvertor
    // @DateFormat
    // 复合convertor
    public void testConvert2() throws NoSuchFieldException, ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Field field = User.class.getDeclaredField("createTime");
        Assert.assertEquals(sdf.format(new Date()), sdf.format(ConvertKit.convert(null, field, null)));
    }
}
