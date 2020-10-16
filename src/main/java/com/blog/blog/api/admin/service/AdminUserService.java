package com.blog.blog.api.admin.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.blog.bean.user.po.User;
import com.blog.blog.api.admin.mapper.AdminUserMapper;
import org.springframework.stereotype.Service;

/**
 * @author Li Yao Bing**/

@Service
public class AdminUserService extends ServiceImpl<AdminUserMapper, User> {
}
