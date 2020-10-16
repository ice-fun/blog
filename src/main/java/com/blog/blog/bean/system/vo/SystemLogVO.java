package com.blog.blog.bean.system.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Li Yao Bing* @Date 2020/3/27
 **/

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SystemLogVO {
    @TableId
    private String systemLogId;
    private String path;
    private String method;
    private String type;
    private String name;
    private String platform;
    private String ip;
    private String userId;
    private String username;
    private String header;
    private String param;
    private String result;
    private String exception;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime logTime;
    private Integer isDelete;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;


    private String keyword;
    private Long pageNo;
    private Long pageSize;
    private Integer resultType;
}
