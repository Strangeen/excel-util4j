package online.dinghuiye.core.resolution.torowrecord;

import online.dinghuiye.api.entity.RowRecord;
import online.dinghuiye.core.resolution.torowrecord.testcase.Score;
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
public class TestRowRecordHandlerSinglePojoImpl {

    private List<Map<String, Object>> excelRowDataList;
    private List<Map<String, Object>> excelRowDataListForError;

    @Before
    public void initTestData() {
        excelRowDataList = new ArrayList<>();
        excelRowDataListForError = new ArrayList<>();

        // for success
        {
            Map<String, Object> excelRowDataMap = new HashMap<>();
            excelRowDataList.add(excelRowDataMap);
            excelRowDataMap.put("姓名", "小明");
            excelRowDataMap.put("年龄", "23");
            excelRowDataMap.put("性别", "男");
            excelRowDataMap.put("生日", "1995-12-1");
            excelRowDataMap.put("学科", "语文");
            excelRowDataMap.put("分数", "99");
        }
        {
            Map<String, Object> excelRowDataMap = new HashMap<>();
            excelRowDataList.add(excelRowDataMap);
            excelRowDataMap.put("姓名", "小花");
            excelRowDataMap.put("年龄", "20");
            excelRowDataMap.put("性别", "女");
            excelRowDataMap.put("生日", "1997-6-1");
        }

        // for error
        {
            Map<String, Object> excelRowDataMap = new HashMap<>();
            excelRowDataListForError.add(excelRowDataMap);
            excelRowDataMap.put("姓名", "小蓝");
            excelRowDataMap.put("年龄", "testError");
            excelRowDataMap.put("性别", "女");
            excelRowDataMap.put("生日", "1997-6-1");
        }
        {
            Map<String, Object> excelRowDataMap = new HashMap<>();
            excelRowDataListForError.add(excelRowDataMap);
            excelRowDataMap.put("姓名", "小粉");
            excelRowDataMap.put("年龄", "19");
            excelRowDataMap.put("性别", "女");
            excelRowDataMap.put("生日", "1998-6-1");
        }
    }

    @Test
    public void testHandleSuccess() {
        RowRecordHandlerSinglePojoImpl handler = new RowRecordHandlerSinglePojoImpl();

        List<RowRecord> rowRecordList = new ArrayList<>();

        // 创建rowRecord
        int i = 0;
        for (Map<String, Object> data : excelRowDataList) {
            RowRecord rowRecord = handler.rowRecordCreate(data, i + 2);
            rowRecordList.add(rowRecord);
            i ++;
        }

            // convert并解析pojo
        boolean success = handler.handle(rowRecordList, Student.class);

        Assert.assertEquals(true, success);
        Assert.assertEquals(
                "RowRecord{rowNo=2, result=RowRecordHandleResult{result=SUCCESS, msg='null'}, excelRecordMap={学科=语文, 分数=99, 姓名=小明, 生日=1995-12-1, 年龄=23, 性别=男}, pojoRecordMap={class online.dinghuiye.core.resolution.torowrecord.testcase.Student=Student{name='小明', age=23, birthday=Fri Dec 01 00:00:00 CST 1995, sex=1, enable=1}}}",
                rowRecordList.get(0).toString()
        );
        Assert.assertEquals(
                "RowRecord{rowNo=3, result=RowRecordHandleResult{result=SUCCESS, msg='null'}, excelRecordMap={姓名=小花, 生日=1997-6-1, 年龄=20, 性别=女}, pojoRecordMap={class online.dinghuiye.core.resolution.torowrecord.testcase.Student=Student{name='小花', age=20, birthday=Sun Jun 01 00:00:00 CST 1997, sex=0, enable=1}}}",
                rowRecordList.get(1).toString()
        );
    }


    @Test
    public void testHandleError() {
        RowRecordHandlerSinglePojoImpl handler = new RowRecordHandlerSinglePojoImpl();

        List<RowRecord> rowRecordList = new ArrayList<>();

        // 创建rowRecord
        int i = 0;
        for (Map<String, Object> data : excelRowDataListForError) {
            RowRecord rowRecord = handler.rowRecordCreate(data, i + 2);
            rowRecordList.add(rowRecord);
            i ++;
        }

        // convert并解析pojo
        boolean success = handler.handle(rowRecordList, Student.class);

        Assert.assertEquals(false, success);
        Assert.assertEquals("[RowRecord{rowNo=2, result=RowRecordHandleResult{result=FAIL, msg='解析错误'}, excelRecordMap={姓名=小蓝, 生日=1997-6-1, 年龄=testError, 性别=女}, pojoRecordMap={class online.dinghuiye.core.resolution.torowrecord.testcase.Student=Student{name='小蓝', age=null, birthday=null, sex=null, enable=null}}}, RowRecord{rowNo=3, result=RowRecordHandleResult{result=SUCCESS, msg='null'}, excelRecordMap={姓名=小粉, 生日=1998-6-1, 年龄=19, 性别=女}, pojoRecordMap={class online.dinghuiye.core.resolution.torowrecord.testcase.Student=Student{name='小粉', age=19, birthday=Mon Jun 01 00:00:00 CST 1998, sex=0, enable=1}}}]",
                rowRecordList.toString());
    }

    @Test
    public void testHandleMultiPojo() {
        RowRecordHandlerSinglePojoImpl handler = new RowRecordHandlerSinglePojoImpl();

        List<RowRecord> rowRecordList = new ArrayList<>();

        // 创建rowRecord
        int i = 0;
        for (Map<String, Object> data : excelRowDataList) {
            RowRecord rowRecord = handler.rowRecordCreate(data, i + 2);
            rowRecordList.add(rowRecord);
            i ++;
        }

        // convert并解析pojo
        boolean success = handler.handle(rowRecordList, Student.class, Score.class);

        Assert.assertEquals(true, success);

        Assert.assertEquals("{class online.dinghuiye.core.resolution.torowrecord.testcase.Student=Student{name='小明', age=23, birthday=Fri Dec 01 00:00:00 CST 1995, sex=1, enable=1}, class online.dinghuiye.core.resolution.torowrecord.testcase.Score=Score{scoreName='语文', score=99}}",
                rowRecordList.get(0).getPojoRecordMap().toString());

        Assert.assertEquals("{class online.dinghuiye.core.resolution.torowrecord.testcase.Student=Student{name='小花', age=20, birthday=Sun Jun 01 00:00:00 CST 1997, sex=0, enable=1}, class online.dinghuiye.core.resolution.torowrecord.testcase.Score=Score{scoreName='null', score=null}}",
                rowRecordList.get(1).getPojoRecordMap().toString());

    }
}
