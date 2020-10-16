package com.knowswift.myspringboot.api.customer.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knowswift.myspringboot.api.customer.mapper.CustomerUserMapper;
import com.knowswift.myspringboot.bean.user.po.User;
import org.springframework.stereotype.Service;

/**
 * @author Li Yao Bing
 * @Company https://www.knowswift.com/
 **/

@Service
public class CustomerUserService extends ServiceImpl<CustomerUserMapper, User> {
}
