package com.polymeric.config.channel;

public interface PoloMethods {

    // 用户注册
    public static String USER_REGISTER = "/user/register";
  
    // KYC国家列表
    public static String KYC_COUNTRY_LIST= "/user/kyc/country/list";
    
    //查询KYC状态
    public static String KYC_STATUS= "/user/kyc/status";
    
    //单个文件上传
    public static String UPLOAD_FILE= "/upload/file";
    
    //提交KYC信息
    public static String KYC_APPLY= "/user/kyc/apply";
    
    //用户卡列表信息
    public static String USER_CARD_LIST= "/bankcard/user/card/list";
    
    //申请卡片
    public static String BANKCARD_APPLY= "/bankcard/apply";
    
    //查询商户可用卡产品列表信息
    public static String MERCHANT_TEMPLATE_LIST= "/bankcard/merchant/template/list";
    
    //银行卡是否可激活
    public static String BANKCARD_CANACTIVE= "/bankcard/canActive";
    
    //银行卡激活
    public static String BANKCARD_ACTIVE= "/bankcard/active";
    
    //查询银行卡余额
    public static String BANKCARD_BALANCE= "/bankcard/balance";
    
    //银行卡充值
    public static String BANKCARD_RECHARGE= "/bankcard/recharge";
    
    //更新银行卡状态
    public static String BANKCARD_UPDATE_STATUS= "/bankcard/update/status";
    
    //注销银行卡
    public static String BANKCARD_CLOSE= "/bankcard/close";
    
    //查询银行卡信息
    public static String BANKCARD_INFO= "/bankcard/info";
    
    //更新银行卡邮箱
    public static String BANKCARD_UPDATE_EMAIL= "/bankcard/update/email";
    
    //查询pin
    public static String BANKCARD_QUERYPIN= "/bankcard/queryPin";
    
    //查询银行卡交易记录
    public static String BANKCARD_TRADE_DETAILS= "/bankcard/trade/details";

}
