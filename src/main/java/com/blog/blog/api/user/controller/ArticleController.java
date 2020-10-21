package com.blog.blog.api.user.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.blog.api.user.service.ArticleService;
import com.blog.blog.bean.article.po.Article;
import com.blog.blog.bean.common.BaseResponse;

/**
 * @description 用户文章模块
 * @author xuguoxing
 * @updateTime 2020-10-21
 */
@RestController
@RequestMapping("/user/article")
public class ArticleController {
	
	@Resource
	private ArticleService articleService;
	
	@PostMapping("/save")
	public BaseResponse<Article> save(@RequestBody Article article) {
		if(!articleService.saveOrUpdate(article)) {
			return BaseResponse.createFailResponse("保存失败");
		}
		return BaseResponse.createSuccessResponse();
	}
}
