package com.blog.blog.security.provider;

import com.blog.blog.security.token.UserUsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

/**
 * @author Li Yao Bing* @Date 2020/8/11
 **/


public class UserAuthenticationProvider extends DaoAuthenticationProvider {

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UserUsernamePasswordAuthenticationToken.class);
    }
}
