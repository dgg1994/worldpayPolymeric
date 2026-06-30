package com.polymeric.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.polymeric.utils.RedisUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;

@Component
@Order(1) // 确保优先执行
public class GlobalRateLimitFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(GlobalRateLimitFilter.class);

    @Autowired
    private RedisUtil redisUtil;

    // 每秒最大请求次数
    private static final int MAX_REQUESTS_PER_SECOND = 3;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 获取客户端IP和请求URI
        String ip = getClientIp(request);
        String uri = request.getRequestURI();

//        logger.info("请求IP：{}，请求URI：{}", ip, uri);

        // 构造 Redis Key: IP + URI + 当前秒
        String key = String.format("rate_limit:%s:%s", ip, uri);
        long secondKey = Instant.now().getEpochSecond();
        String redisKey = key + ":" + secondKey;

        // 自增计数
        Long count = redisUtil.incr(redisKey, 1);
        if (count == 1) {
            redisUtil.expire(redisKey, 2); // 2秒后过期
        }

        // 超过限制返回 429
        if (count > MAX_REQUESTS_PER_SECOND) {
            logger.warn("限流触发 - IP: {}, URI: {}, 当前计数: {}", ip, uri, count);
            response.setStatus(429);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"rtncode\":429,\"msg\":\"操作太快，请稍后再试\"}");
            return;
        }

        // 继续执行过滤器链
        filterChain.doFilter(request, response);
    }

    /**
     * 获取客户端IP
     */
    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        return (xfHeader == null) ? request.getRemoteAddr() : xfHeader.split(",")[0];
    }
}
