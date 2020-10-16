package com.knowswift.myspringboot.bean.admin.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.knowswift.myspringboot.security.AuthUserDetails;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 账号的实体类 继承 AuthUserDetails
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Admin implements AuthUserDetails {

    @TableId
    private String adminId;
    private String adminName;
    private String adminPhone;
    private String adminAccount;
    @JsonIgnore
    private String adminPassword;
    private String role;
    /*
     实际场景中，如果一个账号能登录多个终端程序，
     应该为每个程序都设置一个tokenVersion属性，
     便于同时在多个程序登录
     */
    private Integer adminTokenVersion;
    private Integer userTokenVersion;
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

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return adminPassword;
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return adminAccount;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return isLock == 0;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    @JsonIgnore
    @Override
    public Map<String, Integer> getTokenVersionMap() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("adminTokenVersion", adminTokenVersion);
        map.put("userTokenVersion", userTokenVersion);
        return map;
    }

    @JsonIgnore
    @Override
    public String getId() {
        return adminId;
    }

    @Override
    public String getUnionId() {
        return null;
    }

    @Override
    public String getRole() {
        return role;
    }
}
