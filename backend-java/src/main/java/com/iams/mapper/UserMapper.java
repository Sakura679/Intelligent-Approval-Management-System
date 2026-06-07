package com.iams.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iams.params.UserParam;
import com.iams.pojo.User;
import com.iams.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: 放学后海堤日记
 * @Date: 2026/5/4 18:00
 * @Desc:
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    List<UserVO> selectUser(@Param("param") UserParam param);
}
