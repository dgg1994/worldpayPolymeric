package com.polymeric.security;
import com.polymeric.base.JsonData;
import com.polymeric.constants.Constants;
import com.polymeric.constants.MenuConstants;
import com.polymeric.dao.system.SysUserDao;
import com.polymeric.entity.system.SysRoleEntity;
import com.polymeric.entity.system.SysUserEntity;
import com.polymeric.enums.UserStateEnums;
import com.polymeric.utils.CustomUtils;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.DigestUtils;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

/**
 * (身份验证提供程序)：实现 security自定义身份认证验证组件
 *
 */
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    private static SysUserDao sysUserMapper;

	private HttpServletResponse response;

    @SuppressWarnings("static-access")
	public CustomAuthenticationProvider(UserDetailsService userDetailsService, SysUserDao sysUserMapper,HttpServletResponse response) {
        this.userDetailsService = userDetailsService;
        this.sysUserMapper = sysUserMapper;
        this.response = response;
    }

    /**
     * 用户名密码校验，返回身份认证令牌
     *
     * @param authentication 身份验证令牌（其中包括用户名，密码，权限信息）
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取认证的用户名 & 密码
        String userName = authentication.getName();
        String password = authentication.getCredentials().toString();
        //调用该接口我们现实了的认证方法
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        if (null != userDetails) {
        	
            // 自定义的加密方法
            String encodePassword = DigestUtils.md5DigestAsHex((password).getBytes());
            if (userDetails.getPassword().equals(encodePassword)) {
                // 这里设置权限，后面就可以从userDetails获取getAuthorities()权限了
                ArrayList<GrantedAuthority> authorities = getUserAuthoritiesByUsername(userName);
                // 生成jwt令牌 这里令牌里面存入了:userName,password,authorities, 当然你也可以放其他内容
                //（如果别人获得了密钥，token会被成功解析，所以不建议放密码等敏感信息进去）
                return new UsernamePasswordAuthenticationToken(userName, password, authorities);
            }else if(userDetails.getPassword().equals(password)) {
            	 ArrayList<GrantedAuthority> authorities = getUserAuthoritiesByUsername(userName);
            	return new UsernamePasswordAuthenticationToken(userName, password, authorities);
            }else {
            	CustomUtils.sendJsonMessage(response, JsonData.Error("账号密码错误！！"));
                throw new BadCredentialsException("密码错误");
            }
        } else {
        	CustomUtils.sendJsonMessage(response, JsonData.Error("用户不存在！！"));
            throw new UsernameNotFoundException("用户不存在");
        }
    }

    /**
     * 是否可以提供输入类型的认证服务
     * 登录时，先执行JWTLoginFilter.attemptAuthentication，然后会进入该方法
     * 最后再执行CustomAuthenticationProvider.authenticate方法，具体认证
     *
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    /**
     * 获取该用户所有的菜单权限
     * @param username 用户名
     * @return 返回 array list 描述此返回参数
     */
    private ArrayList<GrantedAuthority> getUserAuthoritiesByUsername(String username) {
        List<String> menuCodeList = sysUserMapper.findMenuCodeByUserName(username,UserStateEnums.NORMAL.getIndex());
        ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>(menuCodeList.size());
        menuCodeList.forEach(menuCode -> grantedAuthorities.add(new GrantedAuthorityImpl(MenuConstants.ROLE_PREFIX + menuCode)));
        return grantedAuthorities;
    }
    
    /**
     * 获取该用户所有的菜单权限
     * @param username 用户名
     * @return 返回 array list 描述此返回参数
     */
	public static List<GrantedAuthorityImpl> getUserAuthorities(String username) {
		List<GrantedAuthorityImpl> menuCodeList = new ArrayList<>();
		List<String> menuList = new ArrayList<>();
//		SysUserEntity sysUser = sysUserMapper.findByAcctive(username);
		SysUserEntity sysUser = sysUserMapper.findByAcctiveState(username,UserStateEnums.NORMAL.getIndex());
        if (sysUser==null) {
            return menuCodeList;
        }
		List<SysRoleEntity> roleList = sysUserMapper.findUserRole(sysUser.getId());
		boolean temp = roleList.stream().anyMatch(p -> p.getRoleKey().equals(Constants.ADMIN_STR));
		if(temp) {//管理员角色
			menuList = sysUserMapper.findMenuPermsAll();
		}else {
			menuList = sysUserMapper.findMenuCodeByUserName(username,UserStateEnums.NORMAL.getIndex());
		}
        if(menuList != null && menuList.size() > 0) {
        	for (int i = 0; i < menuList.size(); i++) {
        		menuCodeList.add(new GrantedAuthorityImpl(MenuConstants.ROLE_PREFIX +menuList.get(i)));
			}
        }
        return menuCodeList;
    }

}
