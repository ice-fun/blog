package com.knowswift.myspringboot.bean.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 统一的响应结果集
 *
 * @param <T>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class BaseResponse<T> implements Serializable {
    public final static int SUCCESS_CODE = 200; // 业务成功
    public final static String SUCCESS_MSG = "success";
    public final static int FAIL_CODE = 300; // 业务失败
    public final static int UNAUTHORIZED_CODE = 301; // 未登录/未授权
    public final static int LOGIN_FAIL_CODE = 302; // 登录失败
    public final static int NO_ACCESS = 305; // 权限不足
    public final static int ERROR_CODE = 500; // 异常

    private int statusCode;
    private String statusMsg;
    private T data;

    public static <T> BaseResponse<T> createResponse(int code, String msg, T data) {
        return new BaseResponse<T>(code, msg, data);
    }

    public static <T> BaseResponse<T> createNoDataResponse(int code, String msg) {
        return createResponse(code, msg, null);
    }

    public static <T> BaseResponse<T> createSuccessResponse() {
        return createResponse(SUCCESS_CODE, SUCCESS_MSG, null);
    }

    public static <T> BaseResponse<T> createSuccessResponse(T data) {
        return createResponse(SUCCESS_CODE, SUCCESS_MSG, data);
    }

    public static <T> BaseResponse<T> createFailResponse(String msg, T data) {
        return createResponse(FAIL_CODE, msg, data);
    }

    public static <T> BaseResponse<T> createFailResponse(String msg) {
        return createNoDataResponse(FAIL_CODE, msg);
    }

    public static <T> BaseResponse<T> createUnauthorizedResponse(String msg, T data) {
        return createResponse(UNAUTHORIZED_CODE, msg, data);
    }

    public static <T> BaseResponse<T> createUnauthorizedResponse(String msg) {
        return createNoDataResponse(UNAUTHORIZED_CODE, msg);
    }

    public static <T> BaseResponse<T> createErrorResponse(String msg, T data) {
        return createResponse(ERROR_CODE, msg, data);
    }

    public static <T> BaseResponse<T> createErrorResponse(String msg) {
        return createNoDataResponse(ERROR_CODE, msg);
    }

    public static <T> BaseResponse<T> createSuccessOrFailResponse(boolean success, String failedMsg) {
        if (success) {
            return createSuccessResponse();
        }
        return createFailResponse(failedMsg);
    }

    public static <T> BaseResponse<T> createNotNullFailResponse(String msg) {
        return createNoDataResponse(FAIL_CODE, msg);
    }
}

