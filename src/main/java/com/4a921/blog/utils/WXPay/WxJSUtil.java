package com.knowswift.myspringboot.utils.WXPay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.knowswift.myspringboot.config.PropertyConfig;
import com.knowswift.myspringboot.utils.RedisUtils;
import com.knowswift.myspringboot.utils.wechat.WechatOfficialAccountUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Li Yao Bing
 * @Company https://www.knowswift.com/
 * @Date 2020/4/17
 **/

@Component
public class WxJSUtil {

    public static final String JS_API_TICKET = "JsApiTicket";
    private static WxJSUtil wxJSUtil;
    @Resource
    private RedisUtils redisUtils;

    public static String getJsAPITicket() {
        String ticket = (String) wxJSUtil.redisUtils.get(JS_API_TICKET + PropertyConfig.WECHAT_OFFICIAL_ACCOUNTS_APP_ID);
        if (ticket != null) {
            return ticket;
        }
        String accessToken = WechatOfficialAccountUtils.getOfficialAccountsAccessToken();
        Map<String, String> map = new HashMap<>();
        RestTemplate restTemplate = new RestTemplate();
        map.put("ACCESS_TOKEN", accessToken);
        String result = restTemplate.getForObject("https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token={ACCESS_TOKEN}&type=jsapi", String.class, map);
        JSONObject object = JSON.parseObject(result);
        ticket = object.getString("ticket");
        wxJSUtil.redisUtils.set(JS_API_TICKET + PropertyConfig.WECHAT_OFFICIAL_ACCOUNTS_APP_ID, ticket, 7000);
        return ticket;
    }

    public static Map<String, String> sign(String url) {
        String ticket = getJsAPITicket();
        String nonceStr = WXPayUtil.generateNonceStr();
        long timestamp = WXPayUtil.getCurrentTimestamp();
        String signature = null;
        String string = String.format("jsapi_ticket=%s&noncestr=%s&timestamp=%s&url=%s", ticket, nonceStr, timestamp, url);
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Map<String, String> map = new HashMap<>();
        map.put("ticket", ticket);
        map.put("nonceStr", nonceStr);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("signature", signature);
        return map;

    }

    private static String byteToHex(byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    @PostConstruct
    public void init() {
        wxJSUtil = this;
    }
}
