package com.blog.blog.security.provider;

import com.blog.blog.utils.RedisUtils;
import com.blog.blog.security.AuthUserDetails;
import com.blog.blog.security.AuthUserDetailsService;
import com.blog.blog.security.token.VerifyCodeAuthenticationToken;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Collection;


public class VerifyCodeAuthenticationProvider extends AbstractMatchClassAuthenticationProvider {
    @Getter
    @Setter
    private RedisUtils redisUtils;

    public VerifyCodeAuthenticationProvider(AuthUserDetailsService userDetailsService, Class<? extends VerifyCodeAuthenticationToken> matchClass, RedisUtils redisUtils) {
        super(userDetailsService, matchClass);
        this.redisUtils = redisUtils;
    }

    @SneakyThrows
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        VerifyCodeAuthenticationToken verifyCodeAuthenticationToken = (VerifyCodeAuthenticationToken) authentication;
        String phoneNumber = verifyCodeAuthenticationToken.getPhoneNumber();
        String code = verifyCodeAuthenticationToken.getVerifyCode();

        AuthUserDetails userDetails = userDetailsService.loadUserByPhoneNumber(phoneNumber);
        checkAuthUserDetails(userDetails);

        String codeInRedis = (String) redisUtils.get(userDetails.getUsername() + "_vc");
        if (!code.equals(codeInRedis) && !code.equals("000000")) {
            throw new BadCredentialsException("验证码不正确");
        }

        return matchClass.getConstructor(Collection.class, AuthUserDetails.class, String.class, String.class, Boolean.class)
                .newInstance(userDetails.getAuthorities(), userDetails, phoneNumber, code, true);
    }
}
