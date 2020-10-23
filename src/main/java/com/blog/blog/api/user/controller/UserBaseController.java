package com.blog.blog.api.user.controller;

import com.blog.blog.api.system.service.SystemLogService;
import com.blog.blog.api.system.service.UnbindSubscribeUserService;
import com.blog.blog.api.user.service.UserArticleService;
import com.blog.blog.api.user.service.UserCommentService;
import com.blog.blog.api.user.service.UserCustomerService;
import com.blog.blog.task.AsyncTask;
import com.blog.blog.utils.RedisUtils;
import com.blog.blog.api.user.service.UserUserService;

import javax.annotation.Resource;

/**
 * @author Li Yao Bing**/


public abstract class UserBaseController {

    @Resource
    protected UnbindSubscribeUserService unbindSubscribeUserService;

    @Resource
    protected SystemLogService systemLogService;

    @Resource
    protected UserUserService userUserService;

    @Resource
    protected RedisUtils redisUtils;

    @Resource
    protected AsyncTask asyncTask;

    @Resource
    protected UserCustomerService userCustomerService;

    @Resource
    protected UserArticleService userArticleService;

    @Resource
    UserCommentService userCommentService;
}
