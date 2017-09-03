package online.dinghuiye.core;

import online.dinghuiye.api.entity.Process;
import online.dinghuiye.api.entity.ResultStatus;
import online.dinghuiye.api.entity.RowRecord;
import online.dinghuiye.api.entity.TransactionMode;
import online.dinghuiye.core.persistence.RowRecordPersistencorHibernateImpl;
import online.dinghuiye.core.resolution.torowrecord.RowRecordHandlerImpl;
import online.dinghuiye.core.validation.ResetTestValue;
import online.dinghuiye.core.validation.testcase.User;
import online.dinghuiye.excel.ExcelFactory;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Observable;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * Created by Strangeen on 2017/8/7
 *
 * @author Strangeen on 2017/9/3
 * @version 2.1.0
 */
public class TestImportHandlerForCascadePojo {

    private SessionFactory factory;

    @Before
    public void init() {

        // 准备环境
        {
            Configuration configuration = new Configuration().configure("hbm.cfg.xml");
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();
            factory = configuration.buildSessionFactory(serviceRegistry);
        }
    }

    @Test
    public void testImportExcelSingletonSuccess() {
        long start = System.currentTimeMillis();
        testSuccess(TransactionMode.SINGLETON);
        System.out.println("耗时：" + (System.currentTimeMillis() - start) + "ms"); // 5K数据 = 46400ms
    }

    @Test
    public void testImportExcelMultipleSuccess() {
        long start = System.currentTimeMillis();
        testSuccess(TransactionMode.MULTIPLE);
        System.out.println("耗时：" + (System.currentTimeMillis() - start) + "ms"); // 5k数据 = 23754ms
    }

    private void testSuccess(TransactionMode mode) {

        ResetTestValue.reset();

        ImportHandler handler = new ImportHandler(
                new RowRecordHandlerImpl(),
                new RowRecordPersistencorHibernateImpl(factory), mode);

        List<RowRecord> list = handler.importExcel(
                ExcelFactory.newExcel(new File("D:/test/userinfo.xls")),
                1,
                (Observable o, Object arg) -> {
                    Process process = (Process) arg;
                    System.out.println("进度：" + process.getProcess() + "，当前阶段：" + process.getNode());
                },
                User.class);

        list.forEach(rr -> System.out.println(rr.getResult().getMsg()));

        list.forEach(rr -> {
            Assert.assertEquals(ResultStatus.SUCCESS, rr.getResult().getResult());
            Assert.assertNotNull(rr.get(User.class).getId());
            Assert.assertNotNull(rr.get(User.class).getInfo().getId());
        });
    }

    // 可以保存一条
    @Test
    public void testImportExcelSingletonError() {

        long start = System.currentTimeMillis();
        testError(TransactionMode.SINGLETON, list -> {
            IntStream.range(0, list.size()).forEach(
                idx -> {
                    RowRecord rr = list.get(idx);
                    switch (idx) {
                        case 0:
                            Assert.assertEquals(ResultStatus.SUCCESS, rr.getResult().getResult());
                            Assert.assertNotNull(rr.get(User.class).getId());
                            Assert.assertNotNull(rr.get(User.class).getInfo().getId());
                            break;
                        case 1:
                            Assert.assertEquals(ResultStatus.FAIL, rr.getResult().getResult());
                            Assert.assertNull(rr.get(User.class).getId());
                            Assert.assertNull(rr.get(User.class).getInfo());
                            break;
                        case 2:
                            Assert.assertEquals(ResultStatus.FAIL, rr.getResult().getResult());
                            Assert.assertNull(rr.get(User.class).getId());
                            Assert.assertNull(rr.get(User.class).getInfo().getId());
                            break;
                        default:
                            throw new RuntimeException("impossible value of idx");
                    }
                }
            );
            return null;
        });
        System.out.println("耗时：" + (System.currentTimeMillis() - start) + "ms");
    }

    // 均不能保存
    @Test
    public void testImportExcelMultipleError() {

        long start = System.currentTimeMillis();
        testError(TransactionMode.MULTIPLE, list -> {
            IntStream.range(0, list.size()).forEach(
                    idx -> {
                        RowRecord rr = list.get(idx);
                        switch (idx) {
                            case 0:
                                Assert.assertEquals(ResultStatus.SUCCESS, rr.getResult().getResult());
                                Assert.assertNull(rr.get(User.class).getId());
                                Assert.assertNull(rr.get(User.class).getInfo().getId());
                                break;
                            case 1:
                                Assert.assertEquals(ResultStatus.FAIL, rr.getResult().getResult());
                                Assert.assertNull(rr.get(User.class).getId());
                                Assert.assertNull(rr.get(User.class).getInfo());
                                break;
                            case 2:
                                // 解析成功，但并没有存储的机会，因此状态依然为SUCCESS
                                Assert.assertEquals(ResultStatus.SUCCESS, rr.getResult().getResult());
                                Assert.assertNull(rr.get(User.class).getId());
                                Assert.assertNull(rr.get(User.class).getInfo().getId());
                                break;
                            default:
                                throw new RuntimeException("impossible value of idx");
                        }
                    }
            );
            return null;
        });
        System.out.println("耗时：" + (System.currentTimeMillis() - start) + "ms");
    }

    private void testError(TransactionMode mode, Function<List<RowRecord>, Object> function) {

        ResetTestValue.reset();

        ImportHandler handler = new ImportHandler(
                new RowRecordHandlerImpl(),
                new RowRecordPersistencorHibernateImpl(factory), mode);
        List<RowRecord> list = handler.importExcel(
                ExcelFactory.newExcel(new File("D:/test/userinfo_error.xlsx")),
                1,
                (Observable o, Object arg) -> {
                    Process process = (Process) arg;
                    System.out.println("进度：" + process.getProcess() + "，当前阶段：" + process.getNode());
                },
                User.class);

        list.forEach(rr -> {
            System.out.println("行号：" + rr.getRowNo() + ", 错误信息：" + rr.getResult().getMsg());
            System.out.println(rr);
        });

        function.apply(list);
    }
}
