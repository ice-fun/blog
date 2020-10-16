package com.blog.blog.bean.wechat.vo;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static javax.xml.bind.annotation.XmlAccessType.FIELD;

@Data
@XmlRootElement(name = "xml")
@XmlAccessorType(FIELD)
public class PlaintextWechatMessage {
    @XmlElement(name = "ToUserName")
    private String toUserName;

    @XmlElement(name = "FromUserName")
    private String fromUserName;

    @XmlElement(name = "CreateTime")
    private Long createTime;

    @XmlElement(name = "MsgType")
    private String msgType;

    @XmlElement(name = "Event")
    private String event;

    @XmlElement(name = "EventKey")
    private String eventKey;

    @XmlElement(name = "Ticket")
    private String ticket;

    @XmlElement(name = "Latitude")
    private String latitude;

    @XmlElement(name = "Longitude")
    private String longitude;

    @XmlElement(name = "Content")
    private String content;

    @XmlElement(name = "MsgId")
    private String msgId;


}
