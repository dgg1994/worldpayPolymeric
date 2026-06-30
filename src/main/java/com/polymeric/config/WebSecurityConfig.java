package com.polymeric.config;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.polymeric.dao.system.SysUserDao;
import com.polymeric.filter.GlobalRateLimitFilter;
import com.polymeric.filter.JWTAuthenticationFilter;
import com.polymeric.filter.JWTLoginFilter;
import com.polymeric.handler.AuthenticationLogout;
import com.polymeric.handler.TokenAccessDeniedHandler;
import com.polymeric.handler.TokenAuthenticationEntryPoint;
import com.polymeric.security.CustomAuthenticationProvider;
import com.polymeric.utils.RedisUtil;

/**
 * SpringSecurity配置类
 * 通过SpringSecurity的配置，将JWTLoginFilter，JWTAuthenticationFilter组合在一起
 * securedEnabled = true 注解控制方法权限
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

   //身份认证时需要使用，注入我们实现了这个接口的类
   private UserDetailsService userDetailsService;

   @Autowired
   RedisUtil redisUtil;

   @Autowired
   AuthenticationLogout authenticationLogout;

   @Autowired
   private SysUserDao sysUserMapper;
   
   @Autowired
   private GlobalRateLimitFilter globalRateLimitFilter;

   private HttpServletResponse response;
   
   @Value("${dscoins.login.ipLimit.enable}")
   private Boolean ipLimit;
   
   @Value("${dscoins.login.googleLimit.enable}")
   private Boolean googleLimit;

    public WebSecurityConfig(UserDetailsService userDetailsService, SysUserDao sysUserMapper,HttpServletResponse response) {
        this.userDetailsService = userDetailsService;
        this.sysUserMapper = sysUserMapper;
        this.response = response;
    }

    /**
     * 设置HTTP验证规则
     * @param http 描述此参数
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.cors().and().csrf().disable()
		        //会话创建策略：无状态
		        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		        .authorizeRequests()
                //druid放行
		        .antMatchers("/statistics/**").permitAll()
                .antMatchers("/druid/**").permitAll()
                .antMatchers("/actuator/**").permitAll()
                //swagger放行
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/v2/api-docs").permitAll()
                .antMatchers("/configuration/ui").permitAll()
                .antMatchers("/configuration/security").permitAll()
                .antMatchers( "/*.html","/**/*.html","/**/*.css", "/**/*.js","/webSocket/**").permitAll()
                .antMatchers("/api/**").permitAll()
                .antMatchers("/webhook/**").permitAll()
                .antMatchers("/telegram/**").permitAll()
                .antMatchers("/merchants/webhookTest").permitAll()
                //其余所有请求需要身份认证
                .anyRequest().authenticated()
                .and()
                .logout()
                .permitAll()
                .logoutSuccessHandler(authenticationLogout) //注销时的逻辑
                .and()
		        .exceptionHandling().authenticationEntryPoint(new TokenAuthenticationEntryPoint())  //未登录时的逻辑处理
		        .accessDeniedHandler(new TokenAccessDeniedHandler())   //权限不足时的逻辑处理
		        .and()
		        .addFilterBefore(globalRateLimitFilter, JWTLoginFilter.class)
                .addFilter(new JWTLoginFilter(authenticationManager(),redisUtil,sysUserMapper,googleLimit))
                .addFilter(new JWTAuthenticationFilter(authenticationManager(),redisUtil));
    }


    /**
     * 使用自定义身份验证组件
     * @param auth 身份认证管理器
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(new CustomAuthenticationProvider(userDetailsService, sysUserMapper,response));
    }

}
