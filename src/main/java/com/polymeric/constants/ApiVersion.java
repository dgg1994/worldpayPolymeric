package com.polymeric.constants;

public final class ApiVersion {
    
    // 私有构造，防止实例化
    private ApiVersion() {}
    
    // 请求头名称
    public static final String HEADER_NAME = "apiVersion";
    
    // 版本值常量
    public static final String V1_0 = "v1.0";
    public static final String V2_0 = "v2.0";
    public static final String V3_0 = "v3.0";
    
    // 构建header表达式（静态方法，但注解中不能用）
    public static String header(String version) {
        return HEADER_NAME + "=" + version;
    }
    
    // 直接定义常量表达式
    public static final String HEADER_V1_0 = HEADER_NAME + "=" + V1_0;
    public static final String HEADER_V2_0 = HEADER_NAME + "=" + V2_0;
    public static final String HEADER_V3_0 = HEADER_NAME + "=" + V3_0;
}