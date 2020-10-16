package com.knowswift.myspringboot.api.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knowswift.myspringboot.bean.user.po.User;
import com.knowswift.myspringboot.bean.user.vo.UserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * author：LiYaoBing
 */

public interface UserUserMapper extends BaseMapper<User> {

    User getByPhone(@Param("phone") String phone);

    List<UserVO> getChildAllParent(@Param("childId") String childId);
}
