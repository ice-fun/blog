package com.blog.blog.api.user.controller;

import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.blog.annotation.EnableLog;
import com.blog.blog.bean.comment.po.Comment;
import com.blog.blog.bean.comment.vo.CommentVO;
import com.blog.blog.bean.common.BaseResponse;
import com.blog.blog.utils.WrappedBeanCopier;

/**
 * @description 用户评论模块
 * @author xuguoxing
 * @updateTime 2020-10-23
 */
@RestController
@RequestMapping("/user/comment")
public class UserCommentController extends UserBaseController{
	
	@PostMapping("/save")
	@EnableLog(logName = "添加评论")
	public BaseResponse<Comment> save(@RequestBody CommentVO commentVO) {
		Comment comment = WrappedBeanCopier.copyProperties(commentVO,Comment.class);
		if(!userCommentService.saveOrUpdate(comment)) {
			return BaseResponse.createFailResponse("保存失败");
		}
		return BaseResponse.createSuccessResponse();
	}
	
	@PostMapping("/modify")
	@EnableLog(logName = "修改评论")
	public BaseResponse<Comment> modify(@RequestBody CommentVO commentVO) {
		Comment comment = userCommentService.getById(commentVO.getCommentId());
		// 必须进行判空操作，尽力避免空指针
        Assert.notNull(comment, "数据不存在");

        WrappedBeanCopier.copyPropertiesIgnoreNull(commentVO,comment);
		if(!userCommentService.updateById(comment)) {
			return BaseResponse.createFailResponse("修改失败");
		}
		return BaseResponse.createSuccessResponse();
	}
	
	@PostMapping("/detail")
    @EnableLog(logName = "评论详情")
    public BaseResponse<Comment> detail(@RequestBody CommentVO commentVO) {
		Comment comment = userCommentService.getById(commentVO.getCommentId());
        return BaseResponse.createSuccessResponse(comment);
    }
	
	@PostMapping("/remove")
    @EnableLog(logName = "删除评论")
    public BaseResponse<Comment> remove(@RequestBody CommentVO commentVO) {
        boolean remove = userCommentService.removeById(commentVO.getCommentId());
        return BaseResponse.createSuccessOrFailResponse(remove, "删除失败");
    }
	
	@PostMapping("/list")
    @EnableLog(logName = "评论列表")
    public BaseResponse list(@RequestBody CommentVO commentVO) {
        // 分页应该有默认值
        long pageNo = commentVO.getPageNo() == null ? 1 : commentVO.getPageNo();
        long pageSize = commentVO.getPageSize() == null ? 10 : commentVO.getPageSize();
        Page<Comment> page = new Page<>(pageNo, pageSize);
        page = userCommentService.page(page);
        return BaseResponse.createSuccessResponse(page);
    }
}
