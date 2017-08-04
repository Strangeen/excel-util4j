package online.dinghuiye.core.resolution.torowrecord;

import online.dinghuiye.core.entity.RowRecord;
import online.dinghuiye.core.resolution.torowrecord.testcase.Student;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Strangeen on 2017/8/3.
 */
public class TestRowRecordHandleerSinglePojoImpl {

    private List<Map<String, String>> excelRowDataList;

    @Before
    public void initTestData() {
        excelRowDataList = new ArrayList<>();

        {
            Map<String, String> excelRowDataMap = new HashMap<>();
            excelRowDataList.add(excelRowDataMap);
            excelRowDataMap.put("姓名", "小明");
            excelRowDataMap.put("年龄", "23");
            excelRowDataMap.put("性别", "男");
            excelRowDataMap.put("生日", "1995-12-1");
        }

        {
            Map<String, String> excelRowDataMap = new HashMap<>();
            excelRowDataList.add(excelRowDataMap);
            excelRowDataMap.put("姓名", "小花");
            excelRowDataMap.put("年龄", "20");
            excelRowDataMap.put("性别", "女");
            excelRowDataMap.put("生日", "1997-6-1");
        }
    }

    @Test
    public void testHandle() {
        RowRecordHandlerSinglePojoImpl handler = new RowRecordHandlerSinglePojoImpl();
        List<RowRecord> rowRecordList = handler.handle(excelRowDataList, Student.class);
        /*for (RowRecord rowRecord : rowRecordList) {
            System.out.println(rowRecord);
        }*/

        Assert.assertEquals(
                "RowRecord{rowNo=2, result=RowRecordHandleResult{result=SUCCESS, msg='null'}, excelRecordMap={姓名=小明, 生日=1995-12-1, 年龄=23, 性别=男}, pojoRecordMap={class online.dinghuiye.core.resolution.torowrecord.testcase.Student=Student{name='小明', age=23, birthday=Fri Dec 01 00:00:00 CST 1995, sex=1, enable=1}}}",
                rowRecordList.get(0).toString()
        );
        Assert.assertEquals(
                "RowRecord{rowNo=3, result=RowRecordHandleResult{result=SUCCESS, msg='null'}, excelRecordMap={姓名=小花, 生日=1997-6-1, 年龄=20, 性别=女}, pojoRecordMap={class online.dinghuiye.core.resolution.torowrecord.testcase.Student=Student{name='小花', age=20, birthday=Sun Jun 01 00:00:00 CST 1997, sex=0, enable=1}}}",
                rowRecordList.get(1).toString()
        );
    }
}
