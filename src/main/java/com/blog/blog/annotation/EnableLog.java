package com.blog.blog.annotation;


import java.lang.annotation.*;

/**
 * 开启controller的AOP系统日志收集 默认可以不添加此注解
 * 包含三个参数 logName 表示controller的操作名称，如：添加用户。 logType表示controller的操作类型。 logPlatform表示controller的业务终端
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableLog {
    String logName() default "";

    String logType() default "";

    String logPlatform() default "博客";
}
