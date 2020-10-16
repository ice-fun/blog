package com.knowswift.myspringboot.security.token;

import com.knowswift.myspringboot.security.AuthUserDetails;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


public class AdminJwtAuthenticationToken extends JwtAuthenticationToken {
    public AdminJwtAuthenticationToken(String token) {
        super(token);
    }

    public AdminJwtAuthenticationToken(AuthUserDetails principal, String token, Collection<? extends GrantedAuthority> authorities) {
        super(principal, token, authorities);
    }
}
