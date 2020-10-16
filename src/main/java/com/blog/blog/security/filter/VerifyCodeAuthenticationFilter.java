package com.blog.blog.security.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blog.blog.security.token.VerifyCodeAuthenticationToken;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

/**
 * 手机验证码登录拦截器
 */
public class VerifyCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private Class<? extends VerifyCodeAuthenticationToken> authClass;

    public VerifyCodeAuthenticationFilter(String loginPattern, Class<? extends VerifyCodeAuthenticationToken> authClass) {
        super(new AntPathRequestMatcher(loginPattern, "POST"));
        setSessionAuthenticationStrategy(new NullAuthenticatedSessionStrategy());
        this.authClass = authClass;
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(getSuccessHandler(), "AuthenticationSuccessHandler must be specified");
        Assert.notNull(getFailureHandler(), "AuthenticationFailureHandler must be specified");
        Assert.notNull(getAuthenticationManager(), "authenticationManager must be specified");
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        Authentication result = null;
        String body = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
        String phoneNumber = null, code = null;
        if (StringUtils.hasText(body)) {
            JSONObject jsonObj = JSON.parseObject(body);
            phoneNumber = jsonObj.getString("phoneNumber");
            code = jsonObj.getString("verifyCode");
        }

        if (phoneNumber == null) {
            phoneNumber = "";
        }
        if (code == null) {
            code = "";
        }
        phoneNumber = phoneNumber.trim();
        VerifyCodeAuthenticationToken authRequest = authClass.getConstructor(String.class, String.class).newInstance(phoneNumber, code);
        result = this.getAuthenticationManager().authenticate(authRequest);
        return result;
    }

}
