package com.blog.blog.api.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.blog.bean.wechat.po.UnbindSubscribeUser;
import com.blog.blog.api.system.mapper.UnbindSubscribeUserMapper;
import org.springframework.stereotype.Service;

@Service
public class UnbindSubscribeUserService extends ServiceImpl<UnbindSubscribeUserMapper, UnbindSubscribeUser> {
}
