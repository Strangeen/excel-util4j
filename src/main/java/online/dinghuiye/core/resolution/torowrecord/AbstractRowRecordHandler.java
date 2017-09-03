package online.dinghuiye.core.resolution.torowrecord;

import online.dinghuiye.api.entity.Process;
import online.dinghuiye.api.entity.ProcessNode;
import online.dinghuiye.api.entity.RowRecord;
import online.dinghuiye.api.resolution.RowRecordHandler;

import java.util.List;
import java.util.Map;

/**
 * {@link RowRecord}解析器抽象类
 *
 * @author Strangeen on 2017/09/03
 * @version 2.1.0
 */
public abstract class AbstractRowRecordHandler implements RowRecordHandler {

    @Override
    public boolean handle(List<RowRecord> rowRecordList, Process process, Class<?>... pojos) {

        if (process != null)
            process.setNode(ProcessNode.RESOLUTION);
        boolean allSuccess = true;
        for (RowRecord rowRecord : rowRecordList) {
            if (!handle(rowRecord, pojos)) allSuccess = false;
            // 更新进度
            if (process != null)
                process.updateProcess(1);
        }
        return allSuccess;
    }

    @Override
    public boolean handle(RowRecord rowRecord, Class<?>... pojos) {
        return pojoHandle(rowRecord, pojos);
    }


    @Override
    public RowRecord rowRecordCreate(Map<String, Object> excelRowData, Integer row) {
        return RowRecordKit.createRowRecord(excelRowData, row);
    }

    protected abstract boolean pojoHandle(RowRecord rowRecord, Class<?>... pojo);
}
