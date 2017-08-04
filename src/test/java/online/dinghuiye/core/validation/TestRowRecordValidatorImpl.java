package online.dinghuiye.core.validation;

import online.dinghuiye.api.validation.RowRecordValidator;
import online.dinghuiye.core.entity.RowRecord;
import online.dinghuiye.core.resolution.torowrecord.RowRecordHandlerSinglePojoImpl;
import online.dinghuiye.core.resolution.torowrecord.testcase.Student;
import online.dinghuiye.core.validation.testcase.SchoolMan;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * Created by Strangeen on 2017/8/4.
 */
public class TestRowRecordValidatorImpl {

    //private SchoolMan man;
    private List<RowRecord> rowRecordList;
    private RowRecordValidator validator = new RowRecordValidatorImpl();

    @Before
    public void initTestData() {
        rowRecordList = new ArrayList<>();

        {
            SchoolMan man = new SchoolMan(
                null, null, new Date(), null, null,
                    null, null, null, null, null, null
            );

            RowRecord rowRecord = new RowRecord();
            rowRecord.set(SchoolMan.class, man);
            rowRecordList.add(rowRecord);
        }

        {
            SchoolMan man = new SchoolMan(
                    null, "测试教师", new Date(), "测试简介", 3,
                    null, null, null, null, null, 3
            );

            RowRecord rowRecord = new RowRecord();
            rowRecord.set(SchoolMan.class, man);
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
                    "aaa",
                    1
            );

            RowRecord rowRecord = new RowRecord();
            rowRecord.set(SchoolMan.class, man);
            rowRecordList.add(rowRecord);
        }

    }

    @Test
    public void testValid() {
        validator.valid(rowRecordList);

        Assert.assertEquals(
                "类别不能为空;人物简介不能为空;姓名不能为空;性别不能为空;",
                rowRecordList.get(0).getResult().getMsg()
        );
        Assert.assertEquals(
                "类别填写不正确;性别填写不正确;",
                rowRecordList.get(1).getResult().getMsg()
        );
        Assert.assertEquals(
                "电话填写不正确;班级请最多填写25个字;专业请最多填写50个字;" +
                        "学院请最多填写50个字;人物简介请最多填写100个字;姓名请最多填写11个字;",
                rowRecordList.get(2).getResult().getMsg()
        );
    }
}
