package com.polymeric.enums;

public enum ErrorCodeEnum {
    TIMESTAMP_ERROR(2006, "请求超时"),
    NONCE_ERROR(2007, "请勿重复提交"),
    SIGN_ERROR(2000, "验签失败！！！"),
    APPID_ERROR(2001, "AppId不存在！！！"),
    APPID_STATE_ERROR(2003, "AppId不存在！！！"),
    SECRET_KEY_ERROR(2002, "SecretKey错误！！！"),
    IP_ERROR(2100, "IP地址没有访问权限！！！"),
    UID_ERROR(3001, "uid不正确"),
    UID_NULL(3002, "请添加uid请求头参数"),
    CHANNEL_NULL(4002, "渠道信息未配置！！！"),
    CHANNEL_STATUS_ERROR(4003, "渠道状态异常！！！");

    
    private final Integer code;

    private final String message;

    ErrorCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
