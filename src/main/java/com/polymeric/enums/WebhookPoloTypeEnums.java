package com.polymeric.enums;

/**
 * @category polo 回调类型
 * @author Hlin
 *
 */
public enum WebhookPoloTypeEnums {
    
	KYC_STATUS_CHANGE(1, "kycStatusChange", "KYC状态变更通知"),
	AUTHORIZATION_3DS(2, "3ds", "授权3DS通知"),
	CARD_STATUS_CHANGE(3, "cardStatusChange", "银行卡状态变更通知"),
	CARD_RECHARGE_RESULT(4, "cardRechargeResult", "充值结果通知"),
	TRANSACTION_CREATED(5, "transactionCreated", "银行卡交易通知"),
	MERCHANT_RECHARGE(6, "merchantRecharge", "商户充值通知");
    
    private Integer index;
    private String code;
    private String name;
    
    private WebhookPoloTypeEnums(Integer index, String code, String name) {
        this.index = index;
        this.code = code;
        this.name = name;
    }
    
    public Integer getIndex() {
        return index;
    }
    
    public void setIndex(Integer index) {
        this.index = index;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public static String getName(int i) {
    	WebhookPoloTypeEnums[] webhookPoloTypeEnums = values();
        for (WebhookPoloTypeEnums typeEnums : webhookPoloTypeEnums) {
            if (typeEnums.getIndex().equals(i)) {
                return typeEnums.getName();
            }
        }
        return null;
    }
}