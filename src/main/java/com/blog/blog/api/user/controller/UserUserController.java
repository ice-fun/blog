package com.blog.blog.api.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blog.blog.bean.common.BaseResponse;
import com.blog.blog.bean.customer.po.Customer;
import com.blog.blog.bean.system.po.SystemLog;
import com.blog.blog.bean.user.po.User;
import com.blog.blog.bean.user.vo.UserVO;
import com.blog.blog.bean.wechat.po.UnbindSubscribeUser;
import com.blog.blog.config.PropertyConfig;
import com.blog.blog.utils.JwtTokenUtils;
import com.blog.blog.utils.RandomStringUtils;
import com.blog.blog.utils.TxSmsUtils;
import com.blog.blog.utils.WrappedBeanCopier;
import com.blog.blog.annotation.EnableLog;
import com.blog.blog.security.token.JwtAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * author：LiYaoBing
 */
@RestController
@RequestMapping("/user/user")
public class UserUserController extends UserBaseController {


    @PostMapping("/checkTokenStatus")
    public BaseResponse checkTokenStatus(@AuthenticationPrincipal Authentication authentication) {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        User user = (User) authentication.getPrincipal();
        String token = jwtAuthenticationToken.getToken();
        if (JwtTokenUtils.isTokenAlmostExpired(jwtAuthenticationToken.getToken())) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("tokenVersion", user.getTokenVersion());
            token = JwtTokenUtils.generateToken(claims, user.getUsername());
        }
        UserVO userVO = WrappedBeanCopier.copyProperties(user, UserVO.class);

        userVO.setToken(token);


        return BaseResponse.createSuccessResponse(userVO);
    }

//    @PostMapping("/bindPhoneNumber")
//    public BaseResponse bindPhoneNumber(@RequestBody UserVO userVo) {
//        String code = userVo.getCode();
//        String verifyCode = userVo.getVerifyCode();
//        String phoneNumber = userVo.getUserPhone();
//        User user = userUserService.getOne(new LambdaQueryWrapper<User>().eq(User::getUserPhone, phoneNumber));
//        if (user == null) {
//            return BaseResponse.createFailResponse("请先联系老师添加账号");
//        }
//        String vcKey = user.getUserId() + "_vc";
//
//        String verifyCodeInRedis = (String) redisUtils.get(vcKey);
//        if (verifyCodeInRedis == null) {
//            return BaseResponse.createFailResponse("验证码无效");
//        }
//        if (!verifyCodeInRedis.equals(verifyCode)) {
//            return BaseResponse.createFailResponse("验证码不正确");
//        }
//
//        String unionId = (String) redisUtils.get(code + "_unionId");
//        String openId = (String) redisUtils.get(code + "_openId");
//        if (unionId == null && openId == null) {
//            return BaseResponse.createFailResponse("绑定失败，请重试");
//        }
//        redisUtils.del(vcKey);
//        redisUtils.del(code);
//        user.setUnionId(unionId);
//        user.setMiniProgramOpenId(openId);
//        // 先将关注公众号信息置空
//        user.setOfficialOpenId(null);
//        user.setIsSubscribe(PropertyConfig.FALSE);
//
//        user.setTokenVersion(user.getTokenVersion() + 1);
//        UnbindSubscribeUser unbindSubscribeUser = unbindSubscribeUserService.getOne(new LambdaQueryWrapper<UnbindSubscribeUser>()
//                .eq(UnbindSubscribeUser::getUnbindSubscribeUserUnionId, unionId));
//        //如果之前曾经关注过公众号就绑定一起
//        if (unbindSubscribeUser != null) {
//            user.setOfficialOpenId(unbindSubscribeUser.getUnbindSubscribeUserOpenId());
//            user.setIsSubscribe(PropertyConfig.TRUE);
//            userCustomerService.lambdaUpdate()
//                    .set(Customer::getOfficialOpenId, unbindSubscribeUser.getUnbindSubscribeUserOpenId())
//                    .set(Customer::getIsSubscribe, PropertyConfig.TRUE)
//                    .eq(Customer::getUnionId, unionId).update();
//        }
//        user.setIsLogin(PropertyConfig.TRUE);
//        boolean update = userUserService.updateById(user);
//        if (!update) {
//            return BaseResponse.createFailResponse("登录失败");
//        }
//        UserVO data = WrappedBeanCopier.copyProperties(user, UserVO.class);
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("tokenVersion", user.getTokenVersion());
//        String token = JwtTokenUtils.generateToken(claims, user.getUsername());
//        data.setToken(token);
//        return BaseResponse.createSuccessResponse(data);
//    }

    @PostMapping("/verifyCode")
    public BaseResponse verifyCode(@RequestBody UserVO userVo) {
        String phoneNumber = userVo.getUserPhone();
        User user = userUserService.getOne(new LambdaQueryWrapper<User>().eq(User::getUserPhone, phoneNumber));
        if (user == null) {
            return BaseResponse.createFailResponse("手机号码未注册");
        }
        if (PropertyConfig.TRUE.equals(user.getIsLock())) {
            return BaseResponse.createFailResponse("该账号已冻结");
        }
        String verifyCode = RandomStringUtils.createCode();
        redisUtils.set(user.getUserId() + "_vc", verifyCode, 300);
        String s = TxSmsUtils.sendVerifyCode(phoneNumber, verifyCode);
        if (s != null) {
            SystemLog systemLog = new SystemLog();
            systemLog.setException(s);
            systemLogService.save(systemLog);
            return BaseResponse.createFailResponse("短信发送失败，请稍后重试");
        }
        return BaseResponse.createSuccessResponse();
    }

    @PostMapping("/changePhoneVerifyCode")
    public BaseResponse changePhoneVerifyCode(@RequestBody UserVO param) {
        String phoneNumber = param.getUserPhone();
        User user1 = userUserService.getBaseMapper().getByPhone(phoneNumber);
        if (user1 != null && user1.getIsLock().equals(PropertyConfig.FALSE)) {
            return BaseResponse.createFailResponse("手机号码已被使用");
        }
        String verifyCode = RandomStringUtils.createCode();
        redisUtils.set(phoneNumber + "_cvc", verifyCode, 300);
        String s = TxSmsUtils.sendChangePhoneMessage(phoneNumber, verifyCode);
        if (s != null) {
            SystemLog systemLog = new SystemLog();
            systemLog.setException(s);
            systemLogService.save(systemLog);
            return BaseResponse.createFailResponse("短信发送失败，请稍后重试");
        }
        return BaseResponse.createSuccessResponse();
    }

    @PostMapping("/changePhone")
    @EnableLog(logName = "修改手机")
    public BaseResponse changePhone(@AuthenticationPrincipal User user, @RequestBody UserVO param) {
        String phoneNumber = param.getUserPhone();
        String vcKey = phoneNumber + "_cvc";
        String verifyCodeInRedis = (String) redisUtils.get(vcKey);
        if (verifyCodeInRedis == null) {
            return BaseResponse.createFailResponse("验证码无效");
        }
        if (!verifyCodeInRedis.equals(param.getVerifyCode())) {
            return BaseResponse.createFailResponse("验证码不正确");
        }
        user.setUserPhone(param.getUserPhone());
        user.setTokenVersion(0);
        boolean result = userUserService.updateById(user);
        if (!result) {
            return BaseResponse.createFailResponse("修改失败");
        }
        return BaseResponse.createSuccessResponse();
    }


//    @PostMapping("/unbind")
//    public BaseResponse unbind(@AuthenticationPrincipal User user) {
//        user.setIsSubscribe(0);
//        user.setOfficialOpenId(null);
//        user.setUnionId(null);
//        user.setMiniProgramOpenId(null);
//        boolean update = userUserService.updateById(user);
//        return BaseResponse.createSuccessOrFailResponse(update, "解绑失败");
//    }
}
