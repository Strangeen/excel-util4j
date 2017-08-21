package online.dinghuiye.core.validation;

import online.dinghuiye.api.entity.ResultStatus;
import online.dinghuiye.api.entity.RowRecord;
import online.dinghuiye.api.entity.RowRecordHandleResult;
import online.dinghuiye.api.validation.RowRecordValidator;
import online.dinghuiye.core.validation.testcase.SchoolMan;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * Created by Strangeen on 2017/8/4.
 */
public class TestRowRecordValidatorImpl {

    private List<RowRecord> rowRecordList;
    private List<RowRecord> rowRecordListForSuccess;
    private RowRecordValidator validator = new RowRecordValidatorImpl();

    @Before
    public void initTestData() {

        // test error
        rowRecordList = new ArrayList<>();
        {
            SchoolMan man = new SchoolMan(
                null, null, new Date(), null, null,
                    null, null, null, null, null, null
            );

            RowRecord rowRecord = new RowRecord();
            rowRecord.set(SchoolMan.class, man);
            rowRecord.setResult(new RowRecordHandleResult(ResultStatus.SUCCESS, null));
            rowRecordList.add(rowRecord);
        }

        {
            SchoolMan man = new SchoolMan(
                    null, "测试教师", new Date(), "测试简介", 3,
                    null, null, null, null, "aaa", 3
            );

            RowRecord rowRecord = new RowRecord();
            rowRecord.set(SchoolMan.class, man);
            rowRecord.setResult(new RowRecordHandleResult(ResultStatus.SUCCESS, null));
            rowRecordList.add(rowRecord);
        }

        {
            SchoolMan man = new SchoolMan(
                    null,
                    "测试教师12345678901234567890123456789012345678901234567890",
                    new Date(),
                    "测试简介123456789012345678901234567890123456789012345678901234567890" +
                            "1234567890123456789012345678901234567890",
                    0,
                    "123456789012345678901234567890123456789012345678901",
                    null,
                    "123456789012345678901234567890",
                    "123456789012345678901234567890123456789012345678901",
                    "12345678901",
                    1
            );

            RowRecord rowRecord = new RowRecord();
            rowRecord.set(SchoolMan.class, man);
            rowRecord.setResult(new RowRecordHandleResult(ResultStatus.SUCCESS, null));
            rowRecordList.add(rowRecord);
        }


        // test success
        rowRecordListForSuccess = new ArrayList<>();
        {
            SchoolMan man = new SchoolMan(
                    null, "测试教师", new Date(), "测试简介", 0,
                    null, null, null, null, "22345678901", 0
            );

            RowRecord rowRecord = new RowRecord();
            rowRecord.set(SchoolMan.class, man);
            rowRecord.setResult(new RowRecordHandleResult(ResultStatus.SUCCESS, null));
            rowRecordListForSuccess.add(rowRecord);
        }

    }

    @Test
    public void testValidSuccess() {

        ResetTestValue.reset();

        boolean success = validator.valid(rowRecordListForSuccess);
        Assert.assertEquals(true, success);
    }

    @Test
    public void testValidError() {

        ResetTestValue.reset();

        boolean success = validator.valid(rowRecordList);

        System.out.println(rowRecordList);

        Assert.assertEquals(false, success);
        {
            Assert.assertEquals(
                    true,
                    setsEqual(
                            arrayToSet(new String[]{
                                    "类别不能为空","人物简介不能为空","姓名不能为空","性别不能为空"}),
                            arrayToSet(rowRecordList.get(0).getResult().getMsg().split(";"))
                    )
            );
        }

        {
            Assert.assertEquals(
                    true,
                    setsEqual(
                            arrayToSet(new String[]{
                                    "类别填写不正确","性别填写不正确","电话填写不正确"}),
                            arrayToSet(rowRecordList.get(1).getResult().getMsg().split(";"))
                    )
            );
        }

        {
            Assert.assertEquals(
                    true,
                    setsEqual(
                            arrayToSet(new String[]{
                                    "电话已被注册","班级请最多填写25个字",
                                    "专业请最多填写50个字","学院请最多填写50个字",
                                    "人物简介请最多填写100个字","姓名请最多填写11个字"}),
                            arrayToSet(rowRecordList.get(2).getResult().getMsg().split(";"))
                    )
            );
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
}
