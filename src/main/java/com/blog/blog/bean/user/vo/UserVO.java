package com.blog.blog.bean.user.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserVO {

    @TableId
    private String userId;
    private String userRealName;
    private String userAvatar;
    private String userPhone;
    private String userPassword;
    private String role;
    @JsonIgnore
    private Integer tokenVersion;
    private String miniProgramOpenId; // 小程序openId
    private Integer isSubscribe; // 是否关注公众号
    private String officialOpenId; // 公众号openId
    @JsonIgnore
    private String unionId; // unionId
    private Integer isLogin;//0为未登录，1为已登录
    private Integer isLock;//0为未锁定，1为已锁定
    private Integer isDelete;//0为未删除，1为已删除
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    // 非数据库字段
    private String code; //用于获取绑定的unionid
    private String verifyCode;//验证码
    private String token;

    private String keyword;
    private Long pageNo;
    private Long pageSize;

}
