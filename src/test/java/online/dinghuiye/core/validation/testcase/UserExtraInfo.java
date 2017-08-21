package online.dinghuiye.core.validation.testcase;

import com.alibaba.fastjson.annotation.JSONField;
import online.dinghuiye.api.annotation.validate.Validate;
import online.dinghuiye.core.annotation.convert.BlankToNull;
import online.dinghuiye.core.annotation.convert.DateFormat;
import online.dinghuiye.core.annotation.convert.ValueMap;
import online.dinghuiye.core.annotation.excel.SheetTitleName;
import online.dinghuiye.core.annotation.excel.Transient;
import online.dinghuiye.core.validation.CertCardUniqueValidator;
import online.dinghuiye.core.validation.UsernameUniqueValidator;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @author Strangeen
 * on 2017/08/09
 */
@Entity
@DynamicInsert(true)
@Table(name = "user_info2")
public class UserExtraInfo {

    @Transient
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @SheetTitleName("姓名")
    @Size(min = 1, max = 20, message = "请填写{min}~{max}个字")
    private String name;

    @SheetTitleName("性别")
    @ValueMap("{'男':1,'女':0}")
    @Min(value = 0, message = "填写不正确")
    @Max(value = 1, message = "填写不正确")
    private Integer sex;

    @SheetTitleName("分数")
    private Double score;

    @SheetTitleName("生日")
    @BlankToNull
    @DateFormat("yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    @Past(message = "必须早于当前时间")
    private Date birth;

    @SheetTitleName("身份证")
    @Validate(validator = CertCardUniqueValidator.class, message = "身份证重复")
    @Column(name = "cert_card")
    private String certCard;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public UserExtraInfo() {
    }

    public UserExtraInfo(Integer id, User user, String name, Integer sex, Date birth, String certCard) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.sex = sex;
        this.birth = birth;
        this.certCard = certCard;
    }

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

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getCertCard() {
        return certCard;
    }

    public void setCertCard(String certCard) {
        this.certCard = certCard;
    }

    @Override
    public String toString() {
        return "UserExtraInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", score=" + score +
                ", birth=" + birth +
                ", certCard='" + certCard + '\'' +
                ", user is null: " + (user == null) +
                '}';
    }
}
