package online.dinghuiye.core.resolution.convert.testcase;

import online.dinghuiye.core.annotation.convert.*;
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
    private Date birthday;

    @ValueConvert(value = ScoreConvertor.class)
    List<Integer> scoreList;

    @ValueConvert(CurrentTimeConvertor.class)
    private Date createTime;

    @ConstValue(value = "2017-12-12")
    @DateFormat(value = "yyyy-MM-dd")
    private Date modifyTime;

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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public List<Integer> getScoreList() {
        return scoreList;
    }

    public void setScoreList(List<Integer> scoreList) {
        this.scoreList = scoreList;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
