package com.blog.blog.api.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.blog.bean.common.BaseResponse;
import com.blog.blog.bean.user.po.User;
import com.blog.blog.bean.user.vo.UserVO;
import com.blog.blog.utils.WrappedBeanCopier;
import com.blog.blog.annotation.EnableLog;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Li Yao Bing* <p>
 * Controller 层
 * 1.单例服务（非微服务框架）业务分包的情况 路径应该由两部分组成 如：admin/user
 * 前面的admin表示当前模块是总管理平台，user表示是关于user(用户)的模块
 * 其他Controller 均应遵循此原则。
 * <p>
 * 2.接口命名应该遵循规范，使用清晰的英文单词，禁止使用缩写、拼音，未避免路径超出长度，路径尽量使用简单的单词
 * 不强力的约束：
 * 添加：save
 * 修改：modify
 * 删除：remove/delete
 * 详情：detail/getById
 * 条件列表：list
 * 全部：all
 **/

@RestController
@RequestMapping("/admin/user/")
public class AdminUserController extends AdminBaseController {


    /**
     * 每个方法都必须使用统一的响应结果集类
     * 结果集类包含三个部分 1.状态码 2.文字描述  3.响应数据
     * 其中：
     * 1.状态码为项目统一约定，前端依据此状态码进行不同的分支操作，不得随意更换、设置
     * 2.文字描述作为向前端用户传达的信息，必须简短且能完整表达意思，必须使用白话文，不得使用编程术语、外国文字
     * 3.响应数据根据实际情况传递，可空。
     * <p>
     * 虽然不用添加 @EnableLog 也会收集日志，但为了方便记录操作日志，建议每个方法都加 此注解，并且都注明 logName、logPlatform（默认值可省略）
     *
     * @param param 参数 前后端分离的项目中，一般使用 JSON 传递数据，应该使用VO类接收参数，不应使用PO类、不得使用Map。
     * @return 项目统一的结果类。
     */
    @PostMapping("/save")
    @EnableLog(logName = "添加用户")
    public BaseResponse save(@RequestBody UserVO param) {
        User user = WrappedBeanCopier.copyProperties(param, User.class);
        boolean save = adminUserService.save(user);
        // 必须根据结果 选择返回成功或者失败的结果，方便前端控制操作分支
        if (!save) {
            return BaseResponse.createFailResponse("添加失败");
        }
        return BaseResponse.createSuccessResponse();
    }

    @PostMapping("/modify")
    @EnableLog(logName = "修改用户信息")
    public BaseResponse modify(@RequestBody UserVO param) {
        User user = adminUserService.getById(param.getUserId());
        // 必须进行判空操作，尽力避免空指针
        Assert.notNull(user, "数据不存在");

        WrappedBeanCopier.copyPropertiesIgnoreNull(param, user);
        boolean update = adminUserService.updateById(user);
        return BaseResponse.createSuccessOrFailResponse(update, "修改失败");
    }

    @PostMapping("/detail")
    @EnableLog(logName = "管理员详情")
    public BaseResponse detail(@RequestBody UserVO param) {
        User user = adminUserService.getById(param.getUserId());
        return BaseResponse.createSuccessResponse(user);
    }

    @PostMapping("/remove")
    @EnableLog(logName = "删除管理员")
    public BaseResponse remove(@RequestBody UserVO param) {
        boolean remove = adminUserService.removeById(param.getUserId());
        return BaseResponse.createSuccessOrFailResponse(remove, "删除失败");
    }

    @PostMapping("/list")
    @EnableLog(logName = "管理员列表")
    public BaseResponse list(@RequestBody UserVO param) {
        // 分页应该有默认值
        long pageNo = param.getPageNo() == null ? 1 : param.getPageNo();
        long pageSize = param.getPageSize() == null ? 10 : param.getPageSize();
        Page<User> page = new Page<>(pageNo, pageSize);
        page = adminUserService.page(page);
        return BaseResponse.createSuccessResponse(page);
    }
}
