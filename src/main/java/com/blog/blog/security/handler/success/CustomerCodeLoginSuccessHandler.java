package com.blog.blog.security.handler.success;

import com.blog.blog.api.customer.service.CustomerCustomerService;
import com.blog.blog.bean.customer.po.Customer;
import com.blog.blog.bean.customer.vo.CustomerVO;
import com.blog.blog.utils.JwtTokenUtils;
import com.blog.blog.utils.WrappedBeanCopier;
import com.blog.blog.security.handler.AbstractLoginSuccessHandler;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@Component
public class CustomerCodeLoginSuccessHandler extends AbstractLoginSuccessHandler {
    @Resource
    private CustomerCustomerService teacherService;

    @Override
    public Object preAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        Customer customer = (Customer) authentication.getPrincipal();
        customer.setTokenVersion(customer.getTokenVersion() + 1);
        teacherService.updateById(customer);
        Map<String, Object> claims = new HashMap<>();
        claims.put("tokenVersion", customer.getTokenVersion());
        String token = JwtTokenUtils.generateToken(claims, customer.getUsername());
        CustomerVO customerVO = WrappedBeanCopier.copyProperties(customer, CustomerVO.class);
        customerVO.setToken(token);
        return customerVO;
    }
}
