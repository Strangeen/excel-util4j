package online.dinghuiye.core.entity;

/**
 * 每行的解析和检查结果
 */
public class RowRecordHandleResult {

	private ResultStatus result;
	// 多条消息以";"分隔
	private String msg;

	public RowRecordHandleResult(ResultStatus result, String msg) {
		this.result = result;
		this.msg = msg;
	}

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

	@Override
	public String toString() {
		return "RowRecordHandleResult{" +
				"result=" + result +
				", msg='" + msg + '\'' +
				'}';
	}
}
