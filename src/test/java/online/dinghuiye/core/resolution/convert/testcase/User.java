package online.dinghuiye.core.resolution.convert.testcase;

import online.dinghuiye.core.annotation.convert.BlankToNull;
import online.dinghuiye.core.annotation.convert.DateFormat;
import online.dinghuiye.core.annotation.convert.ValueConvert;
import online.dinghuiye.core.annotation.convert.ValueMap;
import online.dinghuiye.core.resolution.convert.BlankToNullConvertor;

import java.util.Date;
import java.util.List;

/**
 * Created by Strangeen on 2017/7/2.
 */ // 测试类
public class User {

    @ValueConvert(value = BlankToNullConvertor.class)
    @BlankToNull
    private String name;
    @ValueMap(value = "{1:'male',0:'female'}")
    @BlankToNull
    private String sex;
    @DateFormat(value = "yyyy-MM-dd HH:mm:ss")
//    @DateFormat(value = "")
    private Date birthday;
    @ValueConvert(value = ScoreConvertor.class)
    List<Integer> scoreList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
