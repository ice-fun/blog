package com.blog.blog.security.filter;

import com.blog.blog.security.token.JwtAuthenticationToken;
import com.blog.blog.security.token.UserJwtAuthenticationToken;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * token访问拦截器，获取header中的token信息
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    Class<? extends JwtAuthenticationToken> auth;
    private AntPathRequestMatcher requiresAuthenticationRequestMatcher;
    private List<RequestMatcher> permissiveRequestMatchers;
    @Setter
    private AuthenticationManager authenticationManager;
    @Setter
    private AuthenticationSuccessHandler successHandler;
    @Setter
    private AuthenticationFailureHandler failureHandler;

    /**
     * 允许token为空的路径，针对本博客系统，允许用户不登录(访客)的情况下访问某些地址，比如文章列表、详情
     */
    private final List<RequestMatcher> allowVisitorRequestMatchers = new ArrayList<>(
            Arrays.asList(
                    new AntPathRequestMatcher("/user/article/list"),
                    new AntPathRequestMatcher("/user/article/detail")
            )
    );


    public JwtAuthenticationFilter(String path, Class<? extends JwtAuthenticationToken> auth) {
        //拦截header中带token的请求
        this.requiresAuthenticationRequestMatcher = new AntPathRequestMatcher(path);
        this.auth = auth;
    }

    private String getJwtToken(HttpServletRequest request) {
        String authInfo = request.getHeader("token");
        return StringUtils.removeStart(authInfo, "Bearer ");
    }

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (permissiveRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        if (!requiresAuthenticationRequestMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        Authentication authResult = null;
        AuthenticationException failed = null;
        String token = getJwtToken(request);
        if (this.allowVisitorRequest(request) && StringUtils.isBlank(token)) {
            authResult = new UserJwtAuthenticationToken(true);
            successfulAuthentication(request, response, filterChain, authResult);
        } else {
            try {
                JwtAuthenticationToken jwtAuthenticationToken = auth.getConstructor(String.class).newInstance(token);
                authResult = authenticationManager.authenticate(jwtAuthenticationToken);
            } catch (AuthenticationException e) {
                failed = e;
            }// Authentication failed

            if (authResult != null) {
                successfulAuthentication(request, response, filterChain, authResult);
            } else {
                unsuccessfulAuthentication(request, response, failed);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void successfulAuthentication(HttpServletRequest request,
                                          HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        if (successHandler != null) {
            successHandler.onAuthenticationSuccess(request, response, chain, authResult);
        }
    }

    private void unsuccessfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }


//    private boolean requiresAuthentication(HttpServletRequest request,
//                                           HttpServletResponse response) {
//        return requiresAuthenticationRequestMatcher.matches(request);
//    }


    private boolean permissiveRequest(HttpServletRequest request) {
        if (permissiveRequestMatchers == null) {
            return false;
        }
        for (RequestMatcher permissiveMatcher : permissiveRequestMatchers) {
            if (permissiveMatcher.matches(request)) {
                return true;
            }
        }
        return false;
    }

    public void setPermissiveUrl(String... urls) {
        if (permissiveRequestMatchers == null) {
            permissiveRequestMatchers = new ArrayList<>();
        }
        for (String url : urls) {
            permissiveRequestMatchers.add(new AntPathRequestMatcher(url));
        }
    }

    public boolean allowVisitorRequest(HttpServletRequest request) {
        return allowVisitorRequestMatchers.stream().anyMatch(visitorMatcher -> visitorMatcher.matches(request));
    }
}
