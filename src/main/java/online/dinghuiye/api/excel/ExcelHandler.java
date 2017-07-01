package online.dinghuiye.api.excel;

import online.dinghuiye.core.entity.RowRecord;

/**
 * Created by Strangeen on 2017/6/26.
 */
public interface ExcelHandler {

    //List<RowRecord> handle(AbstractExcel excel);
    RowRecord handle(AbstractExcel excel, int rowNo);
}
