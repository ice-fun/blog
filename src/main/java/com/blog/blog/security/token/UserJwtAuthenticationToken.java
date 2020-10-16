package com.blog.blog.security.token;

import com.blog.blog.security.AuthUserDetails;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


public class UserJwtAuthenticationToken extends JwtAuthenticationToken {
    public UserJwtAuthenticationToken(String token) {
        super(token);
    }

    public UserJwtAuthenticationToken(AuthUserDetails principal, String token, Collection<? extends GrantedAuthority> authorities) {
        super(principal, token, authorities);
    }
}
