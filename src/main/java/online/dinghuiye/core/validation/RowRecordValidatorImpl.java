package online.dinghuiye.core.validation;

import online.dinghuiye.api.validation.RowRecordValidator;
import online.dinghuiye.core.entity.ResultStatus;
import online.dinghuiye.core.entity.RowRecord;
import online.dinghuiye.core.entity.RowRecordHandleResult;
import online.dinghuiye.core.resolution.torowrecord.RowRecordKit;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Strangeen on 2017/8/3.
 *
 * 实现只考虑单表单pojo的插入操作，TODO 后期重新实现多表一对一关联插入
 * 如果pojo不是拆开的形式，那么可以做到多个pojo同时检测（hibernate validation特性）
 */
public class RowRecordValidatorImpl implements RowRecordValidator {

    private Validator validator;

    public RowRecordValidatorImpl() {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
    }

    @Override
    public void valid(List<RowRecord> rowRecordList) {

        for (RowRecord rowRecord : rowRecordList) {

            // 解析成功的才进行验证
            if (rowRecord.getResult() != null &&
                    rowRecord.getResult().getResult() != ResultStatus.SUCCESS) continue;

            Map<Class<?>, Object> pojoObjMap = rowRecord.getPojoRecordMap();
            for (Map.Entry<Class<?>, Object> pojoObjEntry : pojoObjMap.entrySet()) {
                Set<ConstraintViolation<Object>> validResSet = validator.validate(pojoObjEntry.getValue());
                if (validResSet.size() > 0) {
                    StringBuffer msg = new StringBuffer();
                    Iterator<ConstraintViolation<Object>> i = validResSet.iterator();
                    while (i.hasNext()) {
                        ConstraintViolation<Object> cv = i.next();
                        msg.append(
                                RowRecordKit.getSheetTitleNameByFieldName(
                                        pojoObjEntry.getKey(),
                                        cv.getPropertyPath().iterator().next().getName()))
                                .append(cv.getMessage()).append(";");
                    }
                    rowRecord.setResult(new RowRecordHandleResult(ResultStatus.FAIL, msg.toString()));
                }
            }
        }

    }
}
