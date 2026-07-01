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

}
