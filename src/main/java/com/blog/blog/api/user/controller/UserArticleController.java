package com.blog.blog.api.user.controller;


import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.blog.annotation.EnableLog;
import com.blog.blog.bean.article.po.Article;
import com.blog.blog.bean.article.vo.ArticleVO;
import com.blog.blog.bean.common.BaseResponse;
import com.blog.blog.utils.WrappedBeanCopier;


/**
 * @description 用户文章模块
 * @author xuguoxing
 * @description 用户文章模块
 * @updateTime 2020-10-21
 */
@RestController
@RequestMapping("/user/article")
public class UserArticleController extends UserBaseController{

	@PostMapping("/save")
	@EnableLog(logName = "添加文章")
	public BaseResponse<Article> save(@RequestBody ArticleVO param) {
		Article article = WrappedBeanCopier.copyProperties(param,Article.class);
		if(!userArticleService.saveOrUpdate(article)) {
			return BaseResponse.createFailResponse("保存失败");
		}
		return BaseResponse.createSuccessResponse();
	}

	@PostMapping("/modify")
	@EnableLog(logName = "修改文章")
	public BaseResponse<Article> modify(@RequestBody ArticleVO param) {
		Article article = userArticleService.getById(param.getArticleId());
		// 必须进行判空操作，尽力避免空指针
        Assert.notNull(article, "数据不存在");

        WrappedBeanCopier.copyPropertiesIgnoreNull(param, article);
        if (!userArticleService.updateById(article)) {
            return BaseResponse.createFailResponse("修改失败");
        }
        return BaseResponse.createSuccessResponse();
    }

    @PostMapping("/detail")
    @EnableLog(logName = "文章详情")
    public BaseResponse<Article> detail(@RequestBody ArticleVO param) {
        Article article = userArticleService.getById(param.getArticleId());
        return BaseResponse.createSuccessResponse(article);
    }

    @PostMapping("/remove")
    @EnableLog(logName = "删除文章")
    public BaseResponse<Article> remove(@RequestBody ArticleVO param) {
        boolean remove = userArticleService.removeById(param.getArticleId());
        return BaseResponse.createSuccessOrFailResponse(remove, "删除失败");
    }

    @PostMapping("/list")
    @EnableLog(logName = "文章列表")
    public BaseResponse list(@RequestBody ArticleVO param) {
        // 分页应该有默认值
        long pageNo = param.getPageNo() == null ? 1 : param.getPageNo();
        long pageSize = param.getPageSize() == null ? 10 : param.getPageSize();
        Page<ArticleVO> page = new Page<>(pageNo, pageSize);
        page = userArticleService.getBaseMapper().getList(page);
        return BaseResponse.createSuccessResponse(page);
    }
}
