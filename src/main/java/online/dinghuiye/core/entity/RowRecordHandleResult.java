package online.dinghuiye.core.entity;

/**
 * 每行的解析和检查结果
 */
public class RowRecordHandleResult {

	private ResultStatus result;
	private String msg;

	public ResultStatus getResult() {
		return result;
	}

	public void setResult(ResultStatus result) {
		this.result = result;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
