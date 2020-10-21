package com.blog.blog.api.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.blog.api.user.mapper.UserUserMapper;
import com.blog.blog.bean.user.po.User;
import com.blog.blog.config.PropertyConfig;
import com.blog.blog.security.AuthUserDetails;
import com.blog.blog.security.AuthUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * author：LiYaoBing
 */

@Service
public class UserUserService extends ServiceImpl<UserUserMapper, User> implements AuthUserDetailsService {


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user =  getOne(new LambdaQueryWrapper<User>().eq(User::getUserAccount, s));
        if (user == null) {
            throw new UsernameNotFoundException("无效的登录信息");
        }
        return user;
    }

    @Override
    public AuthUserDetails loadUserByAccount(String account) throws UsernameNotFoundException {
        return null;
    }

    @Override
    public AuthUserDetails loadUserByPhoneNumber(String phoneNumber) throws UsernameNotFoundException {
        User user = getOne(new LambdaQueryWrapper<User>().eq(User::getUserPhone, phoneNumber));
        if (user == null) {
            throw new UsernameNotFoundException("账号不存在");
        }
        return user;
    }

    @Override
    public AuthUserDetails loadUserByOpenId(String unionId, String openId) throws UsernameNotFoundException {
//        User user = getOne(new LambdaQueryWrapper<User>().eq(User::getMiniProgramOpenId, openId));
//        if (user == null) {
//            user = new User();
//            user.setRole(PropertyConfig.ROLE_VISITOR);
//            user.setIsLock(0);
//            user.setUnionId(unionId);
//            user.setMiniProgramOpenId(openId);
//        }
//        return user;
        return null;
    }

    @Override
    public AuthUserDetails loadUserByUnionId(String unionId, String openId) {
        return null;
    }
}
