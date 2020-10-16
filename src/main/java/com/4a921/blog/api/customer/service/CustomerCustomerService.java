package com.knowswift.myspringboot.api.customer.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knowswift.myspringboot.api.customer.mapper.CustomerCustomerMapper;
import com.knowswift.myspringboot.bean.customer.po.Customer;
import com.knowswift.myspringboot.config.PropertyConfig;
import com.knowswift.myspringboot.security.AuthUserDetails;
import com.knowswift.myspringboot.security.AuthUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerCustomerService extends ServiceImpl<CustomerCustomerMapper, Customer> implements AuthUserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Customer customer = getById(s);
        if (customer == null) {
            throw new UsernameNotFoundException("该手机未注册");
        }
        return customer;
    }

    @Override
    public AuthUserDetails loadUserByAccount(String account) throws UsernameNotFoundException {
        return null;
    }

    @Override
    public AuthUserDetails loadUserByPhoneNumber(String phoneNumber) throws UsernameNotFoundException {
        Customer customer = getOne(new LambdaQueryWrapper<Customer>().eq(Customer::getCustomerPhone, phoneNumber));
        if (customer == null) {
            throw new UsernameNotFoundException("该手机未注册");
        }
        return customer;
    }

    @Override
    public AuthUserDetails loadUserByOpenId(String unionId, String openId) throws UsernameNotFoundException {
        Customer customer = getOne(new LambdaQueryWrapper<Customer>().eq(Customer::getMiniProgramOpenId, openId));
        if (customer == null) {
            customer = new Customer();
            customer.setRole(PropertyConfig.ROLE_VISITOR);
            customer.setMiniProgramOpenId(openId);
            customer.setUnionId(unionId);
            return customer;
        }
        return customer;
    }

    @Override
    public AuthUserDetails loadUserByUnionId(String unionId, String openId) {
        Customer customer = getOne(new LambdaQueryWrapper<Customer>().eq(Customer::getUnionId, unionId));
        if (customer == null) {
            customer = new Customer();
            customer.setRole(PropertyConfig.ROLE_VISITOR);
            customer.setOfficialOpenId(openId);
            customer.setUnionId(unionId);
            return customer;
        }
        return customer;
    }
}
