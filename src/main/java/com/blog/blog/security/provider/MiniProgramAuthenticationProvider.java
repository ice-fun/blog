package com.blog.blog.security.provider;

import com.alibaba.fastjson.JSONObject;
import com.blog.blog.api.system.service.SystemLogService;
import com.blog.blog.bean.system.po.SystemLog;
import com.blog.blog.bean.wechat.vo.WechatResponse;
import com.blog.blog.security.AuthUserDetails;
import com.blog.blog.security.AuthUserDetailsService;
import com.blog.blog.security.token.CustomerMiniProgramAuthenticationToken;
import com.blog.blog.security.token.MiniProgramAuthenticationToken;
//import com.blog.blog.utils.wechat.WechatMiniProgramUtils;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.time.LocalDateTime;
import java.util.Collection;


public class MiniProgramAuthenticationProvider extends AbstractMatchClassAuthenticationProvider {

    private SystemLogService systemLogService;

    public MiniProgramAuthenticationProvider(AuthUserDetailsService userDetailsService, Class<? extends MiniProgramAuthenticationToken> matchClass, SystemLogService systemLogService) {
        super(userDetailsService, matchClass);
        this.systemLogService = systemLogService;
    }

    @SneakyThrows
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        MiniProgramAuthenticationToken miniProgramAuthenticationToken = (MiniProgramAuthenticationToken) authentication;
        WechatResponse wechatResponse = null;
//        if (matchClass.equals(CustomerMiniProgramAuthenticationToken.class)) {
//            wechatResponse = WechatMiniProgramUtils.customerMiniProgramAuthorization(miniProgramAuthenticationToken.getCode());
//
//        } else {
//            wechatResponse = WechatMiniProgramUtils.userMiniProgramAuthorization(miniProgramAuthenticationToken.getCode());
//        }
//        if (wechatResponse == null || wechatResponse.getErrorCode() != null) {
//            savaLog(miniProgramAuthenticationToken, wechatResponse);
//            throw new BadCredentialsException("请求异常");
//        }
//        // 如果unionId为空，解密数据
//        if (StringUtils.isBlank(wechatResponse.getUnionId())) {
//            wechatResponse = WechatMiniProgramUtils.decrypt(miniProgramAuthenticationToken.getInfo(), wechatResponse.getSessionKey());
//            if (wechatResponse == null || wechatResponse.getErrorCode() != null || StringUtils.isBlank(wechatResponse.getUnionId())) {
//                savaLog(miniProgramAuthenticationToken, wechatResponse);
//                throw new BadCredentialsException("登录失败，请重试");
//            }
//        }

        try {
            AuthUserDetails authUserDetails = userDetailsService.loadUserByOpenId(wechatResponse.getUnionId(), wechatResponse.getOpenId());
            checkAuthUserDetails(authUserDetails);
            return matchClass.getConstructor(Collection.class, AuthUserDetails.class, String.class, String.class)
                    .newInstance(authUserDetails.getAuthorities(), authUserDetails, miniProgramAuthenticationToken.getCode(),
                            miniProgramAuthenticationToken.getInfo());
        } catch (Exception e) {
            SystemLog systemLog = new SystemLog();
            systemLog.setType("授权异常");
            systemLog.setLogTime(LocalDateTime.now());
            systemLog.setCreateTime(LocalDateTime.now());
            systemLog.setParam(JSONObject.toJSONString(miniProgramAuthenticationToken));
            systemLog.setPath("provider");
            systemLogService.save(systemLog);
            throw new BadCredentialsException("登录失败，请重试");
        }
    }

    private void savaLog(MiniProgramAuthenticationToken miniProgramAuthenticationToken, WechatResponse wechatResponse) {
        SystemLog systemLog = new SystemLog();
        systemLog.setType("授权异常");
        if (wechatResponse != null) {
            systemLog.setException(wechatResponse.getErrormsg());
        }
        systemLog.setLogTime(LocalDateTime.now());
        systemLog.setCreateTime(LocalDateTime.now());
        systemLog.setResult(JSONObject.toJSONString(wechatResponse));
        systemLog.setParam(JSONObject.toJSONString(miniProgramAuthenticationToken));
        systemLog.setPath("provider");
        systemLogService.save(systemLog);
    }
}
