package com.knowswift.myspringboot.security.handler.success;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.knowswift.myspringboot.api.officialAccounts.service.UnbindSubscribeUserService;
import com.knowswift.myspringboot.api.user.service.UserUserService;
import com.knowswift.myspringboot.bean.user.po.User;
import com.knowswift.myspringboot.bean.user.vo.UserVO;
import com.knowswift.myspringboot.bean.wechat.po.UnbindSubscribeUser;
import com.knowswift.myspringboot.bean.wechat.vo.WechatResponse;
import com.knowswift.myspringboot.security.handler.AbstractLoginSuccessHandler;
import com.knowswift.myspringboot.security.token.MiniProgramAuthenticationToken;
import com.knowswift.myspringboot.utils.JwtTokenUtils;
import com.knowswift.myspringboot.utils.RedisUtils;
import com.knowswift.myspringboot.utils.WrappedBeanCopier;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static com.knowswift.myspringboot.config.PropertyConfig.TRUE;


@Component
public class UserMiniProgramLoginSuccessHandler extends AbstractLoginSuccessHandler {
    @Resource
    RedisUtils redisUtils;

    @Resource
    private UserUserService parentService;

    @Resource
    private UnbindSubscribeUserService unbindSubscribeUserService;

    @Override
    public Object preAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {

        MiniProgramAuthenticationToken miniProgramAuthenticationToken = (MiniProgramAuthenticationToken) authentication;
        User parent = (User) authentication.getPrincipal();
        Map<String, Object> data = new HashMap<>();
        String info = miniProgramAuthenticationToken.getInfo();
        WechatResponse wechatResponse = JSON.parseObject(info, WechatResponse.class);
        String avatarUrl = null;
        if (wechatResponse.getUserInfo() != null) {
            avatarUrl = wechatResponse.getUserInfo().getAvatarUrl();
        }
        if (parent.getUsername() == null) {
            redisUtils.set(miniProgramAuthenticationToken.getCode() + "_unionId", parent.getUnionId(), 300);
            redisUtils.set(miniProgramAuthenticationToken.getCode() + "_openId", parent.getMiniProgramOpenId(), 300);
            redisUtils.set(miniProgramAuthenticationToken.getCode() + "_avatar", avatarUrl, 300);
            data.put("code", miniProgramAuthenticationToken.getCode());
            sendResponse(httpServletResponse, 201, "请绑定手机号码", data);
            return null;
        }
        Map<String, Object> claims = new HashMap<>();
        parent.setTokenVersion(parent.getTokenVersion() + 1);
        if (StringUtils.isBlank(parent.getUnionId())) {
            parent.setUnionId(wechatResponse.getUnionId());
            UnbindSubscribeUser user = unbindSubscribeUserService.getOne(new LambdaQueryWrapper<UnbindSubscribeUser>().eq(UnbindSubscribeUser::getUnbindSubscribeUserUnionId, wechatResponse.getUnionId()));
            if (user != null) {
                parent.setOfficialOpenId(user.getUnbindSubscribeUserOpenId());
                parent.setIsSubscribe(TRUE);
            }
        }
        parentService.updateById(parent);
        claims.put("tokenVersion", parent.getTokenVersion());
        String token = JwtTokenUtils.generateToken(claims, parent.getUsername());
        UserVO userVo = WrappedBeanCopier.copyProperties(parent, UserVO.class);
        userVo.setToken(token);
        return userVo;

    }
}