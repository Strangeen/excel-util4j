package online.dinghuiye.core.validation;

import online.dinghuiye.api.validation.RowRecordValidator;
import online.dinghuiye.api.entity.ResultStatus;
import online.dinghuiye.api.entity.RowRecord;
import online.dinghuiye.api.entity.RowRecordHandleResult;
import online.dinghuiye.core.resolution.torowrecord.RowRecordKit;

import javax.validation.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>hibernate validatior实现验证</p>
 *
 * @author Strangeen
 * on 2017/8/3
 */
public class RowRecordValidatorImpl implements RowRecordValidator {

    private Validator validator;

    public RowRecordValidatorImpl() {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
    }

    @Override
    public boolean valid(List<RowRecord> rowRecordList) {

        boolean allSuccess = true;
        for (RowRecord rowRecord : rowRecordList) {
            if (!valid(rowRecord)) allSuccess = false;
        }
        return allSuccess;
    }


    @Override
    public boolean valid(RowRecord rowRecord) {

        // 解析成功的才进行验证
        if (rowRecord.getResult() != null &&
                rowRecord.getResult().getResult() != ResultStatus.SUCCESS)
            return false; // 解析不成功，不进行验证，标记为不成功

        Map<Class<?>, Object> pojoObjMap = rowRecord.getPojoRecordMap();
        for (Map.Entry<Class<?>, Object> pojoObjEntry : pojoObjMap.entrySet()) {
            Set<ConstraintViolation<Object>> validResSet = validator.validate(pojoObjEntry.getValue());
            if (validResSet.size() > 0) {
                StringBuilder msg = new StringBuilder();
                for (ConstraintViolation<Object> cv : validResSet) {
                    msg.append(
                            RowRecordKit.getSheetTitleNameByFieldName(
                                    ValidateKit.getConstraintViolationField(pojoObjEntry.getKey(), cv))
                    )
                            .append(cv.getMessage()).append(";");
                }
                rowRecord.getResult().setResult(ResultStatus.FAIL).setMsg(msg.toString());
            }
        }
        return rowRecord.getResult().getResult() == ResultStatus.SUCCESS;
    }
}
