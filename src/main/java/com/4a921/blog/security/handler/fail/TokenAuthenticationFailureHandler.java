package com.knowswift.myspringboot.security.handler.fail;

import com.knowswift.myspringboot.bean.common.BaseResponse;
import com.knowswift.myspringboot.security.handler.JsonResponseHandler;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenAuthenticationFailureHandler extends JsonResponseHandler implements org.springframework.security.web.authentication.AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) {
        sendResponse(httpServletResponse, BaseResponse.UNAUTHORIZED_CODE, e.getMessage(), null);
    }
}
