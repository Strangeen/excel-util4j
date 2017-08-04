package online.dinghuiye.api.resolution.torowrecord;

import online.dinghuiye.core.entity.RowRecord;

import java.util.List;
import java.util.Map;

/**
 * Created by Strangeen on 2017/6/27.
 */
public interface RowRecordHandler {

    /**
     * 将excel数据转换为rowRocord
     * @param excelRowDataList excel数据
     * @param pojos 数据对应的pojo
     * @return rowRecord的list
     */
    List<RowRecord> handle(List<Map<String, String>> excelRowDataList, Class<?>... pojos);

}
