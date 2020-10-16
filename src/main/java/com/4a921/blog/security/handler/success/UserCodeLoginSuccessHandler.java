package com.knowswift.myspringboot.security.handler.success;

import com.knowswift.myspringboot.api.user.service.UserUserService;
import com.knowswift.myspringboot.bean.user.po.User;
import com.knowswift.myspringboot.bean.user.vo.UserVO;
import com.knowswift.myspringboot.security.handler.AbstractLoginSuccessHandler;
import com.knowswift.myspringboot.utils.JwtTokenUtils;
import com.knowswift.myspringboot.utils.WrappedBeanCopier;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@Component
public class UserCodeLoginSuccessHandler extends AbstractLoginSuccessHandler {
    @Resource
    private UserUserService parentService;

    @Override
    public Object preAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        user.setTokenVersion(user.getTokenVersion() + 1);
        parentService.updateById(user);
        Map<String, Object> claims = new HashMap<>();
        claims.put("tokenVersion", user.getTokenVersion());
        String token = JwtTokenUtils.generateToken(claims, user.getUsername());
        UserVO userVo = WrappedBeanCopier.copyProperties(user, UserVO.class);
        userVo.setToken(token);
        return userVo;
    }
}
