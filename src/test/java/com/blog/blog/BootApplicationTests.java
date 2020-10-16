package com.blog.blog;

import com.blog.blog.api.admin.service.AdminAdminService;
import com.blog.blog.utils.RedisUtils;
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
