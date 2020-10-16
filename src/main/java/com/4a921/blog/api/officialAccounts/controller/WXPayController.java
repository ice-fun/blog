package com.knowswift.myspringboot.api.officialAccounts.controller;

import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static com.knowswift.myspringboot.config.PropertyConfig.MCH_APP_KEY;

/**
 * @author Li Yao Bing
 * @Company https://www.knowswift.com/
 * @Date 2020/4/16
 **/

@RestController
@RequestMapping("/WXPay/")
public class WXPayController extends BaseController {


    @RequestMapping("/payNotify")
    public String payNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //读取参数
        InputStream inputStream;
        StringBuffer sb = null;
        try {
            sb = new StringBuffer();
            inputStream = request.getInputStream();
            String s;
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            while ((s = in.readLine()) != null) {
                sb.append(s);
            }
            in.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //解析xml成map
        Map<String, String> map = WXPayUtil.xmlToMap(sb.toString());


        //过滤空 设置 TreeMap
        SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
        for (String parameter : map.keySet()) {
            String parameterValue = map.get(parameter);
            String v = "";
            if (null != parameterValue) {
                v = parameterValue.trim();
            }
            packageParams.put(parameter, v);
        }
        System.out.println(packageParams);
        //判断签名是否正确
        if (WXPayUtil.isSignatureValid(sb.toString(), MCH_APP_KEY)) {
            //------------------------------
            //处理业务开始
            //------------------------------
            String resXml = "";
            if ("SUCCESS".equals((String) packageParams.get("result_code"))) {
                // 这里是支付成功
                //////////执行自己的业务逻辑////////////////
                String mchId = (String) packageParams.get("mch_id");
                String openid = (String) packageParams.get("openid");
                String is_subscribe = (String) packageParams.get("is_subscribe");
                String outTradeNo = (String) packageParams.get("out_trade_no");
                String total_fee = (String) packageParams.get("total_fee");
                String cash_fee = (String) packageParams.get("cash_fee");
                String attach = (String) packageParams.get("attach");
                String transactionId = (String) packageParams.get("transaction_id");

                //////////执行自己的业务逻辑////////////////


                //通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";

            } else {
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                        + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                return ("fail");
            }
            //------------------------------
            //处理业务完毕
            //------------------------------
            try {
                BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
                out.write(resXml.getBytes());
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {

        }
        return "success";
    }

    @RequestMapping("/teacherPayNotify")
    public String teacherPayNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //读取参数
        InputStream inputStream;
        StringBuffer sb = null;
        try {
            sb = new StringBuffer();
            inputStream = request.getInputStream();
            String s;
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            while ((s = in.readLine()) != null) {
                sb.append(s);
            }
            in.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //解析xml成map
        Map<String, String> map = WXPayUtil.xmlToMap(sb.toString());


        //过滤空 设置 TreeMap
        SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
        for (String parameter : map.keySet()) {
            String parameterValue = map.get(parameter);
            String v = "";
            if (null != parameterValue) {
                v = parameterValue.trim();
            }
            packageParams.put(parameter, v);
        }
        System.out.println(packageParams);
        //判断签名是否正确
        if (WXPayUtil.isSignatureValid(sb.toString(), MCH_APP_KEY)) {
            //------------------------------
            //处理业务开始
            //------------------------------
            String resXml = "";
            if ("SUCCESS".equals((String) packageParams.get("result_code"))) {
                // 这里是支付成功
                //////////执行自己的业务逻辑////////////////
                String mchId = (String) packageParams.get("mch_id");
                String openid = (String) packageParams.get("openid");
                String is_subscribe = (String) packageParams.get("is_subscribe");
                String outTradeNo = (String) packageParams.get("out_trade_no");
                String total_fee = (String) packageParams.get("total_fee");
                String cash_fee = (String) packageParams.get("cash_fee");
                String attach = (String) packageParams.get("attach");
                String transactionId = (String) packageParams.get("transaction_id");

                //////////执行自己的业务逻辑////////////////


                //通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";

            } else {
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                        + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                return ("fail");
            }
            //------------------------------
            //处理业务完毕
            //------------------------------
            try {
                BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
                out.write(resXml.getBytes());
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return "success";
    }

    @RequestMapping("/refundNotify")
    public String refundNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //读取参数
        InputStream inputStream;
        StringBuffer sb = null;
        try {
            sb = new StringBuffer();
            inputStream = request.getInputStream();
            String s;
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            while ((s = in.readLine()) != null) {
                sb.append(s);
            }
            in.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //解析xml成map
        Map<String, String> map = WXPayUtil.xmlToMap(sb.toString());


        //过滤空 设置 TreeMap
        SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
        for (String parameter : map.keySet()) {
            String parameterValue = map.get(parameter);
            String v = "";
            if (null != parameterValue) {
                v = parameterValue.trim();
            }
            packageParams.put(parameter, v);
        }

        System.out.println(packageParams);
        //判断签名是否正确
        if (WXPayUtil.isSignatureValid(sb.toString(), MCH_APP_KEY)) {
            //------------------------------
            //处理业务开始
            //------------------------------
            String resXml = "";
            if ("SUCCESS".equals((String) packageParams.get("result_code"))) {
                // 这里是支付成功
                //////////执行自己的业务逻辑////////////////
                String mchId = (String) packageParams.get("mch_id");
                String openid = (String) packageParams.get("openid");
                String refundId = (String) packageParams.get("refund_id");
                String outTradeNo = (String) packageParams.get("out_trade_no");
                String total_fee = (String) packageParams.get("total_fee");
                String cash_fee = (String) packageParams.get("cash_fee");
                String attach = (String) packageParams.get("attach");
                String transactionId = (String) packageParams.get("transaction_id");

                //////////执行自己的业务逻辑////////////////


                //通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";

            } else {
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                        + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                return ("fail");
            }
            //------------------------------
            //处理业务完毕
            //------------------------------
            try {
                BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
                out.write(resXml.getBytes());
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {

        }
        return "success";
    }
}
