package com.blog.blog.bean.comment.po;

import java.time.LocalDateTime;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName(autoResultMap = true) // mybatisPlus 自动映射JSON类型的sql列
public class Comment {
	
	@TableId
	private String commentId;
	
	private String targetId;//评论对象(文章、评论)
	
	private String userId;
	
	private String commentContent;//评论内容
	
	private Integer likeCount;//点赞数
	
	private LocalDateTime commentTime;//评论时间
	
	@TableField(value = "is_delete", fill = FieldFill.INSERT)
	private int isDelete;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
	private LocalDateTime createTime;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time", fill = FieldFill.INSERT)
	private LocalDateTime updateTime;
}
