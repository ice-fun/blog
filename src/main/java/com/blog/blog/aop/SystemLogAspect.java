package com.blog.blog.aop;

import com.alibaba.fastjson.JSON;
import com.blog.blog.api.system.service.SystemLogService;
import com.blog.blog.bean.system.po.SystemLog;
import com.blog.blog.utils.IpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.blog.blog.annotation.EnableLog;
import com.blog.blog.annotation.IgnoreLog;
import com.blog.blog.security.AuthUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Li Yao Bing* <p>
 * 基于 AOP 的日志收集模块
 * 通过切入 controller ，实现对接口的日志收集。包含接口路径，用户信息，ip地址，请求参数，响应结果，异常信息
 */
@Slf4j
@Component
@Aspect
public class SystemLogAspect {

    @Resource
    SystemLogService systemLogService;


    private String[] headerNames = {"token", "content-type"};

    @Pointcut("execution (* com.blog.blog.api.*.controller..*.*(..))")
    public void controllerAspect() {
    }

    @Around("controllerAspect()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws JsonProcessingException {
        IgnoreLog ignoreLog = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod().getAnnotation(IgnoreLog.class);
        // 如果添加 IgnoreLog 注解，此接口不记录日志
        if (ignoreLog != null) {
            try {
                return proceedingJoinPoint.proceed();
            } catch (Throwable throwable) {
                // 如果出现异常，会记录异常信息
                insertExceptionLog(proceedingJoinPoint, throwable);
                //记录异常后将异常再次抛出到 全局异常处理器处理。
                throw new RuntimeException(throwable);
            }
        }
        // 如果没有 IgnoreLog 注解，将会记录日志
        try {
            Object result = proceedingJoinPoint.proceed();
            // 保存日志
            insertNormalLog(proceedingJoinPoint, result);
            return result;
        } catch (Throwable throwable) {
            // 如果出现异常，会记录异常信息
            insertExceptionLog(proceedingJoinPoint, throwable);
            //记录异常后将异常再次抛出到 全局异常处理器处理。
            throw new RuntimeException(throwable);
        }
    }

    private SystemLog createBaseInfoSystemLog(ProceedingJoinPoint proceedingJoinPoint) throws JsonProcessingException {
        //获取请求相关参数
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String path = request.getRequestURI();
        String method = request.getMethod();
        Map<String, String> header = new HashMap<>();
        for (String name : headerNames) {
            header.put(name, request.getHeader(name));
        }
        String ip = IpUtil.getIpAddr(request);

        //获取用户相关参数
        SecurityContext context = SecurityContextHolder.getContext();
        String userId = null;
        String username = null;
        if (context.getAuthentication() != null) {
            AuthUserDetails userDetails = (AuthUserDetails) context.getAuthentication().getPrincipal();
            userId = userDetails.getId();
            username = userDetails.getUsername();
        }

        //获取注解上参数
        String logName = "";
        String logType = "";
        String logPlatform = "";
        EnableLog enableLog = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod().getAnnotation(EnableLog.class);
        if (enableLog != null) {
            logName = enableLog.logName();
            logType = enableLog.logType();
            logPlatform = enableLog.logPlatform();
        }

        //获取方法中的参数
        Object[] args = proceedingJoinPoint.getArgs();
        String params = "{}";
        for (Object arg : args) {
            if ((arg instanceof AuthUserDetails) || (arg instanceof Authentication)) {
                continue;
            }
            try {
                params = JSON.toJSONString(arg);
            } catch (Exception ignored) {
            }

        }

        //构造对象
        SystemLog systemLog = new SystemLog();
        systemLog.setPath(path);
        systemLog.setMethod(method);
        systemLog.setType(logType);
        systemLog.setName(logName);
        systemLog.setPlatform(logPlatform);
        systemLog.setIp(ip);
        systemLog.setUserId(userId);
        systemLog.setUsername(username);
        systemLog.setHeader(JSON.toJSONString(header));
        systemLog.setParam(params);
        systemLog.setLogTime(LocalDateTime.now());
        return systemLog;
    }

    private void insertExceptionLog(ProceedingJoinPoint proceedingJoinPoint, Throwable throwable) throws JsonProcessingException {
        SystemLog systemLog = createBaseInfoSystemLog(proceedingJoinPoint);
        final Writer writer = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(writer);
        throwable.printStackTrace(printWriter);
        systemLog.setException(writer.toString());
        systemLog.setType("异常");
        systemLogService.save(systemLog);
    }

    private void insertNormalLog(ProceedingJoinPoint proceedingJoinPoint, Object result) throws JsonProcessingException {
        SystemLog systemLog = createBaseInfoSystemLog(proceedingJoinPoint);

        String result1 = JSON.toJSONString(result);
        systemLog.setResult(result1);
        systemLogService.save(systemLog);
    }


}
