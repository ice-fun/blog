package com.knowswift.myspringboot.bean.wechat.vo;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static javax.xml.bind.annotation.XmlAccessType.FIELD;

@Data
@XmlRootElement(name = "xml")
@XmlAccessorType(FIELD)
public class CiphertextWechatMessage {
    @XmlElement(name = "ToUserName")
    private String toUserName;

    @XmlElement(name = "Encrypt")
    private String encrypt;
}
