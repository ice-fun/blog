package com.blog.blog;

import com.blog.blog.api.admin.service.AdminAdminService;
import com.blog.blog.api.user.service.UserUserService;
import com.blog.blog.bean.admin.po.Admin;
import com.blog.blog.bean.user.po.User;
import com.blog.blog.config.PropertyConfig;
import com.blog.blog.utils.MD5Utils;
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
    UserUserService userService;
    
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
    
    @Test
    void addUser(){
        User user = new User();
        user.setUserAccount("test");
        user.setUserPassword(bCryptPasswordEncoder.encode(MD5Utils.md5Decode32WithSalt("11111111")));
        boolean save = userService.save(user);
        System.out.println(save);
    }
}
