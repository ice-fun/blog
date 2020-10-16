package com.knowswift.myspringboot.api.admin.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knowswift.myspringboot.api.admin.mapper.AdminSystemLogMapper;
import com.knowswift.myspringboot.bean.system.po.SystemLog;
import org.springframework.stereotype.Service;

/**
 * author：LiYaoBing
 */

@Service
public class AdminSystemLogService extends ServiceImpl<AdminSystemLogMapper, SystemLog> {
}
