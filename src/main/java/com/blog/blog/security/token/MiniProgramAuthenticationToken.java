package com.blog.blog.security.token;

import com.blog.blog.security.AuthUserDetails;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;


public class MiniProgramAuthenticationToken extends AbstractAuthenticationToken {
    @Getter
    private String code;
    @Getter
    private String info;
    private AuthUserDetails userDetails;

    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal
     *                    represented by this authentication object.
     */
    public MiniProgramAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

//    public MiniProgramAuthenticationToken(String code) {
//        super(Collections.EMPTY_LIST);
//        this.code = code;
//    }

    public MiniProgramAuthenticationToken(String code, String info) {
        super(Collections.EMPTY_LIST);
        this.code = code;
        this.info = info;
    }

//    public MiniProgramAuthenticationToken(Collection<? extends GrantedAuthority> authorities, AuthUserDetails userDetails, String code) {
//        super(authorities);
//        this.userDetails = userDetails;
//        this.code = code;
//    }

    public MiniProgramAuthenticationToken(Collection<? extends GrantedAuthority> authorities, AuthUserDetails userDetails, String code, String info) {
        super(authorities);
        this.userDetails = userDetails;
        this.code = code;
        this.info = info;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userDetails;
    }
}
