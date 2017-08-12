package online.dinghuiye.core.resolution.convert.testcase;

import online.dinghuiye.api.resolution.convert.Convertor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Strangeen on 2017/7/2.
 */
public class ScoreConvertor implements Convertor {

    @Override
    public Object convert(Object obj, Field field, Map<String, Object> excelRecordMap) {
        if (obj instanceof String) {

            String str = obj.toString();
            List<Integer> scoreList = new ArrayList<>();
            for (String s : str.split(",")) {
                scoreList.add(Integer.valueOf(s));
            }
            return scoreList;

        } else {
            throw new RuntimeException("解析错误，field：" + field.getName());
        }
    }


}
