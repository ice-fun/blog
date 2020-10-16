package com.knowswift.myspringboot.security.handler.success;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.knowswift.myspringboot.api.customer.service.CustomerCustomerService;
import com.knowswift.myspringboot.api.officialAccounts.service.UnbindSubscribeUserService;
import com.knowswift.myspringboot.bean.customer.po.Customer;
import com.knowswift.myspringboot.bean.customer.vo.CustomerVO;
import com.knowswift.myspringboot.bean.wechat.po.UnbindSubscribeUser;
import com.knowswift.myspringboot.bean.wechat.vo.WechatResponse;
import com.knowswift.myspringboot.security.handler.AbstractLoginSuccessHandler;
import com.knowswift.myspringboot.security.token.MiniProgramAuthenticationToken;
import com.knowswift.myspringboot.utils.JwtTokenUtils;
import com.knowswift.myspringboot.utils.RedisUtils;
import com.knowswift.myspringboot.utils.WrappedBeanCopier;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static com.knowswift.myspringboot.config.PropertyConfig.TRUE;


@Component
public class CustomerMiniProgramLoginSuccessHandler extends AbstractLoginSuccessHandler {
    @Resource
    CustomerCustomerService customerCustomerService;

    @Resource
    RedisUtils redisUtils;

    @Resource
    private UnbindSubscribeUserService unbindSubscribeUserService;

    @Override
    public Object preAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {

        MiniProgramAuthenticationToken miniProgramAuthenticationToken = (MiniProgramAuthenticationToken) authentication;
        Customer customer = (Customer) authentication.getPrincipal();
        String info = miniProgramAuthenticationToken.getInfo();
        WechatResponse wechatResponse = JSON.parseObject(info, WechatResponse.class);
        String avatarUrl = null;
        if (wechatResponse.getUserInfo() != null) {
            avatarUrl = wechatResponse.getUserInfo().getAvatarUrl();
        }
        Map<String, Object> data = new HashMap<>();
        if (customer.getUsername() == null) {
            redisUtils.set(miniProgramAuthenticationToken.getCode() + "_unionId", customer.getUnionId(), 300);
            redisUtils.set(miniProgramAuthenticationToken.getCode() + "_openId", customer.getMiniProgramOpenId(), 300);
            redisUtils.set(miniProgramAuthenticationToken.getCode() + "_avatar", avatarUrl, 300);
            data.put("code", miniProgramAuthenticationToken.getCode());
            sendResponse(httpServletResponse, 201, "请绑定手机号码", data);
            return null;
        }
        customer.setTokenVersion(customer.getTokenVersion() + 1);
        if (StringUtils.isBlank(customer.getUnionId())) {
            customer.setUnionId(wechatResponse.getUnionId());
            UnbindSubscribeUser user = unbindSubscribeUserService.getOne(new LambdaQueryWrapper<UnbindSubscribeUser>().eq(UnbindSubscribeUser::getUnbindSubscribeUserUnionId, wechatResponse.getUnionId()));
            if (user != null) {
                customer.setOfficialOpenId(user.getUnbindSubscribeUserOpenId());
                customer.setIsSubscribe(TRUE);
            }
        }
        customerCustomerService.updateById(customer);
        Map<String, Object> claims = new HashMap<>();
        claims.put("tokenVersion", customer.getTokenVersion());
        String token = JwtTokenUtils.generateToken(claims, customer.getUsername());
        CustomerVO customerVo = WrappedBeanCopier.copyProperties(customer, CustomerVO.class);
        customerVo.setToken(token);
        return customerVo;
    }
}
