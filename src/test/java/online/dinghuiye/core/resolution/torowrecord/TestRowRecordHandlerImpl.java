package online.dinghuiye.core.resolution.torowrecord;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import online.dinghuiye.api.entity.Process;
import online.dinghuiye.api.entity.ResultStatus;
import online.dinghuiye.api.entity.RowRecord;
import online.dinghuiye.core.resolution.torowrecord.testcase.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Strangeen on 2017/08/09
 *
 * @version 2.1.0 on 2017/9/3
 */
public class TestRowRecordHandlerImpl {

    private List<Map<String, Object>> excelRowDataList;
    private List<Map<String, Object>> excelRowDataListForError;

    @Before
    public void initTestData() throws ParseException {
        excelRowDataList = new ArrayList<>();
        excelRowDataListForError = new ArrayList<>();

        // for success
        {
            Map<String, Object> excelRowDataMap = new HashMap<>();
            excelRowDataList.add(excelRowDataMap);
            excelRowDataMap.put("账号", "123456abc");
            excelRowDataMap.put("密码", "888888");
            excelRowDataMap.put("姓名", "测试123");
            excelRowDataMap.put("性别", "男");
            excelRowDataMap.put("生日", new SimpleDateFormat("yyyy-MM-dd").parse("1998-08-21"));
        }
        {
            Map<String, Object> excelRowDataMap = new HashMap<>();
            excelRowDataList.add(excelRowDataMap);
            excelRowDataMap.put("账号", "123457abc");
            excelRowDataMap.put("密码", "888888");
            excelRowDataMap.put("姓名", "测试124");
            excelRowDataMap.put("性别", "女");
            excelRowDataMap.put("生日", "1998-9-1");
        }

        // for error
        {
            Map<String, Object> excelRowDataMap = new HashMap<>();
            excelRowDataListForError.add(excelRowDataMap);
            excelRowDataMap.put("账号", "123458abc");
            excelRowDataMap.put("密码", "888888");
            excelRowDataMap.put("姓名", "小蓝");
            excelRowDataMap.put("性别", "_test");
            excelRowDataMap.put("生日", "1998-a08-21");
        }
        {
            Map<String, Object> excelRowDataMap = new HashMap<>();
            excelRowDataListForError.add(excelRowDataMap);
            excelRowDataMap.put("账号", "123459abc");
            excelRowDataMap.put("密码", "888888");
            excelRowDataMap.put("姓名", "小粉");
            excelRowDataMap.put("性别", "女");
            excelRowDataMap.put("生日", "1998-6-1");
        }
    }

    @Test
    public void testHandleSuccess() throws ParseException {
        RowRecordHandlerImpl handler = new RowRecordHandlerImpl();

        List<RowRecord> rowRecordList = new ArrayList<>();

        // 创建rowRecord
        int i = 0;
        for (Map<String, Object> data : excelRowDataList) {
            RowRecord rowRecord = handler.rowRecordCreate(data, i + 2);
            rowRecordList.add(rowRecord);
            i ++;
        }

        // convert并解析pojo
        Process process = new Process((long) rowRecordList.size());
        boolean success = handler.handle(rowRecordList, process, User.class);

        Assert.assertEquals(new Double(100.0), process.getProcess());
        Assert.assertTrue(success);

        {
            RowRecord rr = rowRecordList.get(0);
            Assert.assertEquals("{\"账号\":\"123456abc\",\"密码\":\"888888\",\"姓名\":\"测试123\",\"生日\":903628800000,\"性别\":\"男\"}", JSONArray.toJSONString(rr.getExcelRecordMap()));
            Assert.assertEquals(rr.getResult().getResult(), ResultStatus.SUCCESS);
            Assert.assertNull(rr.getResult().getMsg());
            Assert.assertEquals(rr.getRowNo(), 2);
            User user = (User) rr.getPojoRecordMap().get(User.class);
            Assert.assertEquals("123456abc", user.getUsername());
            Assert.assertEquals("888888", user.getPassword());
            Assert.assertEquals("测试123", user.getInfo().getName());
            Assert.assertEquals(true, user.getInfo().getSex() == 1);
            Assert.assertEquals(new SimpleDateFormat("yyyy-MM-dd").parse("1998-08-21"), user.getInfo().getBirth());
        }

        {
            RowRecord rr = rowRecordList.get(1);
            Assert.assertEquals("{\"账号\":\"123457abc\",\"密码\":\"888888\",\"姓名\":\"测试124\",\"生日\":\"1998-9-1\",\"性别\":\"女\"}", JSONArray.toJSONString(rr.getExcelRecordMap()));
            Assert.assertEquals(rr.getResult().getResult(), ResultStatus.SUCCESS);
            Assert.assertNull(rr.getResult().getMsg());
            Assert.assertEquals(rr.getRowNo(), 3);
            User user = (User) rr.getPojoRecordMap().get(User.class);
            Assert.assertEquals("123457abc", user.getUsername());
            Assert.assertEquals("888888", user.getPassword());
            Assert.assertEquals("测试124", user.getInfo().getName());
            Assert.assertEquals(true, user.getInfo().getSex() == 0);
            Assert.assertEquals(new SimpleDateFormat("yyyy-MM-dd").parse("1998-9-1"), user.getInfo().getBirth());
        }

        System.out.println(JSONObject.toJSONString(rowRecordList));
    }


    @Test
    public void testHandleError() throws ParseException {
        RowRecordHandlerImpl handler = new RowRecordHandlerImpl();

        List<RowRecord> rowRecordList = new ArrayList<>();

        // 创建rowRecord
        int i = 0;
        for (Map<String, Object> data : excelRowDataListForError) {
            RowRecord rowRecord = handler.rowRecordCreate(data, i + 2);
            rowRecordList.add(rowRecord);
            i ++;
        }
        // convert并解析pojo
        boolean success = handler.handle(rowRecordList, null, User.class);

        System.out.println(rowRecordList);
        Assert.assertFalse(success);

        {
            RowRecord rr = rowRecordList.get(0);
            Assert.assertEquals("{\"账号\":\"123458abc\",\"密码\":\"888888\",\"姓名\":\"小蓝\",\"生日\":\"1998-a08-21\",\"性别\":\"_test\"}", JSONArray.toJSONString(rr.getExcelRecordMap()));
            Assert.assertEquals(rr.getResult().getResult(), ResultStatus.FAIL);
            Assert.assertEquals("生日解析错误，行：2", rr.getResult().getMsg());
            Assert.assertEquals(rr.getRowNo(), 2);
            User user = (User) rr.getPojoRecordMap().get(User.class);
            Assert.assertEquals("123458abc", user.getUsername());
            Assert.assertEquals("888888", user.getPassword());
            Assert.assertNull(user.getInfo());

            System.out.println(JSONObject.toJSONString(rowRecordList.get(0)));
        }

        {
            RowRecord rr = rowRecordList.get(1);
            Assert.assertEquals("{\"账号\":\"123459abc\",\"密码\":\"888888\",\"姓名\":\"小粉\",\"生日\":\"1998-6-1\",\"性别\":\"女\"}", JSONArray.toJSONString(rr.getExcelRecordMap()));
            Assert.assertEquals(rr.getResult().getResult(), ResultStatus.SUCCESS);
            Assert.assertNull(rr.getResult().getMsg());
            Assert.assertEquals(rr.getRowNo(), 3);
            User user = (User) rr.getPojoRecordMap().get(User.class);
            Assert.assertEquals("123459abc", user.getUsername());
            Assert.assertEquals("888888", user.getPassword());
            Assert.assertEquals("小粉", user.getInfo().getName());
            Assert.assertEquals(true, user.getInfo().getSex() == 0);
            Assert.assertEquals(new SimpleDateFormat("yyyy-MM-dd").parse("1998-6-1"), user.getInfo().getBirth());

            System.out.println(JSONObject.toJSONString(rowRecordList.get(1)));
        }
    }
}
