package com.polymeric.aspect;

import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.polymeric.aop.AccessLimit;
import com.polymeric.base.ResponseBase;
import com.polymeric.constants.Constants;
import com.polymeric.utils.CustomUtils;
import com.polymeric.utils.RedisUtil;

@Aspect
@Component
public class LimitSubmitAspect {
	
	@Autowired
	private RedisUtil redisUtil;

	private static final Logger logger = LoggerFactory.getLogger(LimitSubmitAspect.class);
	
	@Pointcut("@annotation(com.polymeric.aop.AccessLimit)")
	public void ApiLimit() {}
	
	@Before("ApiLimit()")
	public void requestLimit(JoinPoint joinPoint) throws RuntimeException {
		 	ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	        HttpServletRequest request = attributes.getRequest();
	        HttpServletResponse response = attributes.getResponse();
			String ip = request.getLocalAddr();
			String url = request.getRequestURL().toString();
			String key = "req_limit_".concat(ip).concat(url);
			String redisUrl;
			try {
				redisUrl = redisUtil.get(key).toString();
			} catch (Exception e) {
				redisUrl = null;
			}
			int index = 1;
			if (redisUrl == null) {
				redisUtil.set(key, index);
			} else {
				int num = (int) redisUtil.get(key);
				redisUtil.set(key, num + 1);
			}
			int count = (int) redisUtil.get(key);
			// 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            // 获取切入点所在的方法
            Method method = signature.getMethod();
            // 获取操作
            AccessLimit accessLimit = method.getAnnotation(AccessLimit.class);
			if (count > 0) {
				Timer timer = new Timer();
				TimerTask task = new TimerTask() {
					@Override
					public void run() {
						redisUtil.del(key);
					}
				};
				timer.schedule(task,accessLimit.time());
			}
			if (count > accessLimit.count()) {
				logger.info("HTTP请求超出设定的限制");
				ResponseBase base = new ResponseBase();
				base.setCode(Constants.HTTP_RES_CODE_500);
				base.setData(null);
				base.setMsg("操作过于频繁，稍后再试！！！");
				CustomUtils.sendJsonMessage(response, base);
			}
	}

}
