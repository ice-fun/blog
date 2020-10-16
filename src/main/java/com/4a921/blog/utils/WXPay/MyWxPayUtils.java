package com.knowswift.myspringboot.utils.WXPay;

import cn.hutool.extra.qrcode.QrCodeUtil;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.knowswift.myspringboot.config.PropertyConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Li Yao Bing
 * @Company https://www.knowswift.com/
 * @Date 2020/4/16
 **/

@Component
public class MyWxPayUtils {

    protected static String certPath;

    protected static String payNoticeUrl;

    protected static String refundNoticeUrl;

    @Value("${wx.certPath}")
    public void setCertPath(String certPath) {
        MyWxPayUtils.certPath = certPath;
    }

    @Value("${wx.payNoticePath}")
    public void setPayNoticeUrl(String url) {
        MyWxPayUtils.payNoticeUrl = url;
    }

    @Value("${wx.refundPath}")
    public void setRefundNoticeUrl(String url) {
        MyWxPayUtils.refundNoticeUrl = url;
    }

    /**
     * 下单方法，此方法构建下单参数，请求微信官方API--统一下单，
     * 创建微信支付订单
     *
     * @param mchId   商户号。为满足不同商户的订单资金发向不同的商户号，mchID 需要传入
     * @param body    支付内容。为此次订单的文字描述
     * @param orderId 订单ID。系统的订单业务ID
     * @param goodsId 商品ID。系统的商品业务ID
     * @param fee     金额 单位为元
     * @param userIp  支付用户的IP地址
     * @param openId  支付用户的openId，应 注意为小程序下的openId。并非公众号下的openId
     * @return 微信返回的支付结果。
     * @throws Exception 异常信息
     */
    public static Map<String, String> bookOrder(String mchId, String mchAppKey, String cert, String body, String orderId, String goodsId, BigDecimal fee, String userIp, String openId) throws Exception {
        String s = certPath + cert + "/apiclient_cert.p12";
        MyConfig config = new MyConfig(mchId, PropertyConfig.MINI_ID_TWO, mchAppKey, s);
        WXPay wxpay = new WXPay(config, WXPayConstants.SignType.MD5);
        // 将单位为元 的金额换算成 单位为分的金额
        fee = fee.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP);
//        String type = orderType.equals(PropertyConfig.ORDER_TYPE_ACTIVITY) || orderType.equals(PropertyConfig.ORDER_TYPE_EXCHANGE) ? "JSAPI" : "NATIVE";
        Map<String, String> data = new HashMap<>();
        data.put("body", body);
        data.put("appid", PropertyConfig.MINI_ID_TWO);
        data.put("mch_id", mchId);
        data.put("out_trade_no", orderId);
        data.put("fee_type", "CNY");
        data.put("total_fee", fee.toString());
        data.put("spbill_create_ip", userIp);
        data.put("notify_url", payNoticeUrl);
        data.put("trade_type", "JSAPI");  // 小程序支付类型为jsapi
        data.put("openid", openId);
        data.put("product_id", goodsId);
        data.put("attach", "charge");
        data.put("nonce_str", WXPayUtil.generateNonceStr());
        String sign = WXPayUtil.generateSignature(data, mchAppKey);
        data.put("sign", sign);
        Map<String, String> resp = null;
        try {
            resp = wxpay.unifiedOrder(data);
            System.out.println(resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }

    public static String generateQRCode(String url) {
        BufferedImage generate = QrCodeUtil.generate(url, 245, 245);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();//io流
        try {
            ImageIO.write(generate, "png", baos);//写入流中
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] bytes = baos.toByteArray();//转换成字节
        Base64.Encoder encoder = Base64.getEncoder();
        String png_base64 = encoder.encodeToString(bytes).trim();//转换成base64串
        png_base64 = png_base64.replaceAll("\n", "").replaceAll("\r", "");
        return "data:image/jpg;base64," + png_base64;
    }


    /**
     * 申请退款
     *
     * @param transactionId 微信支付订单id
     * @param outRefundNo   退款id
     * @param fee           退款金额
     * @return
     * @throws Exception
     */
    public static Map<String, String> applyRefund(String mchId, String mchApiKey, String cert, String transactionId, String outRefundNo, BigDecimal fee) throws Exception {
        String s = certPath + cert + "/apiclient_cert.p12";
        MyConfig config = new MyConfig(mchId, PropertyConfig.MINI_ID_TWO, mchApiKey, s);
        WXPay wxpay = new WXPay(config, WXPayConstants.SignType.MD5);

        // 随机字符串
        String nonceStr = WXPayUtil.generateNonceStr();
        // 金额换成为单位为分
        BigDecimal orderFee = fee.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP);
        Map<String, String> data = new HashMap<>();
        data.put("appid", PropertyConfig.MINI_ID_TWO);
        data.put("mch_id", mchId);
        data.put("nonce_str", nonceStr);
        data.put("transaction_id", transactionId);
        data.put("out_refund_no", outRefundNo);
        data.put("total_fee", orderFee.toString());
        data.put("refund_fee", orderFee.toString());
        data.put("notify_url", refundNoticeUrl);
        // 签名
        String sign = WXPayUtil.generateSignature(data, mchApiKey);
        data.put("sign", sign);
        // 请求微信退款接口
        Map<String, String> refund = wxpay.refund(data);
        System.out.println(refund);
        return refund;
    }


    public static Map<String, String> bookOrder(String body, String orderId, String goodsId, BigDecimal fee, String userIp, String openId) throws Exception {
        String s = certPath + "/miaozhi/apiclient_cert.p12";
        MyConfig config = new MyConfig(PropertyConfig.MCH_ID, PropertyConfig.MINI_ID_ONE, PropertyConfig.MCH_APP_KEY, s);
        WXPay wxpay = new WXPay(config, WXPayConstants.SignType.MD5);
        // 将单位为元 的金额换算成 单位为分的金额
        fee = fee.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP);
//        String type = orderType.equals(PropertyConfig.ORDER_TYPE_ACTIVITY) || orderType.equals(PropertyConfig.ORDER_TYPE_EXCHANGE) ? "JSAPI" : "NATIVE";
        Map<String, String> data = new HashMap<>();
        data.put("body", body);
        data.put("appid", PropertyConfig.MINI_ID_ONE);
        data.put("mch_id", PropertyConfig.MCH_ID);
        data.put("out_trade_no", orderId);
        data.put("fee_type", "CNY");
        data.put("total_fee", fee.toString());
        data.put("spbill_create_ip", userIp);
        data.put("notify_url", payNoticeUrl);
        data.put("trade_type", "JSAPI");  // 小程序支付类型为jsapi
        data.put("openid", openId);
        data.put("attach", "message");
        data.put("product_id", goodsId);
        data.put("nonce_str", WXPayUtil.generateNonceStr());
        String sign = WXPayUtil.generateSignature(data, PropertyConfig.MCH_APP_KEY);
        data.put("sign", sign);
        Map<String, String> resp = null;
        try {
            resp = wxpay.unifiedOrder(data);
            System.out.println(resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }


    public static void main(String[] args) throws Exception {
    }
}
