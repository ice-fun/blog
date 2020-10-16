package com.blog.blog.api.admin.controller;

import com.blog.blog.bean.admin.po.Admin;
import com.blog.blog.bean.admin.vo.AdminVO;
import com.blog.blog.bean.common.BaseResponse;
import com.blog.blog.utils.JwtTokenUtils;
import com.blog.blog.annotation.EnableLog;
import com.blog.blog.annotation.IgnoreLog;
import com.blog.blog.security.token.JwtAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Li Yao Bing*/
@RestController
@RequestMapping("/admin/admin/")
public class AdminAdminController extends AdminBaseController {

    @PostMapping("/checkTokenStatus")
    @IgnoreLog
    public BaseResponse checkTokenStatus(@AuthenticationPrincipal Authentication authentication) {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        Admin admin = (Admin) authentication.getPrincipal();
        String token = jwtAuthenticationToken.getToken();
        if (JwtTokenUtils.isTokenAlmostExpired(jwtAuthenticationToken.getToken())) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("tokenVersion", admin.getAdminTokenVersion());
            token = JwtTokenUtils.generateToken(claims, admin.getAdminAccount());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("adminId", admin.getAdminId());
        map.put("role", admin.getRole());
        map.put("token", token);
        map.put("adminName", admin.getAdminName());
        return BaseResponse.createSuccessResponse(map);
    }

    @PostMapping("/editPassword")
    @EnableLog(logName = "修改密码")
    public BaseResponse editPassword(@AuthenticationPrincipal Admin admin, @RequestBody AdminVO param) {
        String oldPassword = param.getOldPassword();
        String newPassword = param.getNewPassword();

        if (!bCryptPasswordEncoder.matches(oldPassword, admin.getAdminPassword())) {
            return BaseResponse.createFailResponse("旧密码不正确");
        }
        admin.setAdminPassword(bCryptPasswordEncoder.encode(newPassword));
        boolean update = adminAdminService.updateById(admin);
        return BaseResponse.createSuccessOrFailResponse(update, "修改失败");
    }

}
