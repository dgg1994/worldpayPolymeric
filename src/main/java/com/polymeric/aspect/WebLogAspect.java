package com.polymeric.aspect;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            log.info("##################### 请求开始 ####################");
            log.info("URL : {}", request.getRequestURL().toString());
            log.info("HTTP_METHOD : {}", request.getMethod());
            log.info("IP : {}", request.getRemoteAddr());
            
            // 记录请求参数
            Object[] args = joinPoint.getArgs();
            if (args != null && args.length > 0) {
                for (int i = 0; i < args.length; i++) {
                    Object arg = args[i];
                    if (arg != null) {
                        // 跳过 HttpServletRequest 和 HttpServletResponse 等类型
                        if (arg instanceof HttpServletRequest || arg instanceof HttpServletResponse) {
                            continue;
                        }
                        try {
                            // 限制字符串长度，避免日志过大
                            String parameterJson = JSON.toJSONString(arg);
                            if (parameterJson.length() > 10000) {
                                parameterJson = parameterJson.substring(0, 10000) + "...(truncated)";
                            }
                            log.info("PARAMETER[{}] : {}", i, parameterJson);
                        } catch (JSONException e) {
                            log.warn("PARAMETER[{}] : 无法序列化 (类型: {})", i, arg.getClass().getSimpleName());
                        }
                    }
                }
            }

            // 记录请求参数（GET 请求的 QueryString）
            Enumeration<String> enu = request.getParameterNames();
            while (enu.hasMoreElements()) {
                String name = enu.nextElement();
                log.info("QueryParam : {} = {}", name, request.getParameter(name));
            }
        }
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(JoinPoint point, Object ret) throws Throwable {
        // 获取返回类型
        String returnType = point.getSignature().toLongString().split(" ")[1];
        
        if (ret != null) {
            try {
                // 处理基本类型
                if (ret instanceof String) {
                    log.info("RESPONSE (String) : {}", ret);
                } else if (ret instanceof Boolean) {
                    log.info("RESPONSE (Boolean) : {}", ret);
                } else if (ret instanceof Number) {
                    log.info("RESPONSE (Number) : {}", ret);
                } else {
                    // 尝试解析为 ResponseBase
                    try {
                        ResponseBase base = JSONObject.parseObject(JSON.toJSONString(ret), ResponseBase.class);
                        if (base != null && base.getMsg() != null) {
                            String methodName = point.getSignature().getName();
                            log.info("RESPONSE : {}.{} - {}", 
                                point.getSignature().getDeclaringTypeName(),
                                methodName,
                                base.getMsg());
                        } else {
                            log.info("RESPONSE : {}", JSON.toJSONString(ret));
                        }
                    } catch (Exception e) {
                        // 如果不是 ResponseBase，直接打印
                        String jsonStr = JSON.toJSONString(ret);
                        if (jsonStr.length() > 10000) {
                            jsonStr = jsonStr.substring(0, 10000) + "...(truncated)";
                        }
                        log.info("RESPONSE : {}", jsonStr);
                    }
                }
            } catch (JSONException e) {
                log.error("JSON解析错误", e);
                log.info("RESPONSE : {}", ret.toString());
            }
        } else {
            log.info("RESPONSE : null");
        }

        log.info("##################### 请求结束 ####################");
    }
}