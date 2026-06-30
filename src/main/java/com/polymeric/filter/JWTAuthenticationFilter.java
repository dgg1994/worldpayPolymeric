package com.polymeric.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.polymeric.base.JsonData;
import com.polymeric.config.heard.AppVersionContext;
import com.polymeric.config.heard.CurrencyContext;
import com.polymeric.config.heard.DeviceTypeContext;
import com.polymeric.config.heard.LanguageContext;
import com.polymeric.constants.Constants;
import com.polymeric.enums.CurrencyEnums;
import com.polymeric.enums.LanguageEnums;
import com.polymeric.security.CustomAuthenticationProvider;
import com.polymeric.security.GrantedAuthorityImpl;
import com.polymeric.utils.CustomUtils;
import com.polymeric.utils.RedisUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * JWT身份验证过滤器 token的校验 该类继承自BasicAuthenticationFilter 在doFilterInternal方法中：
 * 从http头的Authorization 项读取token数据， 然后用Jwts包提供的方法校验token的合法性。
 * 如果校验通过，就认为这是一个取得授权的合法请求 注意：注册等排除了的url也会进入doFilterInternal方法中！！
 *
 */
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

	private RedisUtil redisUtil;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, RedisUtil redisUtil) {
		super(authenticationManager);
		this.redisUtil = redisUtil;
	}

	/**
	 * 所有请求都会被拦截验证token
	 * 
	 * @param request  请求
	 * @param response 响应
	 * @param chain    过滤链
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			// 读取请求头 language
	        String language = request.getHeader("x-mergpay-language");
	        List<String> languageList = LanguageEnums.getAll();
	        if (language == null || language.trim().isEmpty()) {
	            language = LanguageEnums.ZH_CN.getName(); // 默认语言
	        } else if (!languageList.contains(language)) {
	            // 如果语言不在支持列表中，也使用默认语言
	            language = LanguageEnums.ZH_CN.getName();
	        }
	        LanguageContext.setLanguage(language);
	        // 读取请求头 currency（币种）
	        String currency = request.getHeader("x-mergpay-currency");
	        if (currency == null || currency.trim().isEmpty()) {
	        	currency = CurrencyEnums.USD.getName(); // 默认语言
	        }
	        CurrencyContext.setCurrency(currency);
	        //读取请求头 deviceType（设备类型）
	        String deviceType = request.getHeader("x-mergpay-devicetype");
	        DeviceTypeContext.setDeviceType(deviceType);
	        //读取请求头 appVersion（应用版本号）
	        String appVersion = request.getHeader("x-mergpay-version");
	        AppVersionContext.setAppVersion(appVersion);
			UsernamePasswordAuthenticationToken authentication = null;
			try {
				authentication = getAuthentication(request);
			} catch (Exception e) {
				CustomUtils.sendJsonMessage(response, JsonData.buildError("token不合法"));
			}
			String header = request.getHeader(Constants.HEADER_AUTH);
			String requestURI = request.getRequestURI();
			// 没有token
			if (header == null || !header.startsWith(Constants.AUTH_HEADER_START_WITH)
					|| requestURI.equals("/oauth/login")) {
				// 返回403
				chain.doFilter(request, response);
				return;
			}
			// 有token，校验token
			if (authentication == null) {
				CustomUtils.sendJsonMessage(response, JsonData.buildError("登录已过期"));
			} else {
				String key = authentication.getName();
				String redisKey = Constants.APP_PACKAGE_NAME+key;
				if (redisUtil.get(redisKey) == null || redisUtil.get(redisKey).toString().length() < 1) {
					chain.doFilter(request, response);
					return;
				} else {
					if (redisUtil.get(redisKey).equals(header)) {
						SecurityContextHolder.getContext().setAuthentication(authentication);
						chain.doFilter(request, response);
					} else {
						CustomUtils.sendJsonMessage(response, JsonData.buildError("登录已过期"));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
//			CustomUtils.sendJsonMessage(response, JsonData.buildError("token不合法"));
			CustomUtils.sendJsonMessage(response, JsonData.Error("系统异常稍后再试"));
		}finally {
	        // 防止内存泄露，必须清理
	        LanguageContext.clear();
	    }

	}

	/**
	 * 解析请求对象中的token
	 *
	 * @param request  请求对象
	 * @return 返回 authentication 用户 名称 密码 身份验证令牌
	 */
	@SuppressWarnings("deprecation")
	public static UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(Constants.HEADER_AUTH);
		if (token != null) {
			try {
				// token格式乱传的会直接报错500，验证为合法的token则会返回用户信息
				String userAndRole = Jwts.parser()
						// 设置密钥
						.setSigningKey(Constants.SIGNING_KEY)
						// 替换前缀
						.parseClaimsJws(token.replace(Constants.AUTH_HEADER_START_WITH, "")).getBody().getSubject();
				// token校验成功，返回用户名和权限信息，并跳转到controller请求路径
				String user = "";
				List<GrantedAuthorityImpl> menuCodeList = new ArrayList<>();
				if (userAndRole != null) {
					String[] userAndMenus = userAndRole.split(",");
					int length = userAndMenus.length;
					if (length > 0) {
						user = userAndMenus[0];
					}
					// 权限字符存在与token中时启用
//                    for (int i = 1; i < length; i++) {
//                        menuCodeList.add(new GrantedAuthorityImpl(userAndMenus[i]));
//                    }
					// 权限字符未存储在token中根据账号获取（控制接口访问权限）
					menuCodeList = CustomAuthenticationProvider.getUserAuthorities(user);
					// 最后一个参数是权限，一定要带入，不然方法验证权限不得行！！！可以传emptyList
					return new UsernamePasswordAuthenticationToken(user, null, menuCodeList);
				}
				return null;
			} catch (ExpiredJwtException e) {
				throw new RuntimeException("登录已过期，请重新登录");
			}
		}
		return null;
	}


}
