package com.knowswift.myspringboot.utils;

import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.knowswift.myspringboot.config.PropertyConfig;

public class TxSmsUtils {

    public static String sendVerifyCode(String phoneNumber, String code) {
        try {
            SmsSingleSender ssender = new SmsSingleSender(PropertyConfig.TECENT_MSG_APP_ID, PropertyConfig.TECENT_MSG_APP_KEY);
            String[] params = {code, "5"};
            SmsSingleSenderResult result = ssender.sendWithParam("86", phoneNumber, 133084, params, "爱贝家园", "", "");
            String errMsg = result.errMsg;
            if (!errMsg.equals("OK")) {
                return errMsg;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public static String sendAttenMessage(String[] phoneNumbers) {
        return sendMessages(285820, phoneNumbers, new String[]{});
    }

    public static String sendAction(String phoneNumber, String name) {
        String[] phoneNumbers = new String[]{phoneNumber};
        return sendMessages(668276, phoneNumbers, new String[]{name});
    }

    public static String remindNotice(String phoneNumber, String name, String kindergartenName) {
        String[] phoneNumbers = new String[]{phoneNumber};
        return sendMessages(668274, phoneNumbers, new String[]{name, kindergartenName});
    }

    public static String sendChangePhoneMessage(String phoneNumber, String code) {
        String[] phoneNumbers = new String[]{phoneNumber};
        return sendMessages(659558, phoneNumbers, new String[]{code});
    }

    private static String sendMessages(int templateId, String[] phoneNumbers, String[] params) {
        SmsMultiSender smsMultiSender = new SmsMultiSender(PropertyConfig.TECENT_MSG_APP_ID, PropertyConfig.TECENT_MSG_APP_KEY);
        SmsMultiSenderResult result = null;
        try {
            result = smsMultiSender.sendWithParam("86", phoneNumbers, templateId, params, "爱贝家园", "", "");
            String errMsg = result.errMsg;
            if (!errMsg.equals("OK")) {
                return errMsg;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

//    public static boolean sendMultiSms(int type, String[] phoneNumbers) {
//
//        if (EnumUtils.getEnumBycode(SmsTemplateEnum.class, type) == null) {
//            return false;
//        }
//        int templateId = EnumUtils.getEnumBycode(SmsTemplateEnum.class, type).getTemplateId();
//        String[] params2 = {};
//        try {
//            SmsMultiSender msender = new SmsMultiSender(Config.TxSmsAppId, Config.TxSmsAppkey);
//            SmsMultiSenderResult result = msender.sendWithParam("1", phoneNumbers, templateId, params2, "Dida", "", "");
//            System.out.println(result);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public static String sendMultiSmsWithTemplateId(int templateId, String[] phoneNumbers) {
//        String[] params = {};
//        SmsMultiSender smsMultiSender = new SmsMultiSender(Config.TxSmsAppId, Config.TxSmsAppkey);
//        SmsMultiSenderResult result = null;
//        try {
//            result = smsMultiSender.sendWithParam("1", phoneNumbers, templateId, params, "Dida", "", "");
//            System.out.println(result);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "";
//        }
//        return result.toString();
//    }

    public static void main(String[] args) {
        TxSmsUtils.sendVerifyCode("18320306595", "123456");

        //String str = "7788991010,6046136085,6046136085,7787076188,7788919199,6046188922,6046188922,6043659277,6047108312,7788816668,6047621216,7788228136,7789855321,6045512911,6047818811,7785225518,7789686789,7788554869,7783870622,7788856389,7788985865,7783235589,7788892881,7788821733,6046683968,7782388519,7788839798,7788639567,7783871942,6047288326,6043478678,7782401221,7787128181,7782312233,7789280567,6046498232,7788856001,6047633968,6047250388,7788632838,7788919199,7788833363,6043450199,6045536662,6049688809,7789686789,6047673355,7788472299,7788479919,6047676683,7789572882,6047104460,7786362268,6046163677,6047673355,7788830453,6047676683,6047673358,6047646346,6043652588,6047676683,7788552219,7783219911,6047250388,6043490529,7783087777,6042182285,6045630746,7788592338,7786365366,6047628836,7782518877,6047222925,7783878373,7788611791,7787076188,7783021900,7789603766,7788988609,6042632563,6043639782,7788882025,7789388751,7788287609,7783163678,7789181210,6042835780,7787754369,7788933839,7783181638,7785518848,7788897328,6043665183,6043631129,2368658168,7788615717,6043478678,7789573888,7788991518";
        //String str = "7788991010";

        //String[] phoneNumbers = str.split(",");
        //TxSmsUtils.sendMultiSms(20,phoneNumbers);
//        TxSmsUtils.sendMultiSmsWithTemplateId(SmsTemplateEnum.CnOrderCanceled.getTemplateId(), new String[]{"7788991010"});
    }

}
