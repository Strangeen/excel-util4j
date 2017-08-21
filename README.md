# **EXCEL-UTIL4J**

为方便java导入excel到数据库开发的一款工具，主要思想为：通过编写POJO将excel表字段和数据库表字段进行关联，util会自动将excel数据注入到POJO中，然后对POJO对象属性值进行验证，验证通过后再将POJO对象持久化到数据库。整个过程用户只需要编写POJO和相关的转换、验证业务代码，以及util的接入代码，其余工作就交给util进行操作了。 **BUT...**

1. 最多只支持一对一关系的表结构进行导入，excel中一行数据只能包含多张表的一条数据

2. 默认只采用hibernate进行持久化，毕竟这种导入形式使用hibernate再适合不过了，如需要使用其他持久化方式，需要自行实现接口编写持久化代码

# 先行配置包

1. [poikit](https://github.com/Strangeen/poi-kit)

2. [excelutil-api](https://github.com/Strangeen/excel-util4j-api)

3. [persistence-hibernate-impl](https://github.com/Strangeen/excel-util4j-persistence4h)（如果不使用hibernate持久化，那么可以不用导入）

注意事项：

- 使用maven构建项目，配套包均需要预先安装，下载源码后执行如下maven命令：
```
mvn clean install -Dmaven.test.skip=true
```

- 使用jar包导入，仅需要获取各jar即可（方法在各自的文档中给出了）


# 获取jar包

maven下执行命令
```
mvn clean package -Dmaven.test.skip=true
```

# Quick Start

## 1. 定义POJO

POJO中定义属性与excel表头字段关系，验证注解以及hibernate POJO规则注解

定义一对一关系数据POJO，假设User.class和UserExtraInfo.class：

`User.class`
```java
@Entity
@DynamicInsert(true)
@Table(name = "user")
public class User {

    @Transient // 不用解析该属性注解
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @SheetTitleName("账号") // 与excel表头名称对应
    @NotNull
    @Size(min = 6, max = 20, message = "请填写{min}~{max}个字")
    // 自定义验证注解，此处用于判重，不用太在意，详见下面的文档：自定义验证
    @Validate(validator = UsernameUniqueValidator.class, message = "已被注册")
    private String username;

    @SheetTitleName("密码")
    @NotNull(message = "不能为空")
    @Size(min = 6, max = 20, message = "请填写{min}~{max}个字")
    private String password;

    @Valid
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserExtraInfo info; // 一对一关系引用

    // ...
}
```
- `@Validate`注解用法参见[自定义验证](https://github.com/Strangeen/excel-util4j#自定义验证)

`UserExtraInfo.class`
```java
@Entity
@DynamicInsert(true)
@Table(name = "user_info")
public class UserExtraInfo {

    @Transient
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @SheetTitleName("姓名")
    @Size(min = 1, max = 20, message = "请填写{min}~{max}个字")
    private String name;

    @SheetTitleName("性别")
    @ValueMap("{'男':1,'女':0}") // 字段对应值转换
    @Min(value = 0, message = "填写不正确")
    @Max(value = 1, message = "填写不正确")
    private Integer sex;

    @SheetTitleName("生日")
    @BlankToNull
    @DateFormat("yyyy-MM-dd") // 日期格式化转换
    @Past(message = "必须早于当前时间")
    private Date birth;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user; // 一对一关系引用

    // ...
}
```
- `@ValueMap`和`@DateFormat`注解参见下面文档[①数据转换](https://github.com/Strangeen/excel-util4j#数据转换)

### 2. 编写接入方法
```java
private SessionFactory factory;
private TransactionMode mode;

// 创建导入器handler
ImportHandler handler = new ImportHandler(
                new RowRecordHandlerImpl(), // 一对一关系解析
                new RowRecordPersistencorHibernateImpl(factory),
                mode);

// 执行excel导入
List<RowRecord> resultList = handler.importExcel(
                ExcelFactory.newExcel(new File("D:/userinfo.xls")), // 创建AbstractExcel对象
                0, // 读取sheet序号为0的sheet
                (Observable o, Object arg) -> System.out.println("进度：" + arg), // 创建导入进度观察者，arg为导入进度百分数（没有%）
                User.class); // 传入POJO
```

- `RowRecordHandlerImpl`为一对一关系解析，如果使用单pojo解析使用`RowRecordHandlerSinglePojoImpl`，具体参见[②解析excel数据为pojo对象 - ②装配](https://github.com/Strangeen/excel-util4j#装配)

- `RowRecordPersistencorHibernateImpl`为hibernate实现持久化，`factory`为`SessionFactory`

- `mode`为`TransactionMode`，事务形式，有两种形式：
  1. `单条事务SINGLETON`：
    每一条要导入的数据在导入过程中各自保持原子性，即本条数据验证或导入失败不会影响其他数据的导入，但如果本条数据验证失败本条数据就不会执行下一步（比如存储）
  2. `整体事务MULTIPLE`
    所有要导入的数据在整个导入过程中整体保持原子性，如果验证过程中有至少一条数据失败，整体均不进行下一步（存储），如果存储中有至少一条数据存储失败，整体均不会被存储

- 每条数据的导入结果可以通过遍历`resultList`，对元素执行`getResult()`方法获得`RowRecordHandleResult`，通过`RowRecordHandleResult`的`getResult()`可获取导入结果，`getMsg()`可获得错误信息，如果成功，则`getMsg()`为`NULL`

---

# UTIL执行流程

#### ①[读取excel](https://github.com/Strangeen/excel-util4j#读取excel) -> ②[解析excel数据为pojo对象](https://github.com/Strangeen/excel-util4j#解析excel数据为pojo对象) -> ③[验证pojo对象](https://github.com/Strangeen/excel-util4j#验证pojo对象) -> ④[pojo对象修正](https://github.com/Strangeen/excel-util4j#pojo对象修正) -> ⑤[持久化](https://github.com/Strangeen/excel-util4j#持久化)

---

## ①读取excel

### - 配套包：[poikit](https://github.com/Strangeen/poi-kit)

### - 注解：

1. **@Transient**

  过滤属性注解，如果POJO中的属性不需要进行解析和转换的，可以添加该属性。比如：`id`属性就可以添加注解

2. **@SheetTitleName**

  excel表头字段名称注解，如果属性名与excel表头名称不一致，需要使用该注解标注。比如POJO属性为`name`，excel表头字段名称为`姓名`，需要如下标注属性：
  ```java
  @SheetTitleName("姓名")
  private String name;    
  ```

### - 说明：

使用apache poi对excel按行读取，结果封装为`List<Map<String, Object>>`，`Map`的`key`为excel列表头名称，`value`为数据，数据根据excel单元格类型即值说匹配对应的java类型

例如：

单元格设置“常规”，`123`将得到`Integer`，`123.1`将得到`Double`，`abc`将得到`String`；

单元格设置“日期”，`1988-01-01`将得到`Date`；

单元格值输入公式`=1+2`，值将得到`Integer` `3`

使用示例代码查看[示例代码](https://github.com/Strangeen/poi-kit#示例代码)，但是这里用户并不用关心这些的！

---

## ②解析excel数据为pojo对象

### - 配套包：[excelutil-api](https://github.com/Strangeen/excel-util4j-api)

### - 说明：

#### 1. pojo对象属性执行流程：

①数据转换 -> ②装配

##### ①数据转换

数据转换是通过对POJO属性配置注解，将excel的值转换为需要的值的操作，例如：excel`性别`字段读取为`男`、`女`，而实际需要的为`1`、`0`，就需要转换（使用`@ValueMap`注解）

excelutil提供了5种注解用于数据转换，基本能够满足数据转换需求，如果**属性配置可多个转换注解，转换的顺序就是注解的顺序，自定义注解`@ValueConvert`的转换顺序是转换器数组的顺序**

转换注解列表如下：

1. **@ConstValue**

  常量值转换注解，注解对应的实现类为`ConstValueConvertor`

  比如表中`enable`字段的值需要设置为`1`，而excel中并么有对应的值，那么可以在POJO中对应的属性上使用该注解
  ```java
  @ConstValue(value = "1")
  private Integer enable;
  ```
  又比如，需要设置初始密码为`123456`，可以使用

  ```java
  @ConstValue(value = "123456")
  private String password;
  ```

2. **@BlankToNull**

  空值转换为NULL注解，注解对应的实现类为`BlankToNullConvertor`

  这里`空值`表示`Blank`，比如`空串`，或者`空白`，实现方式是lang3包中的`StringUtils.isBlank`

  比如excel中没有填写内容或者填写空白，此时获取到的值可能为空串或者空白，而该值在数据表中有唯一约束，如果存入空白可能导致后面同样情况无法存入，此时就需要转换为NULL
  ```java
  @BlankToNull
  private String email;
  ```
  该注解在时间格式转换时也会用到

3. **@DateFormat**

  时间格式转换注解，注解对应的实现类为`DateFormaConvertor`

  当POJO属性为时间时，可以将excel中对应的时间样式字符串转换为`Date`对象（excel单元格设置为`文本`格式时，如果是`日期`格式那么获取到的就是`Date`对象，无需转换，不过添加该注解也不会有问题的）

  比如转换生日字段
  ```java
  @DateFormat(value = "yyyy-MM-dd HH:mm:ss")
  private Date birthday;
  ```

  如果时间可以为NULL，那么excel中可能没有填写，建议使用`@BlankToNull`注解
  ```java
  @BlankToNull
  @DateFormat(value = "yyyy-MM-dd HH:mm:ss")
  private Date birthday;
  ```

  如果时间格式为常量，可以按如下代码：
  ```java
  @ConstValue(value = "2017-08-20")
  @DateFormat(value = "yyyy-MM-dd")
  private Date constDate;  
  ```

4. **@ValueMap**

  值映射转换注解，注解对应的实现类为`ValueMapConvertor`，内部实现使用的alibaba的fastjson解析，

  当excel的值需要按照`Map`映射进行转换时，需要用到该注解

  比如excel`性别`字段读取为`男`、`女`，而实际需要的为`1`、`0`，就需要转换：
  ```java
  @ValueMap(value = "{'男':1,'女':0}")
  private String sex;
  ```
  如果excel读取到的值不是`男`或`女`，那么将转换为`NULL`

5. **@ValueConvert**

  自定义转换器注解，注解实现类为`ValueConvertConvertor`

  当预设注解无法满足要求是，可以通过该注解引入自定义注解，比如POJO属性表示当前时间，如`createTime`属性，此时需要自定义转换实现类：

  1. 定义转换类实现`online.dinghuiye.api.resolution.convert.Convertor`接口
  ```java
  public class CurrentTimeConvertor implements Convertor { // 实现Covertor接口

        @Override
        public Object convert(Object obj, Field field, Map<String, Object> excelRecordMap) {
          // 返回当前时间，这里是没有使用参数的，也可以通过参数field和excelRecordMap获取其他值来得到返回值
          return new Date();
        }
  }
  ```
  2. `@ValueConvert`注解引入自定义转换类
  ```java
  @ValueConvert(CurrentTimeConvertor.class)
  private Date createTime;
  ```
  `@ValueConvert`注解的`value()`方法接收的是`Class<? extends Convertor>[]`，因此也可以将各种转换实现类按照数组方式传入，转换顺序为数组顺序

##### ②装配

装配的实现有两种，用户仅需要选择即可

1. **RowRecordHandlerSinglePojoImpl**

  仅支持单层pojo解析，单层pojo的属性仅为基本类型或String类型，没有自定义类型，例如：学生信息pojo

2. **RowRecordHandlerImpl**

  支持多层pojo解析，多层pojo的属性可以为自定义类型，例如：账号信息pojo1，帐号信息pojo1中有属性info，info为附加信息pojo2，附加信息pojo2中有属性account，account为父级帐号信息pojo1，关系就是hibernate的`OneToOne`类型POJO

  装配实现机制：装配pojo时缓存pojo对象到`pojoObjCache`，子级对象引用父级对象现在缓存中查找，查到即装配，没查到自行解析rowRecord数据

---

## ③验证pojo对象

### - 说明：

验证分为两部分：

1. #### hibernate validator验证

    利用hibernate validator实现验证pojo属性验证，具体文档请参见[hibernate validator](https://docs.jboss.org/hibernate/validator/5.4/reference/en-US/html_single/)

    代码示例如下：

    `User.class：`
    ```java
    @NotNull(message = "不能为空") // 属性不为NULL
    @Size(min = 6, max = 20, message = "请填写{min}~{max}个字") // 属性长度在6-20
    private String username;

    @NotNull(message = "不能为空")
    @Size(min = 6, max = 20, message = "请填写{min}~{max}个字")
    private String password;

    @Valid // 深层验证，即验证应用对象的属性
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserExtraInfo info;
    ```

    `UserExtraInfo.class：`
    ```java
    @NotBlank
    @Size(max = 11, message = "请最多填写{max}个字")
    private String realName;

    @Min(value = 0, message = "填写不正确")
    @Max(value = 1, message = "填写不正确")
    private Integer sex;

    @Pattern(regexp = "(^(\\+|0)[0-9]{2}[0-9]{11}$)|(^[0-9]{11}$)", message = "填写不正确") // 正则匹配验证
    private String phone;
    ```

2. #### 自定义验证

  自定义验证用于属性间的关系验证，以及数据库验证，比如判重验证等（貌似validator的自定义验证无法实现，这里我并没有深究，并不能肯定无法实现，以后有时间再研究，所以就自己实现了一套验证机制，和上述的转换代码很类似）

  1. 定义验证类实现`online.dinghuiye.api.validation.Validator`接口，比如电话号码数据库判重：
  ```java
  public class PhoneUniqueValidator implements Validator {

        private static Set<Object> phoneCache; // 先将所有的电话号码从数据库中读取出来，避免每次都去查询数据库

        /*
         构造对象是将数据库已有数据缓存，可以提高验证效率
         但是值得注意的是：
            1. 并发操作数据库时，需要同步缓存，并更新缓存
            2. 成功添加一条数据后，需要更新缓存
          */
        public PhoneUniqueValidator() {
            // 读取数据库数据并存入缓存phoneCache中
        }

        @Override
        public <SchoolMan> boolean validate(Object fieldValue, Field field, SchoolMan obj) {

            if (phoneCache.contains(fieldValue)) return false; // 检查重复
            phoneCache.add(fieldValue); // 不重复更新缓存
            return true;
        }
  }
  ```
  `validate`方法中出了可以判重，还可以利用`obj`获其他属性进行关系判定，验证成功返回`true`，否则返回`false`

  2. `@Validate`注解引入自定义验证类
  ```java
  @Validate(validator = PhoneUniqueValidator.class, message = "已被注册")
  private String phone;
  ```

---

## ④pojo对象修正

### - 配套包：[persistence-hibernate-impl](https://github.com/Strangeen/excel-util4j-persistence4h)

### - 说明：

当pojo对象解析完成后，依然有一些属性无法达到存库要求，比如密码的MD5加密问题，假设excel中设置了每个用户密码字段，这些密码不是相同的，无法使用`@ConstValue`注解解决，当然可以使用自定义注解转换，这里只是举个例子，不一定恰当，那么我们可以在这一步进行修正，将属性值更改为需要的值，这步只是预留了一个接口，方便最终操作，一般可能也用不上

1. 定义修正类（比如`PasswordMd5Repairer`）实现接口`online.dinghuiye.api.persistence.RowRecordPerPersistentRepairer`，此时可以获取到`List<RowRecord> rowRecordList`，这是保存了所有行数据的集合，通过遍历`rowRecordList`，再对元素调用`getPojoRecordMap`方法就可以获取到所有的POJO和其对象的映射`Map`，就可以操作所有对象了，接口就像这样：
```java
public interface RowRecordPerPersistentRepairer {
    void repaire(List<RowRecord> rowRecordList); // rowRecordList包含所有行数据信息
}
```
2. 在入口方法中调用方法即可
```java
importHandler.setRepairer(passwordMd5Repairer);
```

---

## ⑤持久化

### - 配套包：[persistence-hibernate-impl](https://github.com/Strangeen/excel-util4j-persistence4h)

### - 说明：

使用hibernate对pojo对象进行持久化，用法参见[hibernate官方文档](http://hibernate.org/orm/documentation/4.3/)

需要注意：一对一关系数据导入，pojo之间需要设置双向关联，否则可能无法关联导入

如果想实现自己的持久化方式，需要实现接口`online.dinghuiye.api.persistence.RowRecordPersistencor`

---

# 需要改进

1. 持久化性能问题，希望采用values(...),(...)形式的sql执行存储，需要重新实现持久化类

2. 一对多数据导入问题，暂时还没有很好的解决思路

3. ENUM转换器：@ValueEnum注解

后期有时间再改进，要是没时间就只能暂时放下了...
