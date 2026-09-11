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

    // 设置pin
    public static String BANKCARD_SETPIN = "/bankcard/setPin";
    
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
    
    //3ds授权通过
    public static String BANKCARD_3DS_APPROVE= "/bankcard/3ds/approve";
    
    //3ds授权拒绝
    public static String BANKCARD_3DS_REJECT= "/bankcard/3ds/reject";
    
    //查询邮寄地区列表
    public static String DELIVERY_REGION= "/delivery/region";
    
    //添加邮寄地址
    public static String DELIVERY_ADDRESS_ADD= "/delivery/address/add";
    
    //更新邮寄地址
    public static String DELIVERY_ADDRESS_PDATE= "/delivery/address/update";
    
    //查询邮寄信息
    public static String DELIVERY_INFO= "/delivery/info";
    
    //查询商户资产
    public static String MERCHANT_BALANCE= "/merchant/balance";

    //模拟3ds
    public static String BANKCARD_SIMULATE_3DS= "/bankcard/simulate/3ds";
    
    //模拟交易
    public static String BANKCARD_SIMULATE_AUTH= "/bankcard/simulate/auth";
    
    //模拟结算
    public static String BANKCARD_SIMULATE_CLEAR= "/bankcard/simulate/clear";
    
    //模拟退单
    public static String BANKCARD_SIMULATE_REVERSAL= "/bankcard/simulate/reversal";
    
    
}
