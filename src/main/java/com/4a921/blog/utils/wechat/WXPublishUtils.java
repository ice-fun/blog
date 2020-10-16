package com.knowswift.myspringboot.utils.wechat;

import com.alibaba.fastjson.JSONObject;
import com.knowswift.myspringboot.config.PropertyConfig;
import com.knowswift.myspringboot.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class WXPublishUtils {
    private static String[] ids = new String[]{"oS44vv72z-R0dpf8tURpjfkaq6uA", "oS44vv4BHU3q2dqUaQ1FtlEIy9Ac", "oS44vv-vdMimeqU8oU76r76c4xjc"};


    private static WXPublishUtils wxPublishUtils;


    @Resource
    RedisUtils redisUtils;

    public static boolean publishMessage(String openId, String templateId, String url, Object data) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        //获取redis中的 accessToken
        String accessToken = (String) wxPublishUtils.redisUtils.get("access_token");
//        String accessToken = "";
        if (StringUtils.isBlank(accessToken)) {
            Map<String, String> param1 = new HashMap<>();
            param1.put("GRANT_TYPE", "client_credential");
            param1.put("APPID", PropertyConfig.WECHAT_OFFICIAL_ACCOUNTS_APP_ID);
            param1.put("SECRET", PropertyConfig.WECHAT_OFFICIAL_ACCOUNTS_APP_SECRET);
            String accessTokenResult = restTemplate.getForObject("https://api.weixin.qq.com/cgi-bin/token?grant_type={GRANT_TYPE}&appid={APPID}&secret={SECRET}", String.class, param1);
            accessToken = JSONObject.parseObject(accessTokenResult).getString("access_token");
            if (accessToken == null) {
                JSONObject failResult = JSONObject.parseObject(accessTokenResult);
                return false;
            }
            //保存到redis
            wxPublishUtils.redisUtils.set("access_token", accessToken, 7000);
        }

        //请求推送
        String sendUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken;
        Map<String, Object> param2 = new HashMap<>();
        param2.put("touser", openId);
        param2.put("template_id", templateId);
        param2.put("url", url);
        param2.put("data", data);
        String sendResult = restTemplate.postForObject(sendUrl, param2, String.class);
        if (!JSONObject.parseObject(sendResult).getInteger("errcode").equals(0)) {
            return false;
        }

        return true;
    }


    public static void main(String[] args) {
//        JSONObject jsonObject1 = JSONObject.parseObject("{\n" +
//                "                   \"first\": {\n" +
//                "                       \"value\":\"您好，您的订单已接单成功！\",\n" +
//                "                   },\n" +
//                "                   \"keyword1\":{\n" +
//                "                       \"value\":\"搬运\",\n" +
//                "                   },\n" +
//                "                   \"keyword2\": {\n" +
//                "                       \"value\":\"搬运\",\n" +
//                "                   },\n" +
//                "                   \"keyword3\": {\n" +
//                "                       \"value\":\"李四\",\n" +
//                "                   },\n" +
//                "                   \"keyword4\": {\n" +
//                "                       \"value\":\"13800138000\",\n" +
//                "                   },\n" +
//                "                   \"keyword5\": {\n" +
//                "                       \"value\":\"13800138000\",\n" +
//                "                   },\n" +
//                "                   \"remark\":{\n" +
//                "                       \"value\":\"点击此处查看详细信息！\",\n" +
//                "                   }\n" +
//                "           }");
//        publishTest( "xtMBK2xW0bWGZna9OPx5KVYkQaiak5l0ZjTY0Qy1i9M", "http://weixin.qq.com/download", jsonObject1);
        String type = "预约订单";
    }

    @PostConstruct
    public void init() {
        wxPublishUtils = this;
    }


}
