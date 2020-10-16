package com.blog.blog.task;

import com.blog.blog.api.customer.service.CustomerCustomerService;
import com.blog.blog.api.user.service.UserUserService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class AsyncTask {


    @Resource
    CustomerCustomerService customerCustomerService;

    @Resource
    UserUserService userUserService;

    @Async("taskExecutor")
    public void task() {

    }

}
