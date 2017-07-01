package online.dinghuiye.core.entity;

public enum ResultStatus {

	SUCCESS("成功"), FAIL("失败"), NOATTEPT("未处理");
	
	private String text;
	
	private ResultStatus(String text) {
		this.text = text;
	}
	
	public String getText() {
		return this.text;
	}
}
