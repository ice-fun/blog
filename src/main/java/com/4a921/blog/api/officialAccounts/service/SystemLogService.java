package com.knowswift.myspringboot.api.officialAccounts.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knowswift.myspringboot.api.officialAccounts.mapper.SystemLogMapper;
import com.knowswift.myspringboot.bean.system.po.SystemLog;
import org.springframework.stereotype.Service;

/**
 * author：LiYaoBing
 * date: 2020/3/29 0029
 */

@Service
public class SystemLogService extends ServiceImpl<SystemLogMapper, SystemLog> {
}
