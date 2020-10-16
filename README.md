# SpringBoot项目框架

#### 介绍
这个框架是当前多个项目沿用的单例服务器spring boot框架，满足一般场景下的单例服务开发需求。
容器为spring boot ，持久层orm框架为 mybatis，使用 mybatis plus 增强，
高速缓存KV数据库为 redis ，
使用spring security 提供安全支持，Jwt管理token.提供多种账号登录方法：账号密码、手机验证码、微信H5、微信小程序
使用aop记录接口请求日志


#### 软件架构
软件架构说明


#### 安装教程

1.  Fork 本仓库

#### 使用说明
1. Fork 本仓库之后，打开resources包下的多个application.yml文件，了解配置信息。

2. 请详细阅读security代码的流程，security代码在blog.security

    配合阅读spring security官方文档，filter 为拦截器，获取用户登录或者鉴权的参数。
    根据securityConfig类的配置，跳转到对应的provider类，执行登录或者鉴权的动作。
    结果由handler执行。

3. 阅读api.admin.controller包下的代码，了解项目的controller的编写风格（半强制约束）

#### 参与贡献
     
