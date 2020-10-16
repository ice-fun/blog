package com.knowswift.myspringboot.utils.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.knowswift.myspringboot.bean.wechat.vo.WechatResponse;
import com.knowswift.myspringboot.config.PropertyConfig;
import com.knowswift.myspringboot.utils.RedisUtils;
import com.knowswift.myspringboot.utils.wechat.aes.AesException;
import com.knowswift.myspringboot.utils.wechat.aes.WXBizMsgCrypt;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class WechatMiniProgramUtils {

    private static final String ACCESS_TOKEN_KEY_PARENT = "MimiAccessTokenParent";
    private static final String ACCESS_TOKEN_KEY_TEACHER = "MimiAccessTokenTeacher";

    private static WechatMiniProgramUtils wechatMiniProgramUtils;

    @PostConstruct
    public void init() {
        wechatMiniProgramUtils = this;
    }

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private WXBizMsgCrypt wxBizMsgCrypt;

    /**
     * 小程序授权登录
     *
     * @param code
     * @param id
     * @param secret
     * @return
     */
    private static WechatResponse miniProgramAuthorization(String code, String id, String secret) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        Map<String, String> params = new HashMap<String, String>();
        params.put("APPID", id);
        params.put("SECRET", secret);
        params.put("CODE", code);
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid={APPID}&secret={SECRET}&js_code={CODE}&grant_type=authorization_code";
        String response = restTemplate.getForObject(url, String.class, params);
        System.out.println(response);
        WechatResponse wechatResponse = JSON.parseObject(response, WechatResponse.class);
        System.out.println(wechatResponse);
        return wechatResponse;
    }

    public static WechatResponse customerMiniProgramAuthorization(String code) {
        return miniProgramAuthorization(code, PropertyConfig.MINI_ID_ONE, PropertyConfig.MINI_SECRET_ONE);
    }

    public static WechatResponse userMiniProgramAuthorization(String code) {
        return miniProgramAuthorization(code, PropertyConfig.MINI_ID_TWO, PropertyConfig.MINI_SECRET_TWO);
    }

    public static String getMiniProgramAccessToken(String token, String id, String secret) {
        String accessToken = (String) wechatMiniProgramUtils.redisUtils.get(id + token);
        if (accessToken != null) {
            return accessToken;
        }
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        Map<String, String> params = new HashMap<String, String>();
        params.put("APPID", id);
        params.put("APPSECRET", secret);
        params.put("GRANT_TYPE", "client_credential");
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type={GRANT_TYPE}&appid={APPID}&secret={APPSECRET}";
        String response = restTemplate.getForObject(url, String.class, params);
        JSONObject responseObject = JSONObject.parseObject(response);
        accessToken = responseObject.getString("access_token");
        wechatMiniProgramUtils.redisUtils.set(id + token, accessToken, 7000);
        return accessToken;
    }

    public static String getCustomerMiniProgramAccessToken() {
        return getMiniProgramAccessToken(ACCESS_TOKEN_KEY_TEACHER, PropertyConfig.MINI_ID_ONE, PropertyConfig.MINI_SECRET_ONE);
    }

    public static String getUserMiniProgramAccessToken() {
        return getMiniProgramAccessToken(ACCESS_TOKEN_KEY_PARENT, PropertyConfig.MINI_ID_TWO, PropertyConfig.MINI_SECRET_TWO);
    }


    public static WechatResponse decrypt(String info, String encodingAesKey) {
        JSONObject object = JSONObject.parseObject(info);
        String encryptedData = object.getString("encryptedData");
        String iv = object.getString("iv");
        try {
            return wechatMiniProgramUtils.wxBizMsgCrypt.miniDecrypt(encryptedData, encodingAesKey, iv);
        } catch (AesException e) {
            e.printStackTrace();
        }
        return null;
    }
}
