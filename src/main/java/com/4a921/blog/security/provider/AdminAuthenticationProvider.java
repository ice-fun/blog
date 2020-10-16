package com.knowswift.myspringboot.security.provider;

import com.knowswift.myspringboot.security.token.AdminUsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

/**
 * @author Li Yao Bing
 * @Company https://www.knowswift.com/
 * @Date 2020/6/9
 **/


public class AdminAuthenticationProvider extends DaoAuthenticationProvider {

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(AdminUsernamePasswordAuthenticationToken.class);
    }
}
