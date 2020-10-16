package com.knowswift.myspringboot.annotation;


import java.lang.annotation.*;

/**
 * 此注解用于在controller的AOP系统日志收集时，忽略此controller, 加于一些请求频繁的controller .如 前端定时请求的列表。
 * 但如果方法执行时发生异常，此注解会失效，从而保证异常信息一定会被记录。
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreLog {
}
