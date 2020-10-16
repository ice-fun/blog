package com.blog.blog.security.handler;

import com.blog.blog.bean.common.BaseResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 登录成功处理器
 */
public abstract class AbstractLoginSuccessHandler extends JsonResponseHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        Object data = preAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);
        if (!httpServletResponse.isCommitted()) {
            sendResponse(httpServletResponse, BaseResponse.SUCCESS_CODE, BaseResponse.SUCCESS_MSG, data);
        }
    }

    /**
     * 该方法让子类通过service实现登陆成功后的进行不同操作
     *
     * @param httpServletRequest  请求
     * @param httpServletResponse 响应
     * @param authentication      授权认证
     * @return 响应的data
     */
    public abstract Object preAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                                    HttpServletResponse httpServletResponse,
                                                    Authentication authentication);


}
