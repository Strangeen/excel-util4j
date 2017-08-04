package online.dinghuiye.core.resolution.torowrecord;

import online.dinghuiye.core.annotation.excel.SheetTitleName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * Created by Strangeen on 2017/8/4.
 */
public class RowRecordKit {

    //private static final Logger logger = LoggerFactory.getLogger(RowRecordKit.class);

    public static String getSheetTitleNameByFieldName(Field field) {
        try {
            String sheetTitleName = field.getName();
            SheetTitleName sheetTitleNameAnno = field.getAnnotation(SheetTitleName.class);
            if (sheetTitleNameAnno != null && !"".equals(sheetTitleNameAnno.value())) {
                sheetTitleName = sheetTitleNameAnno.value();
            }

            return sheetTitleName;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static String getSheetTitleNameByFieldName(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            return getSheetTitleNameByFieldName(field);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
