package com.polymeric.constants;

public final class ClientType {
    
    // 私有构造，防止实例化
    private ClientType() {}
    
    // 请求头名称
    public static final String HEADER_CLIENT = "appType";
    
    // 版本值常量
    public static final String CLIENT_ANDROID = "android";
    public static final String CLIENT_IOS = "ios";
    public static final String CLIENT_WEB = "web";
    public static final String CLIENT_H5 = "h5";
    public static final String CLIENT_MINI_PROGRAM = "miniProgram";
    
    // 构建header表达式（静态方法，但注解中不能用）
    public static String header(String version) {
        return HEADER_CLIENT + "=" + version;
    }
    
    // 直接定义常量表达式
    public static final String HEADER_CLIENT_ANDROID = HEADER_CLIENT + "=" + CLIENT_ANDROID;
    public static final String HEADER_CLIENT_IOS = HEADER_CLIENT + "=" + CLIENT_IOS;
    public static final String HEADER_CLIENT_WEB = HEADER_CLIENT + "=" + CLIENT_WEB;
    public static final String HEADER_CLIENT_H5 = HEADER_CLIENT + "=" + CLIENT_H5;
    public static final String HEADER_CLIENT_MINI = HEADER_CLIENT + "=" + CLIENT_MINI_PROGRAM;
}