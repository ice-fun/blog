package com.blog.blog.security;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

/**
 * 自定义的用户鉴权信息接口，为了方便使用不同的登录方法，
 * 每个账号类的类都应该继承此接口
 */
public interface AuthUserDetails extends UserDetails {
    /**
     * @return 返回token版本
     */
    Map<String, Integer> getTokenVersionMap();

    /**
     * @return 返回id
     */
    String getId();

    String getUnionId();

    String getRole();
}
