package com.blog.blog.security.token;

import com.blog.blog.security.AuthUserDetails;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomerVerifyCodeAuthenticationToken extends VerifyCodeAuthenticationToken {
    public CustomerVerifyCodeAuthenticationToken(String phoneNumber, String code) {
        super(phoneNumber, code);
    }

    public CustomerVerifyCodeAuthenticationToken(Collection<? extends GrantedAuthority> authorities, AuthUserDetails principal, String phoneNumber, String code, Boolean authenticated) {
        super(authorities, principal, phoneNumber, code, authenticated);
    }

    public CustomerVerifyCodeAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }
}
