package com.blog.blog.handler;

import com.blog.blog.bean.common.BaseResponse;
import io.lettuce.core.RedisException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 对方法参数校验异常处理方法(仅对于表单提交有效，对于以json格式提交将会失效)
     * 如果是表单类型的提交，则spring会采用表单数据的处理类进行处理（进行参数校验错误时会抛出BindException异常）
     */
//    @ResponseBody
//    @ExceptionHandler(Exception.class)
//    public BaseResponse handlerBindException(Exception e) {
//
//        return BaseResponse.createNoDataResponse(201,"not null");
//    }

    /**
     * 对方法参数校验异常处理方法(前端提交的方式为json格式出现异常时会被该异常类处理)
     * json格式提交时，spring会采用json数据的数据转换器进行处理（进行参数校验时错误是抛出MethodArgumentNotValidException异常）
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse handlerArgumentNotValidException(MethodArgumentNotValidException exception) {
        return BaseResponse.createFailResponse(exception.getBindingResult().getFieldError().getDefaultMessage(), null);
    }


    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    public BaseResponse handlerIllegalArgumentException(IllegalArgumentException exception) {
        return BaseResponse.createFailResponse(exception.getLocalizedMessage());
    }


    @ResponseBody
    @ExceptionHandler(RedisException.class)
    public BaseResponse handlerRedisCommandTimeoutException(RedisException exception) {
        return BaseResponse.createFailResponse("网络不太稳定，请重试");
    }


}
