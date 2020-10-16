package com.blog.blog.security.provider;

import com.blog.blog.security.token.AdminUsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

/**
  * @author Li Yao Bing
 * @Date 2020/6/30
 **/


public class AdminAuthenticationProvider extends DaoAuthenticationProvider {

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(AdminUsernamePasswordAuthenticationToken.class);
    }
}
