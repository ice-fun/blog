package com.blog.blog.api.customer.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.blog.bean.user.po.User;
import com.blog.blog.api.customer.mapper.CustomerUserMapper;
import org.springframework.stereotype.Service;

/**
 * @author Li Yao Bing**/

@Service
public class CustomerUserService extends ServiceImpl<CustomerUserMapper, User> {
}
