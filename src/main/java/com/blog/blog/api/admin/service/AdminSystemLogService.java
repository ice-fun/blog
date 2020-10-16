package com.blog.blog.api.admin.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.blog.api.admin.mapper.AdminSystemLogMapper;
import com.blog.blog.bean.system.po.SystemLog;
import org.springframework.stereotype.Service;

/**
 * author：LiYaoBing
 */

@Service
public class AdminSystemLogService extends ServiceImpl<AdminSystemLogMapper, SystemLog> {
}
