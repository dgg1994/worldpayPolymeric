package com.polymeric.handler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.polymeric.aop.ApiIdempotent;
import com.polymeric.utils.RedisUtil;

import java.lang.reflect.Method;

public class ApiIdempotentInterceptor implements HandlerInterceptor {

    private RedisUtil redisUtil; // 替换为你的自定义 Redis 工具类

    public ApiIdempotentInterceptor(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            ApiIdempotent apiIdempotent = method.getAnnotation(ApiIdempotent.class);
            if (apiIdempotent != null) {
                String requestId = generateRequestId(request);
                if (isDuplicateRequest(requestId)) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("请勿重复提交数据");
                    return false;
                }
                storeRequestId(requestId, apiIdempotent.time());
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            ApiIdempotent apiIdempotent = method.getAnnotation(ApiIdempotent.class);
            if (apiIdempotent != null) {
                String requestId = generateRequestId(request);
                deleteRequestId(requestId);
            }
        }
    }

    private String generateRequestId(HttpServletRequest request) {
        return request.getRequestURI() + "-" + request.getMethod() + "-" + request.getHeader("User-Agent");
    }

    private boolean isDuplicateRequest(String requestId) {
        return redisUtil.hasKey(requestId); // 使用你的自定义 Redis 工具类的方法来判断键是否存在
    }

    private void storeRequestId(String requestId, long expireMillis) {
    	redisUtil.set(requestId, "true", expireMillis); // 使用你的自定义 Redis 工具类的方法来存储键和值
    }

    private void deleteRequestId(String requestId) {
    	redisUtil.del(requestId); // 使用你的自定义 Redis 工具类的方法来删除键
    }
}
