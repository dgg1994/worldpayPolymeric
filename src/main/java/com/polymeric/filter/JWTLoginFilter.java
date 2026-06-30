package com.polymeric.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polymeric.base.JsonData;
import com.polymeric.constants.Constants;
import com.polymeric.dao.system.SysUserDao;
import com.polymeric.entity.system.SysUserEntity;
import com.polymeric.enums.UserStateEnums;
import com.polymeric.exception.BaseException;
import com.polymeric.utils.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;


/**
 * jwt登录拦截器 登录controller方法不用自己写，直接访问/login就行
 * 该类继承自UsernamePasswordAuthenticationFilter，重写了其中的2个方法
 * 验证用户名密码正确后，生成一个token，并将token返回给客户端 attemptAuthentication ：接收并解析用户凭证。
 * successfulAuthentication ：用户成功登录后，这个方法会被调用，我们在这个方法里生成token。
 *
 */
public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {
	
	@Autowired
	private GoogleAuthenticatorUtil googleAuthenticator;
	
	private AuthenticationManager authenticationManager;

	private RedisUtil redisUtil;

	private SysUserDao sysUserDao;
	
	private Boolean googleLimit;
	
	

	public JWTLoginFilter(AuthenticationManager authenticationManager, RedisUtil redisUtil,
			SysUserDao sysUserDao, Boolean googleLimit) {
		this.authenticationManager = authenticationManager;
		this.redisUtil = redisUtil;
		this.sysUserDao = sysUserDao;
	    this.googleLimit = googleLimit;
	}

	/**
	 * 处理登录请求，校验用户名和密码
	 * 
	 * @param req 请求
	 * @param res 响应
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException {
		try {
			 // 获取客户端 IP
//	        String ipAddress = ipUtil.getClientIp(req);
//			boolean isAllowed = true;
//			// 查询 IP 是否在白名单
//			IpWhitelistEntity whitelistEntity = ipWhitelistDao.findIp(ipAddress);
//			if (!ipLimit || whitelistEntity != null || ipUtil.isLocalIp(ipAddress)) {
//				isAllowed = true;
//			}
			boolean isAllowed = true;
			if(isAllowed) {
				// 请求头反序列化获得User对象
				SysUserEntity sysUserLoginDTO = new ObjectMapper().readValue(req.getInputStream(), SysUserEntity.class);
				if (sysUserLoginDTO.getGoogleCode() != null) {
					redisUtil.set(Constants.GOOGLE_COCE, sysUserLoginDTO.getGoogleCode());
					return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
							sysUserLoginDTO.getUsername(), sysUserLoginDTO.getPassword(), new ArrayList<>()));
				} else {
					CustomUtils.sendJsonMessage(res, JsonData.Error("请输入谷歌验证码"));
					return null;
				}
			}else {
				CustomUtils.sendJsonMessage(res, JsonData.Error("对不起，你的IP地址没有访问权限"));
				return null;
			}
		} catch (IOException e) {
			throw new BaseException(e);
		}
	}

	/**
	 * 登录请求后，CustomAuthenticationProvider.authenticate身份验证通过会生成Authentication令牌
	 * 我们在这个方法里对令牌生成token，并设置在响应头中
	 * 
	 * @param req   请求
	 * @param res   响应
	 * @param chain 过滤链
	 * @param auth  身份认证
	 */
	@SuppressWarnings({ "static-access", "deprecation"})
    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        String code = redisUtil.get(Constants.GOOGLE_COCE).toString();

        // 获取权限主题
        String subject = auth.getName();
        SysUserEntity entity = sysUserDao.findByAcctiveState(subject, UserStateEnums.NORMAL.getIndex());
        if (entity.getGoogleSecretkey() != null && entity.getGoogleSecretkey().length() > 0) {
            Boolean temp = !googleLimit ? true : googleAuthenticator.verifyCode(entity.getGoogleSecretkey(), Integer.parseInt(code));
            if (temp) {
                // subject中存入用户名和角色权限
                String token = Jwts.builder()
                        // 设置主题
                        .setSubject(subject)
                        // 设置到期时间
                        .setExpiration(new Date(System.currentTimeMillis() + Constants.REDIS_EXPIRE_TIME))
                        // 选择 加密算法和私钥
                        .signWith(SignatureAlgorithm.HS512, Constants.SIGNING_KEY).compact();
                // 登录用户token存入redis
                if (subject.indexOf(",") == -1) {
                    redisUtil.set(Constants.APP_PACKAGE_NAME + subject, 
                    		Constants.AUTH_HEADER_START_WITH + token, 
                    		Constants.REDIS_EXPIRE_TIME);
                } else {
                    redisUtil.set(Constants.APP_PACKAGE_NAME + subject.substring(0, subject.indexOf(",")),
                            Constants.AUTH_HEADER_START_WITH + token, 
                            Constants.REDIS_EXPIRE_TIME);
                }

                // token返回到请求头中，前端在请求头中获取
                res.setHeader(Constants.HEADER_ACCESS, Constants.HEADER_AUTH);
                res.addHeader(Constants.HEADER_AUTH, Constants.AUTH_HEADER_START_WITH + token);

                CustomUtils.sendJsonMessage(res, JsonData.buildSuccess("登录成功"));
            } else {
                CustomUtils.sendJsonMessage(res, JsonData.Error("谷歌验证失败"));
            }
        } else {
            CustomUtils.sendJsonMessage(res, JsonData.Error("谷歌验证失败"));
        }

    }
	

}
