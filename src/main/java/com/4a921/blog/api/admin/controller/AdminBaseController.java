package com.knowswift.myspringboot.api.admin.controller;

import com.knowswift.myspringboot.api.admin.service.AdminAdminService;
import com.knowswift.myspringboot.api.admin.service.AdminSystemLogService;
import com.knowswift.myspringboot.api.admin.service.AdminUserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;

/**
 * Controller 层的Base类，bean的自动注入在此方法完成，其他controller继承此方法
 */
public abstract class AdminBaseController {

    @Resource
    AdminSystemLogService adminSystemLogService;

    @Resource
    AdminAdminService adminAdminService;

    @Resource
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Resource
    AdminUserService adminUserService;
}
