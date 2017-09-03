package online.dinghuiye.core;

import online.dinghuiye.api.entity.Process;
import online.dinghuiye.api.entity.RowRecord;
import online.dinghuiye.api.entity.TransactionMode;
import online.dinghuiye.core.persistence.RowRecordPersistencorHibernateImpl;
import online.dinghuiye.core.resolution.torowrecord.RowRecordHandlerImpl;
import online.dinghuiye.core.validation.ResetTestValue;
import online.dinghuiye.core.validation.testcase.SchoolMan;
import online.dinghuiye.excel.ExcelFactory;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Strangeen on 2017/8/7
 *
 * @author Strangeen on 2017/9/3
 * @version 2.1.0
 */
public class TestImportHandler {

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

        ResetTestValue.reset();

        ImportHandler handler = new ImportHandler(
                new RowRecordHandlerImpl(),
                new RowRecordPersistencorHibernateImpl(factory), TransactionMode.SINGLETON);
        List<RowRecord> list = handler.importExcel(
                ExcelFactory.newExcel(new File("D:/test/schoolman.xlsx")),
                new Observer() {
                    @Override
                    public void update(Observable o, Object arg) {
                        Process process = (Process) arg;
                        System.out.println("进度：" + process.getProcess() + "，当前阶段：" + process.getNode());
                    }
                },
                SchoolMan.class);
        for (RowRecord rr : list) {
            System.out.println(rr.getResult().getMsg());
        }
    }

    // 可以保存一条
    @Test
    public void testImportExcelSingletonError() {

        ResetTestValue.reset();

        ImportHandler handler = new ImportHandler(
                new RowRecordPersistencorHibernateImpl(factory), TransactionMode.SINGLETON);
        List<RowRecord> list = handler.importExcel(
                ExcelFactory.newExcel(new File("D:/test/schoolman_error.xlsx")),
                new Observer() {
                    @Override
                    public void update(Observable o, Object arg) {
                        Process process = (Process) arg;
                        System.out.println("进度：" + process.getProcess() + "，当前阶段：" + process.getNode());
                    }
                },
                SchoolMan.class);
        for (RowRecord rr : list) {
            System.out.println("行号：" + rr.getRowNo() + ", 错误信息：" + rr.getResult().getMsg());
        }
    }



    @Test
    public void testImportExcelMultipleSuccess() {

        ResetTestValue.reset();

        ImportHandler handler = new ImportHandler(
                new RowRecordPersistencorHibernateImpl(factory), TransactionMode.MULTIPLE);
        List<RowRecord> list = handler.importExcel(
                ExcelFactory.newExcel(new File("D:/test/schoolman.xlsx")),
                new Observer() {
                    @Override
                    public void update(Observable o, Object arg) {
                        Process process = (Process) arg;
                        System.out.println("进度：" + process.getProcess() + "，当前阶段：" + process.getNode());
                    }
                },
                SchoolMan.class);
        for (RowRecord rr : list) {
            System.out.println(rr.getResult().getMsg());
        }
    }

    // 均不能保存
    @Test
    public void testImportExcelMultipleError() {

        ResetTestValue.reset();

        ImportHandler handler = new ImportHandler(
                new RowRecordPersistencorHibernateImpl(factory), TransactionMode.MULTIPLE);
        List<RowRecord> list = handler.importExcel(
                ExcelFactory.newExcel(new File("D:/test/schoolman_error.xlsx")),
                new Observer() {
                    @Override
                    public void update(Observable o, Object arg) {
                        Process process = (Process) arg;
                        System.out.println("进度：" + process.getProcess() + "，当前阶段：" + process.getNode());
                    }
                },
                SchoolMan.class);
        for (RowRecord rr : list) {
            System.out.println("行号：" + rr.getRowNo() + ", 错误信息：" + rr.getResult().getMsg());
        }
    }

}
