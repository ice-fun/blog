package com.blog.blog.bean.admin.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Li Yao Bing* @Date 2020/7/13
 **/

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminVO {

    @TableId
    private String adminId;
    private String adminName;
    private String adminPhone;
    private String adminAccount;
    @JsonIgnore
    private String adminPassword;
    private String kindergartenId;
    private String role;
    private Integer adminTokenVersion;
    private Integer kindergartenTokenVersion;
    private Integer isLock;
    @TableField(value = "is_delete", fill = FieldFill.INSERT)
    private Integer isDelete;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    private String oldPassword;
    private String newPassword;
}
