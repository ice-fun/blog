package com.knowswift.myspringboot.bean.wechat.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

/**
 *
 * 用户保存关注公众号但还没绑定小程序额用户
 * 把这类用户保存下来 在小程序绑定时 再根据unionid把openid绑定上
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UnbindSubscribeUser {
    @TableId
    private String unbindSubscribeUserId;
    private String unbindSubscribeUserOpenId;
    private String unbindSubscribeUserUnionId;
    @TableField(value = "is_delete", fill = FieldFill.INSERT)
    private Integer isDelete;//0为未删除，1为已删除
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time", fill = FieldFill.INSERT)
    private LocalDateTime updateTime;
}
