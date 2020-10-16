package com.knowswift.myspringboot.security;

import com.knowswift.myspringboot.bean.common.BaseResponse;
import com.knowswift.myspringboot.security.handler.JsonResponseHandler;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyAuthenticationEntryPoint extends JsonResponseHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        sendResponse(httpServletResponse, BaseResponse.UNAUTHORIZED_CODE, e.getLocalizedMessage(), null);
    }
}
