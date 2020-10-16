package com.blog.blog.security.token;

import com.blog.blog.security.AuthUserDetails;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;


public class VerifyCodeAuthenticationToken extends AbstractAuthenticationToken {
    private AuthUserDetails principal;
    @Getter
    @Setter
    private String phoneNumber;
    @Getter
    @Setter
    private String verifyCode;


    public VerifyCodeAuthenticationToken(String phoneNumber, String verifyCode) {
        super(Collections.emptyList());
        this.phoneNumber = phoneNumber;
        this.verifyCode = verifyCode;
    }

    public VerifyCodeAuthenticationToken(Collection<? extends GrantedAuthority> authorities, AuthUserDetails principal, String phoneNumber, String verifyCode, Boolean authenticated) {
        super(authorities);
        this.principal = principal;
        this.phoneNumber = phoneNumber;
        this.verifyCode = verifyCode;
        setAuthenticated(authenticated);
    }

    public VerifyCodeAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
