package com.blog.blog.security.handler.success;

import com.blog.blog.api.system.service.SystemLogService;
import com.blog.blog.api.system.service.UnbindSubscribeUserService;
import com.blog.blog.api.user.service.UserUserService;
import com.blog.blog.bean.user.po.User;
import com.blog.blog.bean.user.vo.UserVO;
import com.blog.blog.utils.JwtTokenUtils;
import com.blog.blog.utils.RedisUtils;
import com.blog.blog.utils.WrappedBeanCopier;
import com.blog.blog.security.handler.AbstractLoginSuccessHandler;
import com.blog.blog.security.token.WeChatAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@Component
public class UserWeChatLoginSuccessHandler extends AbstractLoginSuccessHandler {
    @Resource
    RedisUtils redisUtils;

    @Resource
    private UserUserService userUserService;

    @Resource
    private UnbindSubscribeUserService unbindSubscribeUserService;

    @Resource
    SystemLogService systemLogService;

    @Override
    public Object preAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {

        WeChatAuthenticationToken weChatAuthenticationToken = (WeChatAuthenticationToken) authentication;
        User user = (User) authentication.getPrincipal();
        Map<String, Object> data = new HashMap<>();
        if (user.getUsername() == null) {
            redisUtils.set(weChatAuthenticationToken.getCode() + "_unionId", user.getUnionId(), 300);
            redisUtils.set(weChatAuthenticationToken.getCode() + "_subOpenId", user.getOfficialOpenId(), 300);
            redisUtils.set(weChatAuthenticationToken.getCode() + "_avatar", user.getUserAvatar(), 300);
            data.put("code", weChatAuthenticationToken.getCode());
            sendResponse(httpServletResponse, 201, "请绑定手机号码", data);
            return null;
        }
        Map<String, Object> claims = new HashMap<>();
        user.setTokenVersion(user.getTokenVersion() + 1);
        userUserService.updateById(user);
        claims.put("tokenVersion", user.getTokenVersion());
        String token = JwtTokenUtils.generateToken(claims, user.getUsername());
        UserVO userVO = WrappedBeanCopier.copyProperties(user, UserVO.class);
        userVO.setToken(token);
        return userVO;

    }
}