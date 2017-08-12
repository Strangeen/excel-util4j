package online.dinghuiye.core.resolution.torowrecord.testcase;

import com.alibaba.fastjson.annotation.JSONField;
import online.dinghuiye.core.annotation.convert.DateFormat;
import online.dinghuiye.core.annotation.convert.ValueMap;
import online.dinghuiye.core.annotation.excel.SheetTitleName;
import online.dinghuiye.core.annotation.excel.Transient;

import java.util.Date;

/**
 * @author Strangeen
 * on 2017/08/09
 */
public class UserExtraInfo {

    @Transient
    private Integer id;

    private User user;

    @SheetTitleName("姓名")
    private String name;

    @SheetTitleName("性别")
    @ValueMap("{'男':1,'女':0}")
    private Integer sex;

    @SheetTitleName("生日")
    @DateFormat("yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private Date birth;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    @Override
    public String toString() {
        return "UserExtraInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", birth=" + birth +
                ", user is null: " + (user == null) +
                '}';
    }
}
