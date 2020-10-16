package com.knowswift.myspringboot.utils.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.knowswift.myspringboot.bean.wechat.vo.*;
import com.knowswift.myspringboot.config.PropertyConfig;
import com.knowswift.myspringboot.utils.RedisUtils;
import com.knowswift.myspringboot.utils.XMLTranslateUtils;
import com.knowswift.myspringboot.utils.wechat.aes.AesException;
import com.knowswift.myspringboot.utils.wechat.aes.WXBizMsgCrypt;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.knowswift.myspringboot.utils.wechat.WechatMiniProgramUtils.getCustomerMiniProgramAccessToken;
import static com.knowswift.myspringboot.utils.wechat.WechatMiniProgramUtils.getUserMiniProgramAccessToken;

@Component
public class WechatOfficialAccountUtils {
    private static final String ACCESS_TOKEN_KEY = "wechatOfficialAccountsAccessToken";
    private static final String ERROR_BUSY = "-1";
    private static final String ERROR_SUCCESS = "0";
    private static final String ERROR_INVALID_TOKEN = "40001";
    private static final String ERROR_INVALID_CERTIFICATE = "40001";
    private static final String ERROR_INVALID_USER_ID = "40003";
    private static final String ERROR_MEDIA_FILE_TYPE = "40004";
    private static final String ERROR_FILE_TYPE = "40005";
    private static final String ERROR_FILE_SIZE = "40006";
    private static WechatOfficialAccountUtils wechatOfficialAccountUtils;


    @Resource
    private RedisUtils redisUtils;
    @Resource
    private WXBizMsgCrypt wxBizMsgCrypt;


    public static String getOfficialAccountsAccessToken() {
        String accessToken = (String) wechatOfficialAccountUtils.redisUtils.get(PropertyConfig.WECHAT_OFFICIAL_ACCOUNTS_APP_ID + ACCESS_TOKEN_KEY);
        if (accessToken != null) {
            return accessToken;
        }
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        Map<String, String> params = new HashMap<String, String>();
        params.put("APPID", PropertyConfig.WECHAT_OFFICIAL_ACCOUNTS_APP_ID);
        params.put("APPSECRET", PropertyConfig.WECHAT_OFFICIAL_ACCOUNTS_APP_SECRET);
        params.put("GRANT_TYPE", "client_credential");
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type={GRANT_TYPE}&appid={APPID}&secret={APPSECRET}";
        String response = restTemplate.getForObject(url, String.class, params);
        JSONObject responseObject = JSONObject.parseObject(response);
        accessToken = responseObject.getString("access_token");
        wechatOfficialAccountUtils.redisUtils.set(PropertyConfig.WECHAT_OFFICIAL_ACCOUNTS_APP_ID + ACCESS_TOKEN_KEY, accessToken, 7000);
        return accessToken;
    }

    public static WechatUserInfo getUserInfo(String openId) {
        Map<String, String> map = new HashMap<>();
        RestTemplate restTemplate = new RestTemplate();
        map.put("ACCESS_TOKEN", getOfficialAccountsAccessToken());
        map.put("OPENID", openId);
        String result = restTemplate.getForObject("https://api.weixin.qq.com/cgi-bin/user/info?access_token={ACCESS_TOKEN}&openid={OPENID}&lang=zh_CN", String.class, map);
        WechatUserInfo wechatUserInfo = JSON.parseObject(result, WechatUserInfo.class);

        if (ERROR_INVALID_TOKEN.equals(wechatUserInfo.getErrCode())) {
            wechatOfficialAccountUtils.redisUtils.del(ACCESS_TOKEN_KEY);
            //TODO 记录异常
            wechatUserInfo = null;
        }
        return wechatUserInfo;
    }

    public static String createEncodeReply(String from, String to, String content, String timestamp, String nonce) throws AesException {
        PlaintextWechatMessage reply = new PlaintextWechatMessage();
        reply.setFromUserName(from);
        reply.setToUserName(to);
        reply.setMsgType("text");
        reply.setCreateTime(System.currentTimeMillis() / 1000);
        reply.setContent(content);
        String xml = XMLTranslateUtils.convertToXmlIgnoreXmlHead(reply, "UTF-8");
        return wechatOfficialAccountUtils.wxBizMsgCrypt.encryptMsg(xml, timestamp, nonce);
    }

    /**
     * @param openId     用户 公众号openId
     * @param templateId 模板id
     * @param miniAppId  小程序appId
     * @param pathPage   小程序跳转url
     * @param url        H5跳转url
     * @param values     模板对应的参数
     * @return
     */
    public static boolean publishMessage(String openId, String templateId, String miniAppId, String pathPage, String url, String[] values) {
        RestTemplate restTemplate = new RestTemplate();
        String sendUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + getOfficialAccountsAccessToken();
        TemplateMessageRequest templateMessageRequest = new TemplateMessageRequest();
        templateMessageRequest.setToUser(openId);
        templateMessageRequest.setTemplateId(templateId);
        templateMessageRequest.setAppId(miniAppId);
        templateMessageRequest.setPathPage(pathPage);
        templateMessageRequest.setUrl(url);
        templateMessageRequest.setValues(values);
        System.out.println(templateMessageRequest);
        String sendResult = restTemplate.postForObject(sendUrl, templateMessageRequest, String.class);
        if (!"0".equals(JSONObject.parseObject(sendResult, WechatResponse.class).getErrorCode())) {
            //TODO 记录消息发送失败情况
            System.out.println(sendResult);
            return false;
        }
        return true;
    }

    /**
     * 拉取已关注了微信公众号的用户，微信一次性最多返回一万条记录，因此使用递归的方式
     *
     * @param nextOpenId
     * @return
     */
    public static List<String> pullSubscribeUser(String nextOpenId) {
        RestTemplate restTemplate = new RestTemplate();
        String accessToken = getOfficialAccountsAccessToken();
        String val1 = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=" + accessToken;
        String val2 = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=" + accessToken + "&next_openid=" + nextOpenId;
        String sendUrl = nextOpenId == null ? val1 : val2;
        String sendResult = restTemplate.getForObject(sendUrl, String.class);
        JSONObject jsonObject = JSONObject.parseObject(sendResult);
        int total = jsonObject.getIntValue("total");
        int count = jsonObject.getIntValue("count");
        JSONObject data = jsonObject.getObject("data", JSONObject.class);
        JSONArray array = data.getJSONArray("openid");
        String nextOne = jsonObject.getString("next_openid");
        List<String> strings = array.toJavaList(String.class);
        if (total > 10000 && count == 10000) {
            List<String> strings1 = pullSubscribeUser(nextOne);
            strings.addAll(strings1);
        }
        return strings;
    }


    public static List<Object> pullOfficialText() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        String accessToken = getOfficialAccountsAccessToken();
        String sendUrl = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=" + accessToken;
        Map<String, Object> params = new HashMap<>();
        params.put("type", "news");
        params.put("offset", 0);
        params.put("count", 20);
        String post = restTemplate.postForObject(sendUrl, params, String.class);
        WeChatOfficialMaterial material = JSONObject.parseObject(post, WeChatOfficialMaterial.class);
        System.out.println(material);
        return null;
    }


    /**
     * 调用微信珊瑚安全接口，检测内容是否含有敏感词汇
     *
     * @param isTeacher 是否是教师端 true 为教师  false为家长端
     * @param content   文本内容
     * @return true 为正常  false 为含有敏感信息
     */
    public static boolean checkText(boolean isTeacher, String content) {
        if (StringUtils.isBlank(content)) {
            return true;
        }
        String token = isTeacher ? getCustomerMiniProgramAccessToken() : getUserMiniProgramAccessToken();
        RestTemplate restTemplate = new RestTemplate();
        JSONObject json = new JSONObject();
        json.put("content", content);
        String body = restTemplate.postForObject("https://api.weixin.qq.com/wxa/msg_sec_check?access_token=" + token, json, String.class);
        JSONObject object = JSONObject.parseObject(body);
        return object.get("errcode").equals(0);
    }


    public static boolean checkImage(boolean isTeacher, String fileUrl) throws IOException {
        if (StringUtils.isBlank(fileUrl)) {
            return true;
        }
        String token;
        if (isTeacher) {
            token = getCustomerMiniProgramAccessToken();
        } else {
            token = getUserMiniProgramAccessToken();
        }
        String checkUrl = "https://api.weixin.qq.com/wxa/img_sec_check?access_token=" + token;
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的
        HttpClient httpclient = HttpClients.createDefault();
        //创建一个post请求
        HttpPost request = new HttpPost(checkUrl);
        //设置响应头   （ application/octet-stream：二进制流，不知道下载文件类型）
        request.addHeader("Content-Type", "application/octet-stream");
        //输入流,获取输入图片的输入流
        FileOutputStream fos = null;
        InputStream bis = null;
        HttpURLConnection httpUrl = null;
        URL url = null;
        int size = 0;
        try {
            url = new URL(fileUrl);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            bis = httpUrl.getInputStream();
            //创建一个byte数组，和输入的文件输入流大小一样
            byte[] byt = new byte[httpUrl.getContentLength()];
            ArrayList<Byte> bytes = new ArrayList<>();
            //从输入流中读取全部，并将其存储在缓冲区数组byt 中。
            int available = bis.available();
            byte[] buf = new byte[available];
            fos = new FileOutputStream("test1.jpg");
            while ((size = bis.read(buf)) != -1) {
                fos.write(buf, 0, size);
            }

            FileInputStream fileInputStream = new FileInputStream("test1.jpg");
            //            boolean image = checkImage(httpclient, request, bis);
            //TODO 删除缓存图片
            return checkImage(httpclient, request, fileInputStream);
        } catch (IOException | ClassCastException ignored) {
        } finally {
            try {

                assert bis != null;
                bis.close();
                httpUrl.disconnect();
            } catch (IOException | NullPointerException ignored) {
            }
        }
        return false;
//        return checkImage(httpclient, request, bis);
    }

    public static boolean checkImage(boolean isTeacher, MultipartFile file) throws IOException {
        if (file == null) {
            return false;
        }
        String token;
        if (isTeacher) {
            token = getCustomerMiniProgramAccessToken();
        } else {
            token = getUserMiniProgramAccessToken();
        }
        String url = "https://api.weixin.qq.com/wxa/img_sec_check?access_token=" + token;
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的
        HttpClient httpclient = HttpClients.createDefault();
        //创建一个post请求
        HttpPost request = new HttpPost(url);
        //设置响应头   （ application/octet-stream：二进制流，不知道下载文件类型）
        request.addHeader("Content-Type", "application/octet-stream");
        //输入流,获取输入图片的输入流
        InputStream inputStream = file.getInputStream();
        return checkImage(httpclient, request, inputStream);
    }

    public static boolean checkImage(boolean isTeacher) throws IOException {

        String token;
        if (isTeacher) {
            token = getCustomerMiniProgramAccessToken();
        } else {
            token = getUserMiniProgramAccessToken();
        }
        String url = "https://api.weixin.qq.com/wxa/img_sec_check?access_token=" + token;
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的
        HttpClient httpclient = HttpClients.createDefault();
        //创建一个post请求
        HttpPost request = new HttpPost(url);
        //设置响应头   （ application/octet-stream：二进制流，不知道下载文件类型）
        request.addHeader("Content-Type", "application/octet-stream");
        //输入流,获取输入图片的输入流
        FileInputStream fileInputStream = new FileInputStream(new File("C:\\Users\\Admin\\Desktop\\test1.jpg"));
//        InputStream inputStream = file.getInputStream();
        return checkImage(httpclient, request, fileInputStream);
    }

    private static boolean checkImage(HttpClient httpclient, HttpPost request, InputStream inputStream) {
        try {
            //创建一个byte数组，和输入的文件输入流大小一样
            byte[] byt = new byte[inputStream.available()];
            //从输入流中读取全部，并将其存储在缓冲区数组byt 中。
            inputStream.read(byt);
            //定制提交内容
            request.setEntity(new ByteArrayEntity(byt, ContentType.create("image/jpg")));
            //// 由客户端执行(发送)请求,执行校验
            HttpResponse response = httpclient.execute(request);
            // 从响应模型中获取响应实体
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");// 转成string
            //打印校验结果
            System.out.println("result：" + result);
            JSONObject object = JSONObject.parseObject(result);
            return object.get("errcode").equals(0);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    public static void main(String[] args) {

    }

    @PostConstruct
    public void init() {
        wechatOfficialAccountUtils = this;
    }
}
