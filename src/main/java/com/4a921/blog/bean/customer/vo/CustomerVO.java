package com.knowswift.myspringboot.bean.customer.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerVO {
    @TableId
    private String customerId;
    private String customerName;
    private String customerPhone;
    @JsonIgnore
    private String customerPassword;
    private String role;
    @JsonIgnore
    private Integer tokenVersion;
    private String miniProgramOpenId;
    private Integer isSubscribe;
    private String officialOpenId;
    @JsonIgnore
    private String unionId;
    private Integer isLogin;
    private Integer isDelete;//0为未删除，1为已删除
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    // 非数据库字段
    @Min(value = 1, message = "页数不可小于1")
    private Long pageNo;//页数
    @Min(value = 1, message = "页大小不可小于1")
    private Long pageSize;//页大小
    private String code;
    private String verifyCode;//验证码
    private String token;

}
