package com.blog.blog.api.customer.controller;

import com.blog.blog.api.customer.service.CustomerCustomerService;
import com.blog.blog.api.customer.service.CustomerUserService;
import com.blog.blog.api.system.service.UnbindSubscribeUserService;
import com.blog.blog.task.AsyncTask;
import com.blog.blog.utils.RedisUtils;

import javax.annotation.Resource;


public abstract class CustomerBaseController {

    @Resource
    protected CustomerCustomerService customerCustomerService;


    @Resource
    protected UnbindSubscribeUserService unbindSubscribeUserService;

    @Resource
    protected RedisUtils redisUtils;

    @Resource
    protected AsyncTask asyncTask;

    @Resource
    protected CustomerUserService customerUserService;

}
