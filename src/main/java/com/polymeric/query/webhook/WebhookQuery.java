package com.polymeric.query.webhook;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WebhookQuery {
    
    /************公共参数************/
    @ApiModelProperty(name = "eventId", value = "事件唯一id", required = true, dataType = "String")
    private String eventId;
    
    @ApiModelProperty(name = "eventType", value = "事件类型", required = true, dataType = "String")
    private String eventType;
    
    /****************kyc认证通知回调参数*****************/
    @ApiModelProperty(name = "uid", value = "用户id", required = true, dataType = "String")
    private String uid;
    
    @ApiModelProperty(name = "auditState", value = "kyc审核状态 1认证通过、2未通过", required = true, dataType = "String")
    private String auditState;
    
    @ApiModelProperty(name = "auditRemark", value = "kyc审核备注", required = true, dataType = "String")
    private String auditRemark;
    
    /****************授权3DS通知回调参数*****************/
    @ApiModelProperty(name = "userBankcardId", value = "用户卡id", required = true, dataType = "String")
    private Integer userBankcardId;
    
    @ApiModelProperty(name = "verificationType", value = "授权通知类型:otp:发送验证码 http:通过授权id http请求验证", required = true, dataType = "String")
    private String verificationType;
    
    @ApiModelProperty(name = "otp", value = "验证码", required = true, dataType = "String")
    private String otp;
    
    @ApiModelProperty(name = "authId", value = "授权id", required = true, dataType = "String")
    private String authId;
    
    @ApiModelProperty(name = "transactionAmount", value = "金额", required = true, dataType = "String")
    private String transactionAmount;
    
    @ApiModelProperty(name = "transactionCurrency", value = "币种", required = true, dataType = "String")
    private String transactionCurrency;
    
    @ApiModelProperty(name = "merchantName", value = "商户名称", required = true, dataType = "String")
    private String merchantName;
    
    @ApiModelProperty(name = "oldUserBankcardId", value = "老系统卡id", required = true, dataType = "String")
    private String oldUserBankcardId;
    
    /****************银行卡状态变更通知*****************/
    @ApiModelProperty(name = "cardNo", value = "卡号", required = true, dataType = "String")
    private String cardNo;
    
    @ApiModelProperty(name = "status", value = "状态 cardFreeze 卡片冻结；cardActive 卡片激活；cardClose 卡片关闭", required = true, dataType = "String")
    private String status;
    
    @ApiModelProperty(name = "reason", value = "冻结原因", required = true, dataType = "String")
    private String reason;
    
    @ApiModelProperty(name = "refundAmount", value = "退款金额，卡注销事件才有", required = true, dataType = "String")
    private String refundAmount;
    
    /****************银行卡充值通知*****************/
    @ApiModelProperty(name = "rechargeAmount", value = "充值金额", required = true, dataType = "String")
    private String rechargeAmount;
    
    @ApiModelProperty(name = "orderId", value = "商户请求订单号", required = true, dataType = "String")
    private String orderId;
    
    // ========== 新增：交易回调字段 ==========
    
    @ApiModelProperty(name = "createAt", value = "创建时间戳", dataType = "Long")
    private Long createAt;
    
    @ApiModelProperty(name = "currency", value = "币种", dataType = "String")
    private String currency;
    
    @ApiModelProperty(name = "transaction", value = "交易详细信息", dataType = "Transaction")
    private Transaction transaction;
    
    /**
     * 交易详细信息内部类
     */
    @Data
    public static class Transaction {
        
        @ApiModelProperty(value = "余额")
        private String balance;
        
        @ApiModelProperty(value = "手续费金额")
        private String feeAmount;
        
        @ApiModelProperty(value = "手续费币种")
        private String feeCurrency;
        
        @ApiModelProperty(value = "本地币种")
        private String localCurrency;
        
        @ApiModelProperty(value = "本地币种金额")
        private String localCurrencyAmt;
        
        @ApiModelProperty(value = "商户类别码")
        private String merchantCategoryCode;
        
        @ApiModelProperty(value = "商户名称")
        private String merchantName;
        
        @ApiModelProperty(value = "原始交易ID")
        private String originalTransactionId;
        
        @ApiModelProperty(value = "流水号")
        private String recordNo;
        
        @ApiModelProperty(value = "响应码描述")
        private String respCodeDesc;
        
        @ApiModelProperty(value = "交易币种")
        private String transCurrency;
        
        @ApiModelProperty(value = "交易币种金额")
        private String transCurrencyAmt;
        
        @ApiModelProperty(value = "交易状态")
        private Integer transStatus;
        
        @ApiModelProperty(value = "交易类型")
        private Integer transType;
        
        @ApiModelProperty(value = "交易ID")
        private String transactionId;
    }
}