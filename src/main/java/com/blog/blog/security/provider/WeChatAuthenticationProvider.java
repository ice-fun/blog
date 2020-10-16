package com.blog.blog.security.provider;

import com.blog.blog.bean.user.po.User;
import com.blog.blog.bean.wechat.vo.WechatResponse;
//import com.blog.blog.utils.wechat.WeChatAuthorizeUtils;
import com.blog.blog.security.AuthUserDetails;
import com.blog.blog.security.AuthUserDetailsService;
import com.blog.blog.security.token.WeChatAuthenticationToken;
import lombok.SneakyThrows;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Collection;

/**
 * @author Li Yao Bing* @Date 2020/8/14
 **/


public class WeChatAuthenticationProvider extends AbstractMatchClassAuthenticationProvider {
    public WeChatAuthenticationProvider(AuthUserDetailsService userDetailsService, Class<? extends WeChatAuthenticationToken> matchClass) {
        super(userDetailsService, matchClass);
    }

    @SneakyThrows
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        WeChatAuthenticationToken weChatAuthenticationToken = (WeChatAuthenticationToken) authentication;
//        WechatResponse wechatResponse = WeChatAuthorizeUtils.weChatAuthorization(weChatAuthenticationToken.getCode());
//        if (wechatResponse == null || wechatResponse.getErrorCode() != null) {
//            throw new BadCredentialsException("请求异常");
//        }
//        AuthUserDetails authUserDetails = userDetailsService.loadUserByOpenId(wechatResponse.getUnionId(), wechatResponse.getOpenId());
//        checkAuthUserDetails(authUserDetails);
//        User userInfo = WeChatAuthorizeUtils.getUserInfo(wechatResponse.getAccessToken(), wechatResponse.getOpenId());
//        User user = (User) authUserDetails;
//        user.setUnionId(userInfo.getUnionId());
//        user.setUserAvatar(userInfo.getUserAvatar());
//        return matchClass.getConstructor(Collection.class, AuthUserDetails.class, String.class)
//                .newInstance(authUserDetails.getAuthorities(), authUserDetails, weChatAuthenticationToken.getCode());
        return null;
    }
}
