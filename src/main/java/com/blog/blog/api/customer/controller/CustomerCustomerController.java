package com.blog.blog.api.customer.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blog.blog.bean.common.BaseResponse;
import com.blog.blog.bean.customer.po.Customer;
import com.blog.blog.bean.customer.vo.CustomerVO;
import com.blog.blog.bean.user.po.User;
import com.blog.blog.bean.wechat.po.UnbindSubscribeUser;
import com.blog.blog.config.PropertyConfig;
import com.blog.blog.utils.JwtTokenUtils;
import com.blog.blog.utils.RandomStringUtils;
import com.blog.blog.utils.TxSmsUtils;
import com.blog.blog.utils.WrappedBeanCopier;
import com.blog.blog.annotation.EnableLog;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/teacher/index")
public class CustomerCustomerController extends CustomerBaseController {

    @PostMapping("/checkTokenStatus")
    public BaseResponse checkTokenStatus(@AuthenticationPrincipal Customer customer) {

        CustomerVO customerVO = WrappedBeanCopier.copyProperties(customer, CustomerVO.class);
        return BaseResponse.createSuccessResponse(customerVO);
    }


//    @PostMapping("/bindPhoneNumber")
//    @EnableLog(logName = "教师 绑定手机")
//    public BaseResponse bindPhoneNumber(@RequestBody CustomerVO customerVO) {
//        String code = customerVO.getCode();
//        String verifyCode = customerVO.getVerifyCode();
//        String phoneNumber = customerVO.getCustomerPhone();
//        Customer customer = customerCustomerService.getOne(new LambdaQueryWrapper<Customer>().eq(Customer::getCustomerPhone, phoneNumber));
//        if (customer == null) {
//            return BaseResponse.createFailResponse("请先联系管理员添加账号");
//        }
//        String vcKey = customer.getCustomerId() + "_vc";
//        if (!verifyCode.equals("000000")) {
//            String verifyCodeInRedis = (String) redisUtils.get(vcKey);
//            if (verifyCodeInRedis == null) {
//                return BaseResponse.createFailResponse("验证码无效");
//            }
//            if (!verifyCodeInRedis.equals(verifyCode)) {
//                return BaseResponse.createFailResponse("验证码不正确");
//            }
//        }
//        String unionId = (String) redisUtils.get(code + "_unionId");
//        String openId = (String) redisUtils.get(code + "_openId");
//        if (unionId == null && openId == null) {
//            return BaseResponse.createFailResponse("绑定失败，请重试");
//        }
//        redisUtils.del(vcKey);
//        redisUtils.del(code);
//        customer.setUnionId(unionId);
//        customer.setMiniProgramOpenId(openId);
//        customer.setTokenVersion(customer.getTokenVersion() + 1);
//        customer.setIsLogin(PropertyConfig.TRUE);
//        // 先将关注公众号信息置空
//        customer.setOfficialOpenId(null);
//        customer.setIsSubscribe(PropertyConfig.FALSE);
//
//        UnbindSubscribeUser unbindSubscribeUser = unbindSubscribeUserService.getOne(new LambdaQueryWrapper<UnbindSubscribeUser>()
//                .eq(UnbindSubscribeUser::getUnbindSubscribeUserUnionId, unionId));
//        //如果之前曾经关注过公众号就绑定一起
//        if (unbindSubscribeUser != null) {
//            customer.setOfficialOpenId(unbindSubscribeUser.getUnbindSubscribeUserOpenId());
//            customer.setIsSubscribe(PropertyConfig.TRUE);
//            //同一微信下 家长端也绑定
//            customerUserService.lambdaUpdate()
//                    .set(User::getOfficialOpenId, unbindSubscribeUser.getUnbindSubscribeUserOpenId())
//                    .set(User::getIsSubscribe, PropertyConfig.TRUE)
//                    .eq(User::getUnionId, unbindSubscribeUser.getUnbindSubscribeUserUnionId()).update();
//        }
//        boolean update = customerCustomerService.updateById(customer);
//        if (!update) {
//            return BaseResponse.createFailResponse("登录失败");
//        }
//        CustomerVO data = WrappedBeanCopier.copyProperties(customer, CustomerVO.class);
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("tokenVersion", customer.getTokenVersion());
//        String token = JwtTokenUtils.generateToken(claims, customer.getUsername());
//        data.setToken(token);
//        return BaseResponse.createSuccessResponse(data);
//    }

    @PostMapping("/verifyCode")
    public BaseResponse verifyCode(@RequestBody CustomerVO customerVO) {
        String phoneNumber = customerVO.getCustomerPhone();
        Customer customer = customerCustomerService.getOne(new LambdaQueryWrapper<Customer>().eq(Customer::getCustomerPhone, phoneNumber));
        if (customer == null) {
            return BaseResponse.createFailResponse("请先联系管理员添加账号");
        }
        String verifyCode = RandomStringUtils.createCode();
        redisUtils.set(customer.getCustomerId() + "_vc", verifyCode, 300);
        String s = TxSmsUtils.sendVerifyCode(phoneNumber, verifyCode);
        if (s != null) {
            return BaseResponse.createFailResponse("短信发送失败，请稍后重试");
        }
        return BaseResponse.createSuccessResponse();
    }


    @PostMapping("/changePhoneVerifyCode")
    public BaseResponse changePhoneVerifyCode(@RequestBody CustomerVO param) {
        String phoneNumber = param.getCustomerPhone();
        Customer customer = customerCustomerService.getOne(new LambdaQueryWrapper<Customer>().eq(Customer::getCustomerPhone, phoneNumber));
        if (customer != null) {
            return BaseResponse.createFailResponse("手机号码已被使用");
        }
        String verifyCode = RandomStringUtils.createCode();
        redisUtils.set(phoneNumber + "_cvc", verifyCode, 300);
        String s = TxSmsUtils.sendChangePhoneMessage(phoneNumber, verifyCode);
        if (s != null) {
            return BaseResponse.createFailResponse(s);
        }
        return BaseResponse.createSuccessResponse();
    }

    @PostMapping("/changePhone")
    @EnableLog(logName = "修改手机")
    public BaseResponse changePhone(@AuthenticationPrincipal Customer customer, @RequestBody CustomerVO param) {
        String phoneNumber = param.getCustomerPhone();
        String vcKey = phoneNumber + "_cvc";
        String verifyCodeInRedis = (String) redisUtils.get(vcKey);
        if (verifyCodeInRedis == null) {
            return BaseResponse.createFailResponse("验证码无效");
        }
        if (!verifyCodeInRedis.equals(param.getVerifyCode())) {
            return BaseResponse.createFailResponse("验证码不正确");
        }
        customer.setCustomerPhone(phoneNumber);
        customer.setTokenVersion(0);
        boolean result = customerCustomerService.updateById(customer);
        if (!result) {
            return BaseResponse.createFailResponse("修改失败");
        }
        return BaseResponse.createSuccessResponse();
    }
}
