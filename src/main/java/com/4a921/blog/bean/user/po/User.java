package com.knowswift.myspringboot.bean.user.po;


import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
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

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements AuthUserDetails {

    @TableId
    private String userId;
    private String userRealName;
    private String userAvatar;
    private String userPhone;
    private String userPassword;
    private String childId;
    private String role;
    private Integer tokenVersion;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String miniProgramOpenId;
    private Integer isSubscribe;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String officialOpenId;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String unionId;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Integer isLogin;//0为未登录，1为已登录
    private Integer isLock;//0为未锁定，1为已锁定
    @TableField(value = "is_delete", fill = FieldFill.INSERT)
    private Integer isDelete;//0为未删除，1为已删除
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isLock == 0;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Integer> getTokenVersionMap() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("tokenVersion", tokenVersion);
        return map;
    }

    @Override
    public String getId() {
        return userId;
    }

    @Override
    public String getUnionId() {
        return unionId;
    }

    @Override
    public String getRole() {
        return role;
    }
}
