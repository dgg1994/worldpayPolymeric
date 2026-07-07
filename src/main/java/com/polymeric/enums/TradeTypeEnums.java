package com.polymeric.enums;

/**
 * @category 银行卡交易类型
 */
public enum TradeTypeEnums {
    
    APPLY_CARD(140, "申请卡片","Apply for a card"),
    RECHARGE(150, "卡片充值","Card top-up"),
    CONSUME(170, "卡片消费","Card consumption"),
    REFUND(171, "卡片退款","Card refund"),
    FEE(172, "卡片手续费","Card processing fees"),
    ATM_WITHDRAW(173, "卡片ATM取现","Card ATM cash withdrawal"),
    PRE_AUTHORIZATION(174, "卡预授权","Card pre-authorization"),
    CANCEL_CARD(175, "卡片注销","Card cancellation");
    
    private Integer code;
    private String desc;
    private String enDesc;
    
    TradeTypeEnums(Integer code, String desc, String enDesc) {
        this.code = code;
        this.desc = desc;
        this.enDesc = enDesc;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getDesc() {
        return desc;
    }

	public String getEnDesc() {
		return enDesc;
	}

	public void setEnDesc(String enDesc) {
		this.enDesc = enDesc;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
    
    
    
    
}
