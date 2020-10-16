package com.knowswift.myspringboot.security.token;

import com.knowswift.myspringboot.security.AuthUserDetails;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


public class CustomerMiniProgramAuthenticationToken extends MiniProgramAuthenticationToken {
    public CustomerMiniProgramAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

//    public TeacherMiniProgramAuthenticationToken(String code) {
//        super(code);
//    }

    public CustomerMiniProgramAuthenticationToken(String code, String info) {
        super(code, info);
    }

//    public TeacherMiniProgramAuthenticationToken(Collection<? extends GrantedAuthority> authorities, AuthUserDetails userDetails, String code) {
//        super(authorities, userDetails, code);
//    }

    public CustomerMiniProgramAuthenticationToken(Collection<? extends GrantedAuthority> authorities, AuthUserDetails userDetails, String code, String info) {
        super(authorities, userDetails, code, info);
    }
}
