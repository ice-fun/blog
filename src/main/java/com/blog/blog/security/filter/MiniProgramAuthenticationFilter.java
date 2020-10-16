package com.blog.blog.security.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blog.blog.api.system.service.SystemLogService;
import com.blog.blog.bean.system.po.SystemLog;
import com.blog.blog.security.token.MiniProgramAuthenticationToken;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;


public class MiniProgramAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private Class<? extends MiniProgramAuthenticationToken> authClass;


    @Resource
    SystemLogService systemLogService;


    public MiniProgramAuthenticationFilter(String loginPattern, Class<? extends MiniProgramAuthenticationToken> authClass) {
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
        String code = null;
        String info = null;
        if (StringUtils.hasText(body)) {
            JSONObject jsonObj = JSON.parseObject(body);
            code = jsonObj.getString("code");
            info = jsonObj.getString("info");
        }
        if (code == null) {
            code = "";
        }
        if (info == null) {
            info = "";
        }
        SystemLog systemLog = new SystemLog();
        systemLog.setParam(body);

        MiniProgramAuthenticationToken authRequest = authClass.getConstructor(String.class, String.class).newInstance(code, info);
        result = this.getAuthenticationManager().authenticate(authRequest);
        return result;
    }

}
