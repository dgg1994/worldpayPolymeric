package com.polymeric.utils;

import com.polymeric.constants.Constants;
import com.polymeric.dao.merchants.MerchantsInfoDao;
import com.polymeric.dao.system.SysUserDao;
import com.polymeric.entity.merchants.MerchantsInfoEntity;
import com.polymeric.entity.system.SysRoleEntity;
import com.polymeric.entity.system.SysUserEntity;
import com.polymeric.enums.RoleTypeEnums;
import com.polymeric.enums.UserStateEnums;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

/**
 * 类描述：token工具类
 *
 * @author GeminiSun
 * @date 2026/07/03 13:58
 */
@Component
public class TokenUtils {

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private MerchantsInfoDao merchantsInfoDao;

    public String getToken() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        HttpServletRequest request = attributes.getRequest();
        return request.getHeader(Constants.HEADER_AUTH);
    }

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public String getUsername() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return null;
        }
        return authentication.getName();
    }

    /**
     * 获取当前登录用户
     */
    public SysUserEntity getCurrentUser() {
        String username = getUsername();
        if (StringUtils.isBlank(username)) {
            return null;
        }
        return sysUserDao.findByAcctiveState(username, UserStateEnums.NORMAL.getIndex());
    }

    /**
     * 获取当前登录用户id
     */
    public Integer getCurrentUserId() {
        SysUserEntity user = getCurrentUser();
        return user == null ? null : user.getId();
    }

    /**
     * 获取当前用户角色标识列表，如 admin、merchants
     */
    public List<String> getRoleKeys() {
        SysUserEntity user = getCurrentUser();
        if (user == null) {
            return Collections.emptyList();
        }
        List<String> roleKeys = sysUserDao.findRoleKey(user.getId());
        return roleKeys == null ? Collections.emptyList() : roleKeys;
    }

    /**
     * 获取当前用户角色详情
     */
    public List<SysRoleEntity> getRoleList() {
        SysUserEntity user = getCurrentUser();
        if (user == null) {
            return Collections.emptyList();
        }
        List<SysRoleEntity> roles = sysUserDao.findUserRole(user.getId());
        return roles == null ? Collections.emptyList() : roles;
    }

    /**
     * 获取当前用户主角色类型，管理员 > 商户 > 运营
     */
    public RoleTypeEnums getRoleType() {
        List<SysRoleEntity> roles = getRoleList();
        if (CollectionUtils.isEmpty(roles)) {
            return null;
        }
        if (roles.stream().anyMatch(role -> RoleTypeEnums.ADMIN.getIndex().equals(role.getRoleId()))) {
            return RoleTypeEnums.ADMIN;
        }
        if (roles.stream().anyMatch(role -> RoleTypeEnums.MERCHANTS.getIndex().equals(role.getRoleId()))) {
            return RoleTypeEnums.MERCHANTS;
        }
        if (roles.stream().anyMatch(role -> RoleTypeEnums.OPERATIONS.getIndex().equals(role.getRoleId()))) {
            return RoleTypeEnums.OPERATIONS;
        }
        return null;
    }

    /**
     * 是否管理员
     */
    public boolean isAdmin() {
        return hasRoleKey(RoleTypeEnums.ADMIN.getValue());
    }

    /**
     * 是否商户
     */
    public boolean isMerchant() {
        return hasRoleKey(RoleTypeEnums.MERCHANTS.getValue());
    }

    /**
     * 是否运营
     */
    public boolean isOperations() {
        return hasRoleKey(RoleTypeEnums.OPERATIONS.getValue());
    }

    /**
     * 是否包含指定角色标识
     */
    public boolean hasRoleKey(String roleKey) {
        if (StringUtils.isBlank(roleKey)) {
            return false;
        }
        return getRoleKeys().stream().anyMatch(roleKey::equals);
    }

    /**
     * 是否可查看全部数据（管理员、运营）
     */
    public boolean isDataScopeAll() {
        return isAdmin() || isOperations();
    }

    /**
     * 数据查询范围：管理员/运营返回 null（不限制），商户返回绑定的商户id
     */
    public Integer getDataScopeMerchantId() {
        if (isDataScopeAll()) {
            return null;
        }
        if (isMerchant()) {
            return getMerchantId();
        }
        return null;
    }

    /**
     * 获取当前商户用户绑定的商户id
     */
    public Integer getMerchantId() {
        MerchantsInfoEntity merchantInfo = getMerchantInfo();
        return merchantInfo == null ? null : merchantInfo.getId();
    }

    /**
     * 获取当前商户用户绑定的商户id
     */
    public String getMerchantAppId() {
        MerchantsInfoEntity merchantInfo = getMerchantInfo();
        return merchantInfo == null ? null : merchantInfo.getAppId();
    }

    /**
     * 获取当前商户用户绑定的商户信息
     */
    public MerchantsInfoEntity getMerchantInfo() {
        SysUserEntity user = getCurrentUser();
        if (user == null) {
            return null;
        }
        return merchantsInfoDao.findBySysAccountId(user.getId());
    }

    /**
     * 菜单权限检查
     */
    public boolean hasAuthority(String authority) {
        Authentication authentication = getAuthentication();
        if (authentication == null || CollectionUtils.isEmpty(authentication.getAuthorities())) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(grantedAuthority -> grantedAuthority.equals(authority));
    }

    /**
     * 角色检查（自动添加 ROLE_ 前缀）
     */
    public boolean hasRole(String role) {
        return hasAuthority("ROLE_" + role);
    }
}
