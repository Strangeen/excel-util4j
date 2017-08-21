package online.dinghuiye.core.validation.testcase;

import online.dinghuiye.api.annotation.validate.Validate;
import online.dinghuiye.core.annotation.convert.ValueConvert;
import online.dinghuiye.core.annotation.convert.ValueMap;
import online.dinghuiye.core.annotation.excel.SheetTitleName;
import online.dinghuiye.core.annotation.excel.Transient;
import online.dinghuiye.core.resolution.convert.testcase.CurrentTimeConvertor;
import online.dinghuiye.core.validation.PhoneUniqueValidator;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;

/**
 * Created by Strangeen on 2017/8/3.
 */
@Entity
@Table(name = "school_man")
@DynamicInsert(true)
public class SchoolMan {

    @Transient
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @SheetTitleName("姓名")
    @NotBlank
    @Size(max = 11, message = "请最多填写{max}个字")
    private String name;

    @ValueConvert(CurrentTimeConvertor.class)
    @Column(name = "create_time")
    private Date createTime;

    @SheetTitleName("人物简介")
    @Size(max = 100, message = "请最多填写{max}个字")
    @NotBlank
    private String description;

    @SheetTitleName("性别")
    @ValueMap("{'男':1,'女':0}")
    @NotNull(message = "不能为空")
    @Min(value = 0, message = "填写不正确")
    @Max(value = 1, message = "填写不正确")
    private Integer sex;

    @SheetTitleName("学院")
    @Size(max = 50, message = "请最多填写{max}个字")
    private String college;

    @SheetTitleName("年级")
    @Pattern(regexp = "[0-9]{4}", message = "填写不正确")
    private String grade;

    @SheetTitleName("班级")
    @Size(max = 25, message = "请最多填写{max}个字")
    @Column(name = "class_name")
    private String className;

    @SheetTitleName("专业")
    @Size(max = 50, message = "请最多填写{max}个字")
    private String major;

    @SheetTitleName("电话")
    @Pattern(regexp = "(^(\\+|0)[0-9]{2}[0-9]{11}$)|(^[0-9]{11}$)", message = "填写不正确")
    @Validate(validator = PhoneUniqueValidator.class, message = "已被注册")
    private String phone;

    @SheetTitleName("类别")
    @ValueMap("{'教师':0,'学生':1}")
    @NotNull(message = "不能为空")
    @Min(value = 0, message = "填写不正确")
    @Max(value = 1, message = "填写不正确")
    @Column(name = "cate_id")
    private Integer cateId;

    public SchoolMan() {
    }

    public SchoolMan(Integer id, String name, Date createTime, String description, Integer sex, String college, String grade, String className, String major, String phone, Integer cateId) {
        this.id = id;
        this.name = name;
        this.createTime = createTime;
        this.description = description;
        this.sex = sex;
        this.college = college;
        this.grade = grade;
        this.className = className;
        this.major = major;
        this.phone = phone;
        this.cateId = cateId;
    }

    @Override
    public String toString() {
        return "SchoolMan{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createTime=" + createTime +
                ", description='" + description + '\'' +
                ", sex=" + sex +
                ", college='" + college + '\'' +
                ", grade='" + grade + '\'' +
                ", className='" + className + '\'' +
                ", major='" + major + '\'' +
                ", phone='" + phone + '\'' +
                ", cateId=" + cateId +
                '}';
    }
}
