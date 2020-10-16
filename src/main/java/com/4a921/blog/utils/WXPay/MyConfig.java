package com.knowswift.myspringboot.utils.WXPay;

import com.github.wxpay.sdk.WXPayConfig;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author Li Yao Bing
 * @Company https://www.knowswift.com/
 * @Date 2020/4/16
 * <p>
 * 调用微信支付API全局配置类，为满足给不用的商户分账的需求，mchId不再默认配置，需要在构造函数传入
 **/


public class MyConfig implements WXPayConfig {
    private final byte[] certData;
    private final String mchId;
    private final String appId; // 商户号的密钥
    private final String apiKey; // 商户号的密钥

    /**
     * @param mchId    商户号
     * @param appId    小程序ID
     * @param apiKey   商户号的密钥
     * @param certPath 商户号证书地址
     * @throws Exception
     */
    public MyConfig(String mchId, String appId, String apiKey, String certPath) throws Exception {
        this.mchId = mchId;
        this.appId = appId;
        this.apiKey = apiKey;
        File file = new File(certPath);
        InputStream certStream = new FileInputStream(file);
        this.certData = new byte[(int) file.length()];
        certStream.read(this.certData);
        certStream.close();
    }

    @Override
    public String getAppID() {
        return this.appId;
    }

    @Override
    public String getMchID() {
        return this.mchId;
    }

    @Override
    public String getKey() {
        return this.apiKey;
    }

    @Override
    public InputStream getCertStream() {
        return new ByteArrayInputStream(this.certData);
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 10000;
    }

}
