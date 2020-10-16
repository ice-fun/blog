package com.knowswift.myspringboot.api.user.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knowswift.myspringboot.api.user.mapper.UserCustomerMapper;
import com.knowswift.myspringboot.bean.customer.po.Customer;
import org.springframework.stereotype.Service;

/**
 * @author Li Yao Bing
 * @Company https://www.knowswift.com/
 **/

@Service
public class UserCustomerService extends ServiceImpl<UserCustomerMapper, Customer> {
}
