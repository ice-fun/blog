package com.blog.blog.api.user.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.blog.bean.customer.po.Customer;
import com.blog.blog.api.user.mapper.UserCustomerMapper;
import org.springframework.stereotype.Service;

/**
 * @author Li Yao Bing**/

@Service
public class UserCustomerService extends ServiceImpl<UserCustomerMapper, Customer> {
}
