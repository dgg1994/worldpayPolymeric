package com.polymeric.security;

import org.springframework.security.core.GrantedAuthority;

/**
 * 授予权限
 * 负责存储权限和角色
 *
 */
@SuppressWarnings("serial")
public class GrantedAuthorityImpl implements GrantedAuthority {

    private String authority;

    public GrantedAuthorityImpl(String authority) {
        this.authority = authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }

}
