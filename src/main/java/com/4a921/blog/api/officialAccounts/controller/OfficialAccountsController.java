package com.knowswift.myspringboot.api.officialAccounts.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.knowswift.myspringboot.annotation.IgnoreLog;
import com.knowswift.myspringboot.api.customer.service.CustomerCustomerService;
import com.knowswift.myspringboot.api.officialAccounts.service.UnbindSubscribeUserService;
import com.knowswift.myspringboot.api.user.service.UserUserService;
import com.knowswift.myspringboot.bean.customer.po.Customer;
import com.knowswift.myspringboot.bean.user.po.User;
import com.knowswift.myspringboot.bean.wechat.po.UnbindSubscribeUser;
import com.knowswift.myspringboot.bean.wechat.vo.PlaintextWechatMessage;
import com.knowswift.myspringboot.bean.wechat.vo.WechatUserInfo;
import com.knowswift.myspringboot.config.PropertyConfig;
import com.knowswift.myspringboot.utils.XMLTranslateUtils;
import com.knowswift.myspringboot.utils.wechat.WechatOfficialAccountUtils;
import com.knowswift.myspringboot.utils.wechat.aes.AesException;
import com.knowswift.myspringboot.utils.wechat.aes.SHA1;
import com.knowswift.myspringboot.utils.wechat.aes.WXBizMsgCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.knowswift.myspringboot.config.PropertyConfig.FALSE;
import static com.knowswift.myspringboot.config.PropertyConfig.TRUE;

@Controller
@RequestMapping("/wechatOfficialAccounts")
public class OfficialAccountsController {
    @Resource
    private WXBizMsgCrypt wxBizMsgCrypt;

    @Resource
    private CustomerCustomerService customerCustomerService;

    @Resource
    private UserUserService userUserService;

    @Resource
    private UnbindSubscribeUserService unbindSubscribeUserService;

    @ResponseBody
    @GetMapping("/getMessage")
    @IgnoreLog
    public String wechatCheck(@RequestParam("signature") String signature,
                              @RequestParam("timestamp") String timestamp,
                              @RequestParam("nonce") String nonce,
                              @RequestParam("echostr") String echoStr,
                              HttpServletRequest request) {
        String token = PropertyConfig.WECHAT_OFFICIAL_ACCOUNTS_TOKEN;
        try {
            String echo = SHA1.getSHA1(token, timestamp, nonce, "");
            if (echo.equals(signature)) {
                return echoStr;
            }
        } catch (AesException e) {
            e.printStackTrace();
        }
        return null;
    }

    @ResponseBody
    @PostMapping("/getMessage")
    @IgnoreLog
    public String processMsg(@RequestBody String xml,
                             @RequestParam(value = "signature", required = false) String signature,
                             @RequestParam(value = "timestamp", required = false) String timeStamp,
                             @RequestParam(value = "nonce", required = false) String nonce,
                             @RequestParam(value = "encrypt_type", required = false) String encryptType,
                             @RequestParam(value = "msg_signature", required = false) String msgSignature,
                             HttpServletRequest request) throws AesException {
        String plaintext = wxBizMsgCrypt.decryptMsg(msgSignature, timeStamp, nonce, xml);
        PlaintextWechatMessage plaintextObject = XMLTranslateUtils.converToJavaBean(plaintext, PlaintextWechatMessage.class);
        String accountId = plaintextObject.getToUserName();
        String openId = plaintextObject.getFromUserName();
        String unionId = null;
        boolean isTeacher = false;
        boolean isParent = false;
        System.out.println(plaintextObject);
        switch (plaintextObject.getMsgType()) {
            //发送文本
            case "text":
                break;
            //事件
            case "event":
                switch (plaintextObject.getEvent()) {
                    //关注
                    case "subscribe":
                        WechatUserInfo wechatUserInfo = WechatOfficialAccountUtils.getUserInfo(openId);
                        if (wechatUserInfo == null) {
                            return null;
                        }
                        unionId = wechatUserInfo.getUnionId();
                        isTeacher = customerCustomerService.lambdaUpdate()
                                .eq(Customer::getUnionId, unionId)
                                .set(Customer::getOfficialOpenId, openId)
                                .set(Customer::getIsSubscribe, TRUE).update();
                        isParent = userUserService.lambdaUpdate()
                                .eq(User::getUnionId, unionId)
                                .set(User::getOfficialOpenId, openId)
                                .set(User::getIsSubscribe, TRUE).update();

                        //都不是
                        String content = "欢迎关注爱贝家园公众号";
//                        if (!(isTeacher || isParent)) {
                        UnbindSubscribeUser unbindSubscribeUser = new UnbindSubscribeUser();
                        unbindSubscribeUser.setUnbindSubscribeUserOpenId(openId);
                        unbindSubscribeUser.setUnbindSubscribeUserUnionId(unionId);
                        int row = unbindSubscribeUserService.count(new LambdaQueryWrapper<UnbindSubscribeUser>()
                                .eq(UnbindSubscribeUser::getUnbindSubscribeUserOpenId, openId));
                        if (row == 0) {
                            unbindSubscribeUserService.save(unbindSubscribeUser);
                        }
//                        }
                        return WechatOfficialAccountUtils.createEncodeReply(accountId, openId, content, timeStamp, nonce);
                    //取消关注
                    case "unsubscribe":
                        wechatUserInfo = WechatOfficialAccountUtils.getUserInfo(openId);
                        if (wechatUserInfo == null) {
                            return null;
                        }
                        unionId = wechatUserInfo.getUnionId();
//                        isTeacher = teacherTeacherService.lambdaUpdate()
//                                .eq(Teacher::getUnionId, unionId)
//                                .set(Teacher::getSubscriptionOpenId, null)
//                                .set(Teacher::getIsFocusSubscription, FALSE).update();
                        isTeacher = customerCustomerService.lambdaUpdate()
                                .eq(Customer::getOfficialOpenId, openId)
                                .set(Customer::getIsSubscribe, FALSE).update();
                        isParent = userUserService.lambdaUpdate()
                                .eq(User::getOfficialOpenId, openId)
                                .set(User::getIsSubscribe, FALSE).update();
//                        if (!(isTeacher || isParent)) {
                        unbindSubscribeUserService.remove(new LambdaQueryWrapper<UnbindSubscribeUser>()
                                .eq(UnbindSubscribeUser::getUnbindSubscribeUserOpenId, openId));
//                        }
                        break;
                    default: {

                    }
                }

            default: {

            }
        }
        return null;
    }

}
