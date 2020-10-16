package com.knowswift.myspringboot.security.handler;

import com.alibaba.fastjson.JSON;
import com.knowswift.myspringboot.bean.common.BaseResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public abstract class JsonResponseHandler {

    protected void sendResponse(HttpServletResponse httpServletResponse, int code, String msg, Object data) {
        BaseResponse responseObject = BaseResponse.createResponse(code, msg, data);
        httpServletResponse.setStatus(code);
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.setCharacterEncoding("UTF-8");
        try (PrintWriter out = httpServletResponse.getWriter()) {
            out.append(JSON.toJSONString(responseObject));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
