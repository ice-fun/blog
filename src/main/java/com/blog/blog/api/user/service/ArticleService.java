package com.blog.blog.api.user.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.blog.api.user.mapper.ArticleMapper;
import com.blog.blog.bean.article.po.Article;

/**
 * @description 
 * @author xuguoxing
 * @updateTime 2020-10-21
 */
@Service
public class ArticleService extends ServiceImpl<ArticleMapper, Article>{

}
