package com.knowswift.myspringboot.api.customer.controller;

import com.knowswift.myspringboot.api.customer.service.CustomerCustomerService;
import com.knowswift.myspringboot.api.customer.service.CustomerUserService;
import com.knowswift.myspringboot.api.officialAccounts.service.UnbindSubscribeUserService;
import com.knowswift.myspringboot.task.AsyncTask;
import com.knowswift.myspringboot.utils.RedisUtils;

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
