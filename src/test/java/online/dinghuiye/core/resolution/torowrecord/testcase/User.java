package online.dinghuiye.core.resolution.torowrecord.testcase;

import online.dinghuiye.core.annotation.excel.SheetTitleName;
import online.dinghuiye.core.annotation.excel.Transient;

/**
 * @author Strangeen
 * on 2017/08/09
 */
public class User {

    @Transient
    private Integer id;

    @SheetTitleName("账号")
    private String username;

    @SheetTitleName("密码")
    private String password;

    private UserExtraInfo info;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserExtraInfo getInfo() {
        return info;
    }

    public void setInfo(UserExtraInfo info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", info is null: " + (info == null) +
                '}';
    }
}
