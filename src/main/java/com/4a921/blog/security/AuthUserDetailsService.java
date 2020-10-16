package com.knowswift.myspringboot.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 自定义的登录方法，提供账号、手机号码、微信openId,unionID, 可以根据实际需求，添加其他登录方法。
 */
public interface AuthUserDetailsService extends UserDetailsService {
    /**
     * @param account 账号
     * @return 用户信息
     * @throws UsernameNotFoundException 找不到用户时抛出
     *                                   <p>
     *                                   账号登陆
     */
    AuthUserDetails loadUserByAccount(String account) throws UsernameNotFoundException;

    /**
     * @param phoneNumber 手机号码
     * @return 用户信息
     * @throws UsernameNotFoundException 找不到用户时抛出
     *                                   <p>
     *                                   手机登陆
     */
    AuthUserDetails loadUserByPhoneNumber(String phoneNumber) throws UsernameNotFoundException;

    /**
     * @param openId 小程序下openId
     * @return 用户信息
     * <p>
     * 微信小程序登陆
     */
    AuthUserDetails loadUserByOpenId(String unionId, String openId);

    /**
     * @param unionId 微信unionId
     * @return 用户信息
     * <p>
     * 微信小程序登陆
     */
    AuthUserDetails loadUserByUnionId(String unionId, String openId);
}
