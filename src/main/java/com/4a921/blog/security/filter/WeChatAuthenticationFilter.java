package com.knowswift.myspringboot.security.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.knowswift.myspringboot.api.officialAccounts.service.SystemLogService;
import com.knowswift.myspringboot.bean.system.po.SystemLog;
import com.knowswift.myspringboot.security.token.WeChatAuthenticationToken;
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

/**
 * 微信授权登录拦截器
 */
public class WeChatAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private Class<? extends WeChatAuthenticationToken> authClass;


    @Resource
    SystemLogService systemLogService;


    public WeChatAuthenticationFilter(String loginPattern, Class<? extends WeChatAuthenticationToken> authClass) {
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
        Authentication result;
        String body = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
        String code = null;
        if (StringUtils.hasText(body)) {
            JSONObject jsonObj = JSON.parseObject(body);
            code = jsonObj.getString("code");
        }
        if (code == null) {
            code = "";
        }
        SystemLog systemLog = new SystemLog();
        systemLog.setParam(body);

        WeChatAuthenticationToken authRequest = authClass.getConstructor(String.class).newInstance(code);
        result = this.getAuthenticationManager().authenticate(authRequest);
        return result;
    }

}
