package com.knowswift.myspringboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    //配置token拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(commandRoomInterception).addPathPatterns("/commandRoom/**");
//        registry.addInterceptor(dutyRoomInterception).addPathPatterns("/dutyRoom/**");
    }
}
