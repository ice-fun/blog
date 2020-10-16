package com.blog.blog.bean.wechat.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Li Yao Bing* @Date 2020/8/4
 **/

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WeChatOfficialMaterial {

    @JsonProperty("total_count")
    @JSONField(name = "total_count")
    private Integer totalCount;

    @JsonProperty("item_count")
    @JSONField(name = "item_count")
    private Integer item_count;

    private List<item> item;
}

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
class item {
    @JsonProperty("media_id")
    @JSONField(name = "media_id")
    private String media_id;
    private Content content;
    @JsonProperty("update_time")
    @JSONField(name = "update_time")
    private Long update_time; // 这篇图文消息素材的最后更新时间
}

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
class Content {
    @JsonProperty("news_item")
    @JSONField(name = "news_item")
    private List<newsItem> newsItems;
}

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
class newsItem {
    private String title; // 图文消息的标题
    @JsonProperty("thumb_media_id")
    @JSONField(name = "thumb_media_id")
    private String thumb_media_id; // 图文消息的封面图片素材id（必须是永久mediaID）
    @JsonProperty("show_cover_pic")
    @JSONField(name = "show_cover_pic")
    private Integer show_cover_pic; // 是否显示封面，0为false，即不显示，1为true，即显示
    private String author; // 作者
    private String digest; // 图文消息的摘要，仅有单图文消息才有摘要，多图文此处为空
    private String content; // 图文消息的具体内容，支持HTML标签，必须少于2万字符，小于1M，且此处会去除JS
    private String url; // 图文页的URL，或者，当获取的列表是图片素材列表时，该字段是图片的URL
    @JsonProperty("content_source_url")
    @JSONField(name = "content_source_url")
    private String content_source_url; // 图文消息的原文地址，即点击“阅读原文”后的URL
}
