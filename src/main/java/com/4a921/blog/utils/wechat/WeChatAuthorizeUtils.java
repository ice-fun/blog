package com.knowswift.myspringboot.utils.wechat;

import com.alibaba.fastjson.JSON;
import com.knowswift.myspringboot.api.officialAccounts.service.UnbindSubscribeUserService;
import com.knowswift.myspringboot.api.user.service.UserUserService;
import com.knowswift.myspringboot.bean.customer.po.Customer;
import com.knowswift.myspringboot.bean.user.po.User;
import com.knowswift.myspringboot.bean.wechat.vo.WechatResponse;
import com.knowswift.myspringboot.bean.wechat.vo.WechatUserInfo;
import com.knowswift.myspringboot.config.PropertyConfig;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Li Yao Bing
 **/

@Component
public class WeChatAuthorizeUtils {

    private static WeChatAuthorizeUtils weChatAuthorizeUtils;
    @Resource
    UserUserService userUserService;
    @Resource
    UnbindSubscribeUserService unbindSubscribeUserService;

    /**
     * 微信公众号授权方法
     *
     * @param code 微信拉取授权窗口后 返回的CODE， 前端传递
     * @return
     */
    private static WechatResponse WeChatAuthorization(String code) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        Map<String, String> params = new HashMap<String, String>();
        params.put("APPID", PropertyConfig.WECHAT_OFFICIAL_ACCOUNTS_APP_ID);
        params.put("SECRET", PropertyConfig.WECHAT_OFFICIAL_ACCOUNTS_APP_SECRET);
        params.put("CODE", code);
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={APPID}&secret={SECRET}&code={CODE}&grant_type=authorization_code";
        String response = restTemplate.getForObject(url, String.class, params);
        return JSON.parseObject(response, WechatResponse.class);
    }

    public static WechatResponse weChatAuthorization(String code) {
        return WeChatAuthorization(code);
    }

    /**
     * 拉取个人信息
     *
     * @param accessToken 上一步，授权成功后，微信返回
     * @param openId      授权成功后微信返回
     * @return
     */
    public static WechatUserInfo getUserInformation(String accessToken, String openId) {

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        Map<String, String> params = new HashMap<String, String>();
        params.put("ACCESS_TOKEN", accessToken);
        params.put("OPENID", openId);
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token={ACCESS_TOKEN}&openid={OPENID}&lang=zh_CN";
        String response = restTemplate.getForObject(url, String.class, params);
        return JSON.parseObject(response, WechatUserInfo.class);
    }


    public static User getUserInfo(String accessToken, String openId) {
        WechatUserInfo wechatUserInfo = getUserInformation(accessToken, openId);
        User user = new User();
        user.setUserAvatar(wechatUserInfo.getHeadImgUrl());
        user.setUnionId(wechatUserInfo.getUnionId());
        return user;
    }

    public static Customer getTeacherInfo(String accessToken, String openId) {
        WechatUserInfo wechatUserInfo = getUserInformation(accessToken, openId);
        Customer teacher = null;
        return teacher;
    }

    @PostConstruct
    public void init() {
        weChatAuthorizeUtils = this;
    }
}
