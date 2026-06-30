package com.polymeric.enums;

public enum ErrorCodeEnum {
    TIMESTAMP_ERROR(2006, "订单超时，请重新提交"),
    NONCE_ERROR(2007, "订单已存在，请勿重复提交"),
    SIGN_ERROR(2000, "验签失败！！！"),
    APPID_ERROR(2001, "AppId不存在！！！"),
    APPID_STATE_ERROR(2003, "AppId不存在！！！"),
    SECRET_KEY_ERROR(2002, "SecretKey错误！！！"),
    IP_ERROR(2100, "IP地址没有访问权限！！！"),
    PRODUCT_INFO_NULL(3000, "此产品信息不存在！！！"),
    CHANNEL_AISLE_NULL(4000, "没有匹配通道可用！！！"),
    CHANNEL_AISLE_STATUS_ERROR(4001, "通道状态异常！！！"),
    CHANNEL_API_NULL(4100, "API接口未配置！！！"),
    CHANNEL_API_STATUS_ERROR(4101, "API接口状态异常！！！"),
    CHANNEL_NULL(4002, "渠道信息未配置！！！"),
    CHANNEL_STATUS_ERROR(4003, "渠道状态异常！！！"),
    ORDERNUM_NULL(5001, "订单号错误！！！"),
    ORDER_NULL(5002, "订单不存在！！！");

    
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
