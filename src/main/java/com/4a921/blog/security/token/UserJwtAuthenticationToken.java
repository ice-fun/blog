package com.knowswift.myspringboot.security.token;

import com.knowswift.myspringboot.security.AuthUserDetails;
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
