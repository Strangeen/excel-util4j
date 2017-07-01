package online.dinghuiye.core.resolution;

import online.dinghuiye.api.resolution.RowRecordHandler;
import online.dinghuiye.core.annotation.excel.SheetTitleName;
import online.dinghuiye.core.annotation.excel.Transient;
import online.dinghuiye.core.entity.RowRecord;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Strangeen on 2017/6/27.
 */
public class RowRecordHandlerImpl implements RowRecordHandler {

    public void excelRecordHandle(RowRecord rowRecord, List<Class<?>> pojoList) {



    }

    private void pojoHandle(RowRecord rowRecord, Class<?> pojo) throws IllegalAccessException, InstantiationException {

        Object pojoObj = pojo.newInstance();
        Field[] fields = pojo.getFields();

        for (Field field : fields) {

            Transient transientAnno = field.getAnnotation(Transient.class);
            if (transientAnno != null) continue;

            String sheetTitleName = field.getName();
            SheetTitleName sheetTitleNameAnno = field.getAnnotation(SheetTitleName.class);
            if (sheetTitleNameAnno != null && !"".equals(sheetTitleNameAnno.value())) {
                sheetTitleName = sheetTitleNameAnno.value();
            }

            // TODO 转换




            Object fieldValue = rowRecord.get(sheetTitleName);

            field.set(pojoObj, fieldValue);
        }
    }


    public void pojoRecordHandle() {

    }

}
