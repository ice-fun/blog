package com.blog.blog.security.handler.success;

import com.blog.blog.api.admin.service.AdminAdminService;
import com.blog.blog.bean.admin.po.Admin;
import com.blog.blog.bean.common.BaseResponse;
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


@Component
public class AdminPasswordLoginSuccessHandler extends AbstractLoginSuccessHandler {
    @Resource
    AdminAdminService adminAdminService;

    @Override
    public Object preAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        Admin admin = (Admin) authentication.getPrincipal();
        if (!admin.getRole().equals(PropertyConfig.ROLE_ADMIN)) {
            sendResponse(httpServletResponse, BaseResponse.NO_ACCESS, "无权访问", null);
            return null;
        }
        admin.setAdminTokenVersion(admin.getAdminTokenVersion() + 1);
        adminAdminService.updateById(admin);
        Map<String, Object> claims = new HashMap<>();
        claims.put("tokenVersion", admin.getAdminTokenVersion());
        String token = JwtTokenUtils.generateToken(claims, admin.getUsername());
        Map<String, Object> data = new HashMap<>();
        data.put("adminId", admin.getAdminId());
        data.put("role", admin.getRole());
        data.put("token", token);
        data.put("adminName", admin.getAdminName());
        return data;
    }
}
