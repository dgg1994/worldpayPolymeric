package com.polymeric.enums;


/**
 * 订单类型
 * 
 * @author system
 * @date 2026-06-16
 */
public enum OrderTypeEnum {

    OPEN_CARD(1, "开卡",2),
    CARD_TOP_UP(2, "银行卡充值",2),
    BALANCE_TOP_UP(3, "余额充值",1);

    private final Integer code;
    
    private final String name;
    
    private final Integer lable;

    OrderTypeEnum(Integer code, String name,Integer lable) {
        this.code = code;
        this.name = name;
        this.lable = lable;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

	public Integer getLable() {
		return lable;
	}
    
}