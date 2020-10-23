package com.blog.blog.api.user.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.blog.api.user.mapper.UserCommentMapper;
import com.blog.blog.bean.comment.po.Comment;

/**
 * @description 
 * @author xuguoxing
 * @updateTime 2020-10-23
 */
@Service
public class UserCommentService extends ServiceImpl<UserCommentMapper, Comment>{

}
