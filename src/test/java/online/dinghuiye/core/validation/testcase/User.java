package online.dinghuiye.core.validation.testcase;

import online.dinghuiye.api.annotation.validate.Validate;
import online.dinghuiye.core.annotation.excel.SheetTitleName;
import online.dinghuiye.core.annotation.excel.Transient;
import online.dinghuiye.core.validation.UsernameUniqueValidator;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Strangeen
 * on 2017/08/09
 */
@Entity
@DynamicInsert(true)
@Table(name = "user2")
public class User {

    @Transient
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @SheetTitleName("账号")
    @NotNull
    @Size(min = 6, max = 20, message = "请填写{min}~{max}个字")
    @Validate(validator = UsernameUniqueValidator.class, message = "用户名已被注册")
    private String username;

    @SheetTitleName("密码")
    @NotNull(message = "不能为空")
    @Size(min = 6, max = 20, message = "请填写{min}~{max}个字")
    private String password;

    @Valid
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserExtraInfo info;

    public User() {
    }

    public User(Integer id, String username, String password, UserExtraInfo info) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.info = info;
    }

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
