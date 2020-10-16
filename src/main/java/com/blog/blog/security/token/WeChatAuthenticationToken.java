package com.blog.blog.security.token;

import com.blog.blog.security.AuthUserDetails;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;


public class WeChatAuthenticationToken extends AbstractAuthenticationToken {
    @Getter
    private String code;
    private AuthUserDetails userDetails;

    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal
     *                    represented by this authentication object.
     */
    public WeChatAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    public WeChatAuthenticationToken(String code) {
        super(Collections.EMPTY_LIST);
        this.code = code;
    }

    public WeChatAuthenticationToken(Collection<? extends GrantedAuthority> authorities, AuthUserDetails userDetails, String code) {
        super(authorities);
        this.userDetails = userDetails;
        this.code = code;
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
