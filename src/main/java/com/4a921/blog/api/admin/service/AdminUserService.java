package com.knowswift.myspringboot.api.admin.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knowswift.myspringboot.api.admin.mapper.AdminUserMapper;
import com.knowswift.myspringboot.bean.user.po.User;
import org.springframework.stereotype.Service;

/**
 * @author Li Yao Bing
 * @Company https://www.knowswift.com/
 **/

@Service
public class AdminUserService extends ServiceImpl<AdminUserMapper, User> {
}
