package online.dinghuiye.core.resolution.torowrecord.testcase;

import online.dinghuiye.core.annotation.convert.ConstValue;
import online.dinghuiye.core.annotation.convert.DateFormat;
import online.dinghuiye.core.annotation.convert.ValueMap;
import online.dinghuiye.core.annotation.excel.SheetTitleName;

import java.util.Date;

/**
 * Created by Strangeen on 2017/8/3.
 */
public class Student {

    @SheetTitleName("姓名")
    private String name;

    @SheetTitleName("年龄")
    private Integer age;

    @SheetTitleName("生日")
    @DateFormat("yyyy-MM-dd")
    private Date birthday;

    @SheetTitleName("性别")
    @ValueMap("{'男':1,'女':0}")
    private Integer sex;

    @ConstValue("1")
    private Integer enable;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", birthday=" + birthday +
                ", sex=" + sex +
                ", enable=" + enable +
                '}';
    }
}
