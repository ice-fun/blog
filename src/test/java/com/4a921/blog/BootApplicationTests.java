package com.knowswift.myspringboot;

import com.knowswift.myspringboot.api.admin.service.AdminAdminService;
import com.knowswift.myspringboot.utils.RedisUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;

@SpringBootTest
class BootApplicationTests {

    @Resource
    RedisUtils redisUtils;

    @Resource
    AdminAdminService adminService;

    @Resource
    BCryptPasswordEncoder bCryptPasswordEncoder;

}
