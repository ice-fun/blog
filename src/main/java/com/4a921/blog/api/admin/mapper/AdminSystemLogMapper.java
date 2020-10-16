package com.knowswift.myspringboot.api.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knowswift.myspringboot.bean.system.po.SystemLog;
import com.knowswift.myspringboot.bean.system.vo.SystemLogVO;
import org.apache.ibatis.annotations.Param;

/**
 * author：LiYaoBing
 */


public interface AdminSystemLogMapper extends BaseMapper<SystemLog> {
    Page<SystemLogVO> getList(@Param("keyword") String keyword,
                              @Param("resultType") Integer resultType, @Param("platform") String platform,
                              Page<SystemLogVO> page);
}
