package online.dinghuiye.api.validation;

import online.dinghuiye.core.entity.RowRecord;

import java.util.List;

/**
 * Created by Strangeen on 2017/8/3.
 *
 * 对rowRecord进行遍历，并检查，不通过改变rowRecord的状态
 */
public interface RowRecordValidator {

    void valid(List<RowRecord> rowRecordList);

}
