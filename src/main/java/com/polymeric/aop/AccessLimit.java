package com.polymeric.aop;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.Order;

/**
 * 在需要保证 接口防刷限流 的Controller的方法上使用此注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
//最高优先级
@Order(1)
public @interface AccessLimit {

	/**
	 *
	 * 允许访问的次数，默认值MAX_VALUE
	 */
	int count() default Integer.MAX_VALUE;

	/**
	 *
	 * 时间段，单位为毫秒，默认值一分钟
	 */
	long time() default 5000;


}
