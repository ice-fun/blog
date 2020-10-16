package com.blog.blog.api.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.blog.bean.admin.po.Admin;
import com.blog.blog.api.admin.mapper.AdminAdminMapper;
import com.blog.blog.security.AuthUserDetails;
import com.blog.blog.security.AuthUserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 账号对象的service类，实现AuthUserDetailsService接口，用于spring security 登录、鉴权
 */
@Service
public class AdminAdminService extends ServiceImpl<AdminAdminMapper, Admin> implements AuthUserDetailsService {

    @Override
    public AuthUserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Admin admin = getOne(new LambdaQueryWrapper<Admin>().eq(Admin::getAdminAccount, s));
        if (admin == null) {
            throw new UsernameNotFoundException("该账号未注册");
        }
        return admin;
    }

    @Override
    public AuthUserDetails loadUserByAccount(String account) throws UsernameNotFoundException {
        return loadUserByUsername(account);
    }

    @Override
    public AuthUserDetails loadUserByPhoneNumber(String phoneNumber) throws UsernameNotFoundException {
        Admin admin = getOne(new LambdaQueryWrapper<Admin>().eq(Admin::getAdminPhone, phoneNumber));
        if (admin == null) {
            throw new UsernameNotFoundException("该手机未注册");
        }
        return admin;
    }

    @Override
    public AuthUserDetails loadUserByOpenId(String unionId, String openId) throws UsernameNotFoundException {
        return null;
    }

    @Override
    public AuthUserDetails loadUserByUnionId(String unionId, String openId) {
        return null;
    }
}
