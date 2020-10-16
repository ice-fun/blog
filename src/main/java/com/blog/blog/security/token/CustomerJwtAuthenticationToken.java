package com.blog.blog.security.token;

import com.blog.blog.security.AuthUserDetails;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


public class CustomerJwtAuthenticationToken extends JwtAuthenticationToken {
    public CustomerJwtAuthenticationToken(String token) {
        super(token);
    }

    public CustomerJwtAuthenticationToken(AuthUserDetails principal, String token, Collection<? extends GrantedAuthority> authorities) {
        super(principal, token, authorities);
    }
}
