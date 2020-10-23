package com.blog.blog.api.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.blog.bean.article.po.Article;
import com.blog.blog.bean.article.vo.ArticleVO;

/**
 * @description 
 * @author xuguoxing
 * @updateTime 2020-10-21
 */
public interface UserArticleMapper extends BaseMapper<Article>{

    Page<ArticleVO> getList(Page<ArticleVO> page);
}
