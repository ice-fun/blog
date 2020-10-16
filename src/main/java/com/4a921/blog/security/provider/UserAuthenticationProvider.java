package com.knowswift.myspringboot.security.provider;

import com.knowswift.myspringboot.security.token.UserUsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

/**
 * @author Li Yao Bing
 * @Company https://www.knowswift.com/
 * @Date 2020/8/11
 **/


public class UserAuthenticationProvider extends DaoAuthenticationProvider {

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UserUsernamePasswordAuthenticationToken.class);
    }
}
