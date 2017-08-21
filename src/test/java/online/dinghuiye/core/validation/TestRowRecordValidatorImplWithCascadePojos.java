package online.dinghuiye.core.validation;

import online.dinghuiye.api.entity.ResultStatus;
import online.dinghuiye.api.entity.RowRecord;
import online.dinghuiye.api.entity.RowRecordHandleResult;
import online.dinghuiye.api.validation.RowRecordValidator;
import online.dinghuiye.core.validation.testcase.User;
import online.dinghuiye.core.validation.testcase.UserExtraInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

/**
 * @author Strangeen
 * on 2017/08/09
 */
public class TestRowRecordValidatorImplWithCascadePojos {


    private List<RowRecord> rowRecordList;
    private List<RowRecord> rowRecordListForSuccess;
    private RowRecordValidator validator = new RowRecordValidatorImpl();

    @Before
    public void initTestData() {

        // test success
        rowRecordListForSuccess = new ArrayList<>();
        {
            User user = new User(
                    null,
                    "223456",
                    "888888",
                    null
                    );
            UserExtraInfo info = new UserExtraInfo(
                    null,
                    user,
                    "测试用户姓名",
                    0,
                    new Date(),
                    "666667"
                    );
            user.setInfo(info);

            RowRecord rowRecord = new RowRecord();
            rowRecord.set(User.class, user);
            rowRecord.setResult(new RowRecordHandleResult(ResultStatus.SUCCESS, null));
            rowRecordListForSuccess.add(rowRecord);
        }

        {
            User user = new User(
                    null,
                    "223457",
                    "888888",
                    null
            );
            UserExtraInfo info = new UserExtraInfo(
                    null,
                    user,
                    "测试用户姓名2",
                    1,
                    new Date(),
                    "666668"
            );
            user.setInfo(info);

            RowRecord rowRecord = new RowRecord();
            rowRecord.set(User.class, user);
            rowRecord.setResult(new RowRecordHandleResult(ResultStatus.SUCCESS, null));
            rowRecordListForSuccess.add(rowRecord);
        }



        // test error
        rowRecordList = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        {
            User user = new User(
                    null,
                    "1234580000000000000000000000000000000000000000",
                    "8888880000000000000000000000000000000000000000",
                    null
            );
            c.setTime(new Date());
            c.add(Calendar.YEAR, 1);
            UserExtraInfo info = new UserExtraInfo(
                    null,
                    user,
                    "测试用户姓名30000000000000000000000000000000000",
                    3,
                    c.getTime(),
                    "666666"
            );
            user.setInfo(info);

            RowRecord rowRecord = new RowRecord();
            rowRecord.set(User.class, user);
            rowRecord.setResult(new RowRecordHandleResult(ResultStatus.SUCCESS, null));
            rowRecordList.add(rowRecord);
        }

        {
            User user = new User(
                    null,
                    "123459",
                    "888889",
                    null
            );
            UserExtraInfo info = new UserExtraInfo(
                    null,
                    user,
                    "测试用户姓名4",
                    1,
                    new Date(),
                    ""
            );
            user.setInfo(info);

            RowRecord rowRecord = new RowRecord();
            rowRecord.set(User.class, user);
            rowRecord.setResult(new RowRecordHandleResult(ResultStatus.SUCCESS, null));
            rowRecordList.add(rowRecord);
        }

        {
            // success

            User user = new User(
                    null,
                    "223459",
                    "888889",
                    null
            );
            UserExtraInfo info = new UserExtraInfo(
                    null,
                    user,
                    "测试用户姓名3.5",
                    1,
                    new Date(),
                    "888889"
            );
            user.setInfo(info);

            RowRecord rowRecord = new RowRecord();
            rowRecord.set(User.class, user);
            rowRecord.setResult(new RowRecordHandleResult(ResultStatus.SUCCESS, null));
            rowRecordList.add(rowRecord);
        }

        {
            User user = new User(
                    null,
                    "",
                    "8888",
                    null
            );
            UserExtraInfo info = new UserExtraInfo(
                    null,
                    user,
                    null,
                    null,
                    new Date(),
                    "888889"
            );
            user.setInfo(info);

            RowRecord rowRecord = new RowRecord();
            rowRecord.set(User.class, user);
            rowRecord.setResult(new RowRecordHandleResult(ResultStatus.SUCCESS, null));
            rowRecordList.add(rowRecord);
        }

    }

    private <T> boolean setsEqual(Set<T> s1, Set<T> s2) {

        if (s1.size() != s2.size()) return false;
        for (T t : s1) {
            if (!s2.contains(t)) return false;
        }
        return true;
    }

    private <T> Set<T> arrayToSet(T[] ts) {

        Collection<T> cs = Arrays.asList(ts);
        Set<T> set = new HashSet<>();
        set.addAll(cs);
        return set;
    }


    @Test
    public void testValidSuccessForCascadePojo() { // Multiple Pojo

        ResetTestValue.reset();

        boolean success = validator.valid(rowRecordListForSuccess);

        Assert.assertTrue(success);
        Assert.assertNull(rowRecordListForSuccess.get(0).getResult().getMsg());
        Assert.assertNull(rowRecordListForSuccess.get(1).getResult().getMsg());
    }

    @Test
    public void testValidErrorForCascadePojo() { // Multiple Pojo

        ResetTestValue.reset();

        boolean success = validator.valid(rowRecordList);

        Assert.assertFalse(success);

        Assert.assertEquals(ResultStatus.FAIL, rowRecordList.get(0).getResult().getResult());
        Assert.assertEquals(
                true,
                setsEqual(
                        arrayToSet(new String[]{
                                "账号请填写6~20个字","生日必须早于当前时间",
                                "密码请填写6~20个字","性别填写不正确","姓名请填写1~20个字",
                                "身份证身份证重复"}),
                        arrayToSet(rowRecordList.get(0).getResult().getMsg().split(";"))
                )
        );

        Assert.assertEquals(ResultStatus.FAIL, rowRecordList.get(1).getResult().getResult());
        Assert.assertEquals(
                true,
                setsEqual(
                        arrayToSet(new String[]{
                                "账号用户名已被注册"}),
                        arrayToSet(rowRecordList.get(1).getResult().getMsg().split(";"))
                )
        );

        Assert.assertEquals(ResultStatus.SUCCESS, rowRecordList.get(2).getResult().getResult());

        Assert.assertEquals(ResultStatus.FAIL, rowRecordList.get(3).getResult().getResult());
        Assert.assertEquals(
                true,
                setsEqual(
                        arrayToSet(new String[]{
                                "账号请填写6~20个字","密码请填写6~20个字","身份证身份证重复"}),
                        arrayToSet(rowRecordList.get(3).getResult().getMsg().split(";"))
                )
        );
    }
}
