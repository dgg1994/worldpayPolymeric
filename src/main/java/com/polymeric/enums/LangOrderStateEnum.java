package com.polymeric.enums;

public enum LangOrderStateEnum {
	PULL_ERROR(0, "拉取失败"), 
	PENDING_PAYMENT(1, "待支付"),
	ALREADY_PAYMENT(2, "已支付"),
	TIME_OUT(3, "超时/过期");

	private Integer index;

	private String name;

	private LangOrderStateEnum(Integer index, String name) {
		this.index = index;
		this.name = name;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
    public static String getName(int i) {
    	LangOrderStateEnum[] orderStatusEnums = values();
        for (LangOrderStateEnum orderStatusEnum : orderStatusEnums) {
            if (orderStatusEnum.getIndex().equals(i)) {
                return orderStatusEnum.getName();
            }
        }
        return null;
    }

}
