package com.polymeric.aspect;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.polymeric.base.ResponseBase;

import lombok.extern.slf4j.Slf4j;

/**
 * @category 日志统一管理
 * @author csz
 *
 */
@Aspect
@Component
@Slf4j
public class WebLogAspect {
	

    @Pointcut("execution(public * com.polymeric.service..*.*(..))")
    public void webLog() {}

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(attributes != null) {
        	 HttpServletRequest request = attributes.getRequest();
             log.info("##################### 请求开始 ####################");
             log.info("URL : " + request.getRequestURL().toString());
             log.info("HTTP_METHOD : " + request.getMethod());
             log.info("IP : " + request.getRemoteAddr());
             Object[] args = joinPoint.getArgs();
             if (args != null && args.length > 0 && args[0] != null) {
                 try {
                     String parameterJson = JSON.toJSONString(args[0]);
                     log.info("PARAMETER：" + request.getRequestURL() + "-[" + parameterJson + "]");
                 } catch (JSONException e) {
                     log.error("PARAMETER：" + request.getRequestURL() + "-[]");
                 }
             }

             Enumeration<String> enu = request.getParameterNames();
             while (enu.hasMoreElements()) {
                 String name = enu.nextElement();
                 log.info("name:{" + name + "},value:{" + request.getParameter(name) + "}");
             }

        }
       
    }
    
    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(JoinPoint point, Object ret) throws Throwable {
        
        String type = point.getSignature().toLongString().split(" ")[1];
        if (type != null && !"void".equals(type) && !"String".equals(type)) {
            try {
                if (ret != null) {
                    // 判断 ret 是否是字符串类型
                    if (ret instanceof String) {
                        log.info("RESPONSE (String) : " + ret);
                    } 
                    // ✅ 增加对 Boolean 类型的判断
                    else if (ret instanceof Boolean) {
                        log.info("RESPONSE (Boolean) : " + ret);
                    }
                    // ✅ 增加对基本类型的判断
                    else if (ret instanceof Number) {
                        log.info("RESPONSE (Number) : " + ret);
                    }
                    else {
                        // 只有当 ret 是对象时才尝试解析为 ResponseBase
                        try {
                            ResponseBase base = JSONObject.parseObject(JSON.toJSONString(ret), ResponseBase.class);
                            log.info("RESPONSE11 : " + point.getSignature().toString().substring(point.getSignature().toString().indexOf(" ")) + "-" + base.getMsg());
                        } catch (Exception e) {
                            // 如果不是 ResponseBase 对象，直接打印
                            log.info("RESPONSE : " + JSON.toJSONString(ret));
                        }
                    }
                }
            } catch (JSONException e) {
                log.error("JSON解析错误", e);
            }
        }
        
        log.info("##################### 请求结束 ####################");
    }

}
