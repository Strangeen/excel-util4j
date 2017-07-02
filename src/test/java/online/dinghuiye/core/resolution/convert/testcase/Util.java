package online.dinghuiye.core.resolution.convert.testcase;

import java.util.List;

/**
 * Created by Strangeen on 2017/7/2.
 */
public class Util {

    // list集合比较
    public static  <T1,T2> boolean listEquals(List<T1> list1, List<T2> list2, ListValueConvertor convertor) {
        if (list1 == list2) return true;
        if (list1.size() != list2.size()) return false;
        boolean isEqual = true;
        for (int i = 0; i < list1.size(); i++) {
            T1 o1 = list1.get(i);
            T2 o2 = list2.get(i);
            if (o1 == o2) continue; // null
            if (o1 != null && o1.equals(o2)) continue;
            Object oc1 = convertor.change(o1);
            //Object oc2 = convertor.change(o2);
            if (oc1 == o2) continue; // null
            if (oc1 != null && oc1.equals(o2)) continue;
            isEqual = false;
            break;
        }
        return isEqual;
    }
}
