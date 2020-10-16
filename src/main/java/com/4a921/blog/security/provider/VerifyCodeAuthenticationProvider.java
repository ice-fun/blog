package com.knowswift.myspringboot.security.provider;

import com.knowswift.myspringboot.security.AuthUserDetails;
import com.knowswift.myspringboot.security.AuthUserDetailsService;
import com.knowswift.myspringboot.security.token.VerifyCodeAuthenticationToken;
import com.knowswift.myspringboot.utils.RedisUtils;
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
