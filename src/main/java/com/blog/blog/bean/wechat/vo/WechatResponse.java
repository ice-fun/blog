package com.blog.blog.bean.wechat.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WechatResponse {
    @JSONField(name = "openid")
    @JsonProperty("openid")
    private String openId;
    private String nickName;
    private String gender;
    private String city;
    private String province;
    private String country;
    private String avatarUrl;
    @JSONField(name = "unionid")
    @JsonProperty("unionid")
    private String unionId;
    @JSONField(name = "session_key")
    @JsonProperty("session_key")
    private String sessionKey;
    @JSONField(name = "errCode")
    @JsonProperty("errCode")
    private String errorCode;
    @JSONField(name = "errmsg")
    @JsonProperty("errmsg")
    private String errormsg;

    private WechatResponse userInfo;

    @JSONField(name = "access_token")
    private String accessToken;
}
