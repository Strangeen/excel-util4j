package online.dinghuiye.core.common;

import online.dinghuiye.core.common.testcase.A;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Strangeen on 2017/08/16
 */
public class TestAnnotationFactory {

    @Test
    public void testGetAnnotations() {

        A a1 = new A();
        A a2 = new A();
        List<A> aList = new ArrayList<>();
        aList.add(a1);
        aList.add(a2);

        // 通过fieldFacotry获取的field缓存的annos
        aList.forEach(a -> {
            Field[] fields = FieldFactory.getFields(a.getClass());
            Arrays.asList(fields).forEach(field -> {
                Annotation[] annos = AnnotationFactory.getAnnotations(field);
                Arrays.asList(annos).forEach(System.out::println);
            });
        });

        Assert.assertEquals(1, FieldFactory.fieldCache.size());
        Assert.assertEquals(2, AnnotationFactory.annoCache.size());

        FieldFactory.fieldCache.clear();
        AnnotationFactory.annoCache.clear();

        // 通过对象获取class获取的field缓存的annos
        aList.forEach(a -> {
            Field[] fields = a.getClass().getDeclaredFields();
            Arrays.asList(fields).forEach(field -> {
                Annotation[] annos = AnnotationFactory.getAnnotations(field);
                Arrays.asList(annos).forEach(System.out::println);
            });
        });

        Assert.assertEquals(0, FieldFactory.fieldCache.size());
        Assert.assertEquals(2, AnnotationFactory.annoCache.size());
    }
}
