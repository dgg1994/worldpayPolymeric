package com.polymeric.aop;

import java.lang.annotation.*;

/**
 * 系统日志注解
 * @author HL
 */
@Documented//文档生成时，该注解将被包含在javadoc中，可去掉
@Target(ElementType.METHOD)//目标是方法
@Retention(RetentionPolicy.RUNTIME)//注解会在class中存在，运行时可通过反射获取
public @interface SysLogAnnotation {
    // 操作模块
    String module() default "";
    // 操作类型
    String type() default "";
    // 操作说明
    String remark() default "";
}
