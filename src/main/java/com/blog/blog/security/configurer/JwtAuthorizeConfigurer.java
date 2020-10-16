package com.blog.blog.security.configurer;

import com.blog.blog.security.filter.JwtAuthenticationFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import java.util.ArrayList;
import java.util.List;

public class JwtAuthorizeConfigurer<T extends JwtAuthorizeConfigurer<T, B>, B extends HttpSecurityBuilder<B>> extends AbstractHttpConfigurer<T, B> {
    private List<JwtAuthenticationFilter> filterList;

    public JwtAuthorizeConfigurer() {
        filterList = new ArrayList<>();
    }

    @Override
    public void configure(B builder) throws Exception {
        for (JwtAuthenticationFilter jwtAuthenticationFilter : filterList) {
            jwtAuthenticationFilter.setAuthenticationManager(builder.getSharedObject(AuthenticationManager.class));
            JwtAuthenticationFilter filter1 = postProcess(jwtAuthenticationFilter);
            builder.addFilterBefore(filter1, LogoutFilter.class);
        }
    }

    public JwtAuthorizeConfigurer<T, B> addJwtAuthenticationFilter(JwtAuthenticationFilter jwtAuthenticationFilter) {
        filterList.add(jwtAuthenticationFilter);
        return this;
    }

}
