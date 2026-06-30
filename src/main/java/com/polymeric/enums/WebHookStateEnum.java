package com.polymeric.enums;

public enum WebHookStateEnum {
	PENDING(0, "待回调"),
	SUCCESS(1, "回调成功"),
	FAILED(2, "回调失败"),
	MAX_RETRY(3, "达到最大重试次数"),
	ORDER_REPAIR(4, "补单完成");

	private final Integer code;
	private final String desc;

	WebHookStateEnum(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public Integer getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
	
	public static String getName(int i) {
		WebHookStateEnum[] typeEnums = values();
		for (WebHookStateEnum typeEnum : typeEnums) {
			if (typeEnum.getCode().equals(i)) {
				return typeEnum.getDesc();
			}
		}
		return null;
	}
}
