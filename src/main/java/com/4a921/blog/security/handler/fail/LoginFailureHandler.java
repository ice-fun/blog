package com.knowswift.myspringboot.security.handler.fail;

import com.knowswift.myspringboot.bean.common.BaseResponse;
import com.knowswift.myspringboot.security.handler.JsonResponseHandler;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录失败处理器
 */
@Component
public class LoginFailureHandler extends JsonResponseHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        sendResponse(httpServletResponse, BaseResponse.LOGIN_FAIL_CODE, e.getMessage(), null);
    }
}
