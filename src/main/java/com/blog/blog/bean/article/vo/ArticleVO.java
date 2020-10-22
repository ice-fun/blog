package com.blog.blog.bean.article.vo;

import java.time.LocalDateTime;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

/**
 * @description : 文章表
 * @updateTime: 2020-10-21
 * @author xuguoxing
 * */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleVO {
	
	@TableId
	private String articleId;
	private String userId;
	//文章分类
	private String articleTypeId;
	//创作类型 1原创 2转载
	private String produceType;
	//文章标签列表
	private List<String> articleLabels;
	//标题
	private String articleTitle;
	//内容
	private String articleContent;
	//点赞数
	private Integer likeCount;
	//阅读数
	private Integer watchCount;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "article_publish_time", fill = FieldFill.INSERT)
	private LocalDateTime articlePublishTime;//发布时间
	
	@TableField(value = "is_delete", fill = FieldFill.INSERT)
	private Integer isDelete;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
	private LocalDateTime createTime;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time", fill = FieldFill.INSERT)
	private LocalDateTime updateTime;
	
	
    private String keyword;
    private Long pageNo;
    private Long pageSize;
}
