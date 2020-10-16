package com.blog.blog.api.user.controller;

import com.blog.blog.api.system.service.SystemLogService;
import com.blog.blog.api.system.service.UnbindSubscribeUserService;
import com.blog.blog.api.user.service.UserCustomerService;
import com.blog.blog.task.AsyncTask;
import com.blog.blog.utils.RedisUtils;
import com.blog.blog.api.user.service.UserUserService;

import javax.annotation.Resource;

/**
 * @author Li Yao Bing**/


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
