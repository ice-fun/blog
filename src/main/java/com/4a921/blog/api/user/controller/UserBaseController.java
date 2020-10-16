package com.knowswift.myspringboot.api.user.controller;

import com.knowswift.myspringboot.api.officialAccounts.service.SystemLogService;
import com.knowswift.myspringboot.api.officialAccounts.service.UnbindSubscribeUserService;
import com.knowswift.myspringboot.api.user.service.UserCustomerService;
import com.knowswift.myspringboot.api.user.service.UserUserService;
import com.knowswift.myspringboot.task.AsyncTask;
import com.knowswift.myspringboot.utils.RedisUtils;

import javax.annotation.Resource;

/**
 * @author Li Yao Bing
 * @Company https://www.knowswift.com/
 **/


public abstract class UserBaseController {

    @Resource
    UnbindSubscribeUserService unbindSubscribeUserService;

    @Resource
    SystemLogService systemLogService;

    @Resource
    UserUserService userUserService;

    @Resource
    RedisUtils redisUtils;

    @Resource
    AsyncTask asyncTask;

    @Resource
    UserCustomerService userCustomerService;
}
