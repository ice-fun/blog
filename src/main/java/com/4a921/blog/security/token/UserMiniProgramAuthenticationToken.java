package com.knowswift.myspringboot.security.token;

import com.knowswift.myspringboot.security.AuthUserDetails;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


public class UserMiniProgramAuthenticationToken extends MiniProgramAuthenticationToken {
    public UserMiniProgramAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    public UserMiniProgramAuthenticationToken(String code, String info) {
        super(code, info);
    }

    public UserMiniProgramAuthenticationToken(Collection<? extends GrantedAuthority> authorities, AuthUserDetails userDetails, String code, String info) {
        super(authorities, userDetails, code, info);
    }
}
