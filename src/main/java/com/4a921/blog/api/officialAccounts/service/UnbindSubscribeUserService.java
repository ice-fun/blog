package com.knowswift.myspringboot.api.officialAccounts.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knowswift.myspringboot.api.officialAccounts.mapper.UnbindSubscribeUserMapper;
import com.knowswift.myspringboot.bean.wechat.po.UnbindSubscribeUser;
import org.springframework.stereotype.Service;

@Service
public class UnbindSubscribeUserService extends ServiceImpl<UnbindSubscribeUserMapper, UnbindSubscribeUser> {
}
