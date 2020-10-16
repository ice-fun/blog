package com.blog.blog.security.provider;

import com.blog.blog.utils.JwtTokenUtils;
import com.blog.blog.security.AuthUserDetails;
import com.blog.blog.security.AuthUserDetailsService;
import com.blog.blog.security.token.JwtAuthenticationToken;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;


public class JwtAuthenticationProvider extends AbstractMatchClassAuthenticationProvider {
    private String tokenKey;

    public JwtAuthenticationProvider(AuthUserDetailsService userDetailsService, Class<? extends JwtAuthenticationToken> matchClass, String tokenKey) {
        super(userDetailsService, matchClass);
        this.userDetailsService = userDetailsService;
        this.tokenKey = tokenKey;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        String token = jwtAuthenticationToken.getToken();

        if (StringUtils.isEmpty(token)) {
            throw new InsufficientAuthenticationException("尚未登录");
        }
        String username = null;
        AuthUserDetails authUserDetails;
        try {
            username = JwtTokenUtils.getUsernameFromToken(token);
            authUserDetails = (AuthUserDetails) userDetailsService.loadUserByUsername(username);
            checkAuthUserDetails(authUserDetails);
            JwtTokenUtils.validateToken(token, authUserDetails.getUsername(), authUserDetails.getTokenVersionMap().get(tokenKey));
        } catch (JwtException e) {
            throw new BadCredentialsException(e.getMessage(), null);
        }
        return new JwtAuthenticationToken(authUserDetails, token, authUserDetails.getAuthorities());
    }
}
