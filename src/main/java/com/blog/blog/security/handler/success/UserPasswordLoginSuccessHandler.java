package com.blog.blog.security.handler.success;


import com.blog.blog.api.user.service.UserUserService;
import com.blog.blog.bean.common.BaseResponse;
import com.blog.blog.bean.user.po.User;
import com.blog.blog.config.PropertyConfig;
import com.blog.blog.utils.JwtTokenUtils;
import com.blog.blog.security.handler.AbstractLoginSuccessHandler;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Li Yao Bing* @Date 2020/8/11
 **/

@Component
public class UserPasswordLoginSuccessHandler extends AbstractLoginSuccessHandler {

    @Resource
    UserUserService userUserService;


    @Override
    public Object preAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        if (!user.getRole().equals(PropertyConfig.ROLE_USER)) {
            sendResponse(httpServletResponse, BaseResponse.NO_ACCESS, "无权访问", null);
            return null;
        }
        user.setTokenVersion(user.getTokenVersion() + 1);
        userUserService.updateById(user);
        Map<String, Object> claims = new HashMap<>();
        claims.put("tokenVersion", user.getTokenVersion());
        String token = JwtTokenUtils.generateToken(claims, user.getUsername());
        Map<String, Object> data = new HashMap<>();
        data.put("userId", user.getUserId());
        data.put("role", user.getRole());
        data.put("token", token);
        data.put("userName", user.getUserRealName());
        return data;
    }
}
