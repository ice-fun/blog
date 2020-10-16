package com.blog.blog.config;

/**
 * author：LiYaoBing
 * date: 2020/3/9
 */

public class WechatTemplate {

    /**
     * 您好，为方便对学生进行管理，特通知如下：
     * 学校：实验中学
     * 通知人：校办公室
     * 时间：2015-05-20
     * 通知内容：自2015年6月1日起我校启用微信家校互动平台点击查看详情（您好，
     * 我们是做基于微信的家校互平台台项目的，学校希望通过本平台向老师或家长发送一些通知类信息，内容分类不明确，没找到合适的，望批准，谢谢！）
     * <p>
     * {{first.DATA}}
     * 学校：{{keyword1.DATA}}
     * 通知人：{{keyword2.DATA}}
     * 时间：{{keyword3.DATA}}
     * 通知内容：{{keyword4.DATA}}
     * {{remark.DATA}}
     */
    private final static String templateId = "AC2dvjho2FEZpJ4U08lm16WYYz8oHKEz9PpFogq3c3U"; //
}
