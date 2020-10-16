package com.blog.blog.security;

import com.blog.blog.api.admin.service.AdminAdminService;
import com.blog.blog.api.customer.service.CustomerCustomerService;
import com.blog.blog.api.system.service.SystemLogService;
import com.blog.blog.api.user.service.UserUserService;
import com.blog.blog.security.configurer.JwtAuthorizeConfigurer;
import com.blog.blog.security.configurer.LoginConfigurer;
import com.blog.blog.security.handler.fail.LoginFailureHandler;
import com.blog.blog.security.handler.fail.RestAuthenticationAccessDeniedHandler;
import com.blog.blog.security.handler.fail.TokenAuthenticationFailureHandler;
import com.blog.blog.utils.RedisUtils;
import com.blog.blog.security.filter.*;
import com.blog.blog.security.handler.success.*;
import com.blog.blog.security.provider.*;
import com.blog.blog.security.token.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.header.Header;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.Resource;
import java.util.Arrays;


@Configuration
@EnableWebSecurity// 这个注解必须加，开启Security
@EnableGlobalMethodSecurity(prePostEnabled = true)//保证post之前的注解可以使用
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    @Resource
//    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;


    @Resource
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Resource
    RedisUtils redisUtils;

    @Resource
    AdminAdminService adminAdminService;

    @Resource
    CustomerCustomerService customerCustomerService;

    @Resource
    UserUserService userUserService;

    @Resource
    RestAuthenticationAccessDeniedHandler restAuthenticationAccessDeniedHandler;

    @Resource
    TokenAuthenticationFailureHandler tokenAuthenticationFailureHandler;
    /**
     * Handler
     */

    @Resource
    private LoginFailureHandler loginFailureHandler;
    @Resource
    private AdminPasswordLoginSuccessHandler adminPasswordLoginSuccessHandler;

    @Resource
    private UserCodeLoginSuccessHandler userCodeLoginSuccessHandler;

    @Resource
    private CustomerCodeLoginSuccessHandler customerCodeLoginSuccessHandler;

    @Resource
    private CustomerMiniProgramLoginSuccessHandler customerMiniProgramLoginSuccessHandler;

    @Resource
    private UserMiniProgramLoginSuccessHandler userMiniProgramLoginSuccessHandler;

    @Resource
    private UserPasswordLoginSuccessHandler userPasswordLoginSuccessHandler;

    @Resource
    private UserWeChatLoginSuccessHandler userWeChatLoginSuccessHandler;

    @Resource
    private SystemLogService systemLogService;

    /**
     * Provider
     * 配置各种登录方法的provider
     */
    protected VerifyCodeAuthenticationProvider customerCodeAuthenticationProvider() {
        return new VerifyCodeAuthenticationProvider(customerCustomerService, CustomerVerifyCodeAuthenticationToken.class, redisUtils);
    }

    protected JwtAuthenticationProvider customerJwtAuthenticationProvider() {
        return new JwtAuthenticationProvider(customerCustomerService, CustomerJwtAuthenticationToken.class, "tokenVersion");
    }

    protected MiniProgramAuthenticationProvider customerMiniProgramAuthenticationProvider() {
        return new MiniProgramAuthenticationProvider(customerCustomerService, CustomerMiniProgramAuthenticationToken.class, systemLogService);
    }

    protected JwtAuthenticationProvider adminJwtAuthenticationProvider() {
        return new JwtAuthenticationProvider(adminAdminService, AdminJwtAuthenticationToken.class, "adminTokenVersion");
    }


    protected JwtAuthenticationProvider userJwtAuthenticationProvider() {
        return new JwtAuthenticationProvider(userUserService, UserJwtAuthenticationToken.class, "tokenVersion");
    }

    public AuthenticationProvider adminAuthentication() {
        DaoAuthenticationProvider provider = new AdminAuthenticationProvider();
        provider.setUserDetailsService(adminAdminService);
        provider.setHideUserNotFoundExceptions(false);
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        return provider;
    }

    public AuthenticationProvider userAuthentication() {
        DaoAuthenticationProvider provider = new UserAuthenticationProvider();
        provider.setUserDetailsService(userUserService);
        provider.setHideUserNotFoundExceptions(false);
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        return provider;
    }


    protected JwtAuthenticationProvider parentJwtAuthenticationProvider() {
        return new JwtAuthenticationProvider(userUserService, UserJwtAuthenticationToken.class, "tokenVersion");
    }

    protected VerifyCodeAuthenticationProvider parentCodeAuthenticationProvider() {
        return new VerifyCodeAuthenticationProvider(userUserService, UserVerifyCodeAuthenticationToken.class, redisUtils);
    }

    protected MiniProgramAuthenticationProvider parentMiniProgramAuthenticationProvider() {
        return new MiniProgramAuthenticationProvider(userUserService, UserMiniProgramAuthenticationToken.class, systemLogService);
    }

    protected WeChatAuthenticationProvider userWeChatAuthenticationProvider() {
        return new WeChatAuthenticationProvider(userUserService, AgentWeChatAuthenticationToken.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(adminAuthentication())
                .authenticationProvider(adminJwtAuthenticationProvider())
                .authenticationProvider(customerJwtAuthenticationProvider())
                .authenticationProvider(customerCodeAuthenticationProvider())
                .authenticationProvider(customerMiniProgramAuthenticationProvider())
                .authenticationProvider(parentJwtAuthenticationProvider())
                .authenticationProvider(parentCodeAuthenticationProvider())
                .authenticationProvider(parentMiniProgramAuthenticationProvider())
                .authenticationProvider(userAuthentication())
                .authenticationProvider(userJwtAuthenticationProvider())
                .authenticationProvider(userWeChatAuthenticationProvider())
        ;


    }

    /**
     * Filter
     */
    protected JwtAuthenticationFilter configJwtAuthenticationFilter(String path, Class<? extends JwtAuthenticationToken> authClass,
                                                                    AuthenticationSuccessHandler authenticationSuccessHandler,
                                                                    AuthenticationFailureHandler authenticationFailureHandler, String... excludeUrl) {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(path, authClass);
        filter.setPermissiveUrl(excludeUrl);
        filter.setSuccessHandler(authenticationSuccessHandler);
        filter.setFailureHandler(authenticationFailureHandler);
        return filter;
    }

    protected AbstractAuthenticationProcessingFilter configCodeLoginFilter(String path, Class<? extends VerifyCodeAuthenticationToken> authClass,
                                                                           AuthenticationSuccessHandler authenticationSuccessHandler,
                                                                           AuthenticationFailureHandler authenticationFailureHandler) {
        VerifyCodeAuthenticationFilter filter = new VerifyCodeAuthenticationFilter(path, authClass);
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(authenticationFailureHandler);
        return filter;
    }

    protected PasswordAuthenticationFilter configPasswordLoginFilter(String path, Class<? extends UsernamePasswordAuthenticationToken> authClass,
                                                                     AuthenticationSuccessHandler authenticationSuccessHandler,
                                                                     AuthenticationFailureHandler authenticationFailureHandler) {
        PasswordAuthenticationFilter filter = new PasswordAuthenticationFilter(path, authClass);
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(authenticationFailureHandler);
        return filter;
    }

    protected MiniProgramAuthenticationFilter configMiniProgramLoginFilter(String path, Class<? extends MiniProgramAuthenticationToken> authClass,
                                                                           AuthenticationSuccessHandler authenticationSuccessHandler,
                                                                           AuthenticationFailureHandler authenticationFailureHandler) {
        MiniProgramAuthenticationFilter filter = new MiniProgramAuthenticationFilter(path, authClass);
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(authenticationFailureHandler);
        return filter;
    }

    protected WeChatAuthenticationFilter configWeChatLoginFilter(String loginPath, Class<? extends WeChatAuthenticationToken> authClass,
                                                                 AuthenticationSuccessHandler authenticationSuccessHandler,
                                                                 AuthenticationFailureHandler authenticationFailureHandler) {
        WeChatAuthenticationFilter filter = new WeChatAuthenticationFilter(loginPath, authClass);
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(authenticationFailureHandler);
        return filter;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //直接放行 不经security流程
        web.ignoring().antMatchers(
                "/wechatOfficialAccounts/getMessage",
                "/*/index/bindPhoneNumber",
                "/WXPay/*",
                "/*/index/verifyCode"
//                ,"/**"
        );
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling()
                .authenticationEntryPoint(new MyAuthenticationEntryPoint())
                .accessDeniedHandler(restAuthenticationAccessDeniedHandler)
                .and()
                .authorizeRequests()
                //任何权限都可以通过
//                .antMatchers("/*/index/bindPhoneNumber", "/*/index/verifyCode").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .formLogin().disable()
                .sessionManagement().disable()
                .cors()
                .and()
                .headers().addHeaderWriter(new StaticHeadersWriter(Arrays.asList(new Header("Access-control-Allow-Origin", "*"), new Header("Access-Control-Expose-Headers", "Authorization"))))
                .and()
                .addFilterAfter(new OptionsRequestFilter(), CorsFilter.class)
                //配置token认证
                .apply(new JwtAuthorizeConfigurer<>())
                .addJwtAuthenticationFilter(configJwtAuthenticationFilter("/admin/**", AdminJwtAuthenticationToken.class, null, tokenAuthenticationFailureHandler,
                        "/admin/index/login"))
                .addJwtAuthenticationFilter(configJwtAuthenticationFilter("/customer/**", CustomerJwtAuthenticationToken.class, null, tokenAuthenticationFailureHandler,
                        "/customer/index/login", "/customer/index/verifyCodeLogin", "/customer/index/miniProgramLogin"))
                .addJwtAuthenticationFilter(configJwtAuthenticationFilter("/user/**", UserJwtAuthenticationToken.class, null, tokenAuthenticationFailureHandler,
                        "/user/index/login", "/user/index/miniProgramLogin", "/user/index/verifyCodeLogin"))
                //配置登陆
                .and()
                .apply(new LoginConfigurer<>())
                .addFilter(configCodeLoginFilter("/customer/index/verifyCodeLogin", CustomerVerifyCodeAuthenticationToken.class, customerCodeLoginSuccessHandler, loginFailureHandler))
                .addFilter(configCodeLoginFilter("/user/index/verifyCodeLogin", UserVerifyCodeAuthenticationToken.class, userCodeLoginSuccessHandler, loginFailureHandler))
                .addFilter(configPasswordLoginFilter("/admin/index/login", AdminUsernamePasswordAuthenticationToken.class, adminPasswordLoginSuccessHandler, loginFailureHandler))
                .addFilter(configMiniProgramLoginFilter("/customer/index/miniProgramLogin", CustomerMiniProgramAuthenticationToken.class, customerMiniProgramLoginSuccessHandler, loginFailureHandler))
                .addFilter(configMiniProgramLoginFilter("/user/index/miniProgramLogin", UserMiniProgramAuthenticationToken.class, userMiniProgramLoginSuccessHandler, loginFailureHandler))
                .addFilter(configPasswordLoginFilter("/user/index/login", UserUsernamePasswordAuthenticationToken.class, userPasswordLoginSuccessHandler, loginFailureHandler))
                .addFilter(configWeChatLoginFilter("/user/index/authorize", AgentWeChatAuthenticationToken.class, userWeChatLoginSuccessHandler, loginFailureHandler))
                .and()
        ;
    }
}
