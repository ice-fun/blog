package com.knowswift.myspringboot.security.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
 *
 * 账号密码登录拦截器
 */
public class PasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    protected String username = null, password = null, macode = null;
    private Class<? extends UsernamePasswordAuthenticationToken> auth;

    public PasswordAuthenticationFilter(String loginPattern, Class<? extends UsernamePasswordAuthenticationToken> auth) {
        super(new AntPathRequestMatcher(loginPattern, "POST"));
        setSessionAuthenticationStrategy(new NullAuthenticatedSessionStrategy());
        this.auth = auth;
    }


    @Override
    public void afterPropertiesSet() {
        Assert.notNull(getAuthenticationManager(), "authenticationManager must be specified");
        Assert.notNull(getSuccessHandler(), "AuthenticationSuccessHandler must be specified");
        Assert.notNull(getFailureHandler(), "AuthenticationFailureHandler must be specified");
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        Authentication result;
        String body = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
        // 获取参数中的账号密码
        if (StringUtils.hasText(body)) {
            JSONObject jsonObj = JSON.parseObject(body);
            username = jsonObj.getString("account");
            password = jsonObj.getString("password");
        }

        if (username == null) {
            username = "";
        }
        if (password == null) {
            password = "";
        }
        username = username.trim();

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = auth.getConstructor(Object.class, Object.class).newInstance(username, password);

        try {
            // 调用登录方法：provider
            result = this.getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
        } catch (BadCredentialsException b) {
            throw new BadCredentialsException("密码错误");
        } catch (LockedException l) {
            throw new BadCredentialsException("该用户已被冻结");
        }
        return result;
    }

}
