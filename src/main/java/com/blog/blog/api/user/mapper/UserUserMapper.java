package com.blog.blog.api.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.blog.bean.user.po.User;
import com.blog.blog.bean.user.vo.UserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * author：LiYaoBing
 */

public interface UserUserMapper extends BaseMapper<User> {

    User getByPhone(@Param("phone") String phone);

    List<UserVO> getChildAllParent(@Param("childId") String childId);
}
