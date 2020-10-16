package com.blog.blog.bean.wechat.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WechatUserInfo {
    private String subscribe;
    @JsonProperty("openid")
    @JSONField(name = "openid")
    private String openId;

    private String nickname;
    private Integer sex;

    private String language;
    private String city;
    private String province;
    private String country;
    @JsonProperty("headimgurl")
    @JSONField(name = "headimgurl")
    private String headImgUrl;
    @JsonProperty("subscribe_time")
    @JSONField(name = "subscribe_time")
    private String subscribeTime;
    @JsonProperty("unionid")
    @JSONField(name = "unionid")
    private String unionId;
    private String remark;
    @JsonProperty("groupid")
    @JSONField(name = "groupid")
    private String groupId;
    @JsonProperty("tagid_list")
    @JSONField(name = "tagid_list")
    private List tagIdList;
    @JsonProperty("subscribe_scene")
    @JSONField(name = "subscribe_scene")
    private String subscribeScene;
    @JsonProperty("qr_scene")
    @JSONField(name = "qr_scene")
    private Integer qrScene;
    @JsonProperty("qr_scene_str")
    @JSONField(name = "qr_scene_str")
    private String qrSceneStr;


    @JsonProperty("errcode")
    @JSONField(name = "errcode")
    private String errCode;
    @JsonProperty("errmsg")
    @JSONField(name = "errmsg")
    private Error errMsg;
}
