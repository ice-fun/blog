package com.blog.blog;

import com.blog.blog.api.admin.service.AdminAdminService;
import com.blog.blog.bean.admin.po.Admin;
import com.blog.blog.config.PropertyConfig;
import com.blog.blog.utils.RedisUtils;
import org.junit.jupiter.api.Test;
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

    @Test
    void addAdmin(){
        Admin admin = new Admin();
        admin.setAdminName("hades");
        admin.setAdminPassword(bCryptPasswordEncoder.encode(PropertyConfig.DEFAULT_PASSWORD));
        admin.setAdminPhone("10086");
        admin.setAdminAccount("10086");
        boolean save = adminService.save(admin);
        System.out.println(save);
    }

}
