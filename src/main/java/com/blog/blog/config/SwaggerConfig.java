package com.blog.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * @description Swagger2接口文档配置类
 * @updateTime 2020-10-22
 * @author xuguoxing
 * */
@Configuration//配置SwaggerConfig类型在SpringBoot
@EnableSwagger2//
public class SwaggerConfig {
	
	@Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                .select()
				/*此处为你的访问层接口包路径，即控制层所对应的包路径，这里
				*如果没有编写正确，会造成的在访问swagger2页面时没有接口信息出现
				*/  
				 .apis(RequestHandlerSelectors.basePackage("com.blog.blog.api"))
                .paths(PathSelectors.any())
                .build().apiInfo(new ApiInfoBuilder()
               			 //标题
                        .title("4A921博客系统开发文档API总览界面")
                        //描述
                        .description("基础框架系统接口详细信息......")
                        //版本号
                        .version("9.0")
                        //联系方式
                        .contact(new Contact("指尖上跳动的旋律","blog.csdn.net","xxx@qq.com"))
                        //许可证
                        .license("指尖上跳动的旋律")
                        //许可证访问链接
                        .licenseUrl("https://blog.csdn.net/xuguoxing123")
                        .build());
    }
}
