package online.dinghuiye.core.entity;

import java.util.Map;

/**
 * Created by Strangeen on 2017/6/26.
 */
public class RowRecord {

    private int rowNo;
    private RowRecordHandleResult result;

    private Map<String, String> excelRecordMap; // excel元数据map<表头名称, 单元格值>
    private Map<Class<?>, Object> pojoRecordMap; // 数据实体map<实体class, 实体对象>

    public RowRecordHandleResult getResult() {
        return result;
    }

    public void setResult(RowRecordHandleResult result) {
        this.result = result;
    }

    public int getRowNo() {
        return rowNo;
    }

    public void setRowNo(int rowNo) {
        this.rowNo = rowNo;
    }

    public Map<String, String> getExcelRecordMap() {
        return excelRecordMap;
    }

    public void setExcelRecordMap(Map<String, String> excelRecordMap) {
        this.excelRecordMap = excelRecordMap;
    }

    public Map<Class<?>, Object> getPojoRecordMap() {
        return pojoRecordMap;
    }

    public void setPojoRecordMap(Map<Class<?>, Object> pojoRecordMap) {
        this.pojoRecordMap = pojoRecordMap;
    }

    /**
     * 写入元数据map
     * @param key 表头名称
     * @param value 单元格值
     * @return map中对应key原有的值，如果没有值则返回null
     */
    public String set(String key, String value) {
        String lastValue = excelRecordMap.get(key);
        excelRecordMap.put(key, value);
        return lastValue;
    }

    public String get(String key) {
        return excelRecordMap.get(key);
    }

    /**
     * 写入实体map
     * @param pojo 实体class名称
     * @param obj 实体对象
     * @return map中对应key原有的值，如果没有值则返回null
     */
    public <T> T set(Class<T> pojo, T obj) {
        T lastValue = (T) pojoRecordMap.get(pojo);
        pojoRecordMap.put(pojo, obj);
        return lastValue;
    }

    public <T> T get(Class<T> pojo) {
        return (T) pojoRecordMap.get(pojo);
    }
}