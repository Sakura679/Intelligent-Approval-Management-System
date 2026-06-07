package com.iams.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iams.mapper.UserMapper;
import com.iams.params.UserParam;
import com.iams.pojo.User;
import com.iams.service.UserService;
import com.iams.utils.JwtUtils;
import com.iams.utils.Result;
import com.iams.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 放学后海堤日记
 * @Date: 2026/5/4 17:59
 * @Desc:
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * @Author: 放学后海堤日记
     * @Date: 2026/5/4 18:26
     * @Desc: 登录接口实现
     */
    @Override
    public Result<Map<String, Object>> login(UserParam param) {
        List<UserVO> users = userMapper.selectUser(param);

        if (users == null || users.isEmpty()) {
            return Result.error("用户不存在");
        }

        UserVO user = users.get(0);

        if (!passwordEncoder.matches(param.getPassword(), user.getPassword())) {
            return Result.error("密码错误");
        }

        String token = JwtUtils.generateToken(user.getId(), user.getAccount(), user.getAccount());

        HashMap<String, Object> map = new HashMap<>();
        map.put("token", token);

        user.setPassword(null);
        map.put("user", user);

        return Result.success(map);
    }

    /**
     * @Author: 放学后海堤日记
     * @Date: 2026/5/4 18:26
     * @Desc: 注册接口实现
     */
    @Override
    public Result<Boolean> register(UserParam param) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        User user = userMapper.selectOne(queryWrapper
                .eq(User::getAccount, param.getAccount())
        );

        if (user != null) {
            return Result.error("用户已存在");
        }

        // 密码加密
        String encryptedPassword = passwordEncoder.encode(param.getPassword());

        user = new User();
        user.setAccount(param.getAccount());
        user.setName(param.getAccount());
        user.setRoleId(4L);
        user.setStatusId(3L);
        user.setPassword(encryptedPassword);

        LocalDateTime dateTime = LocalDateTime.now();
        user.setCreateTime(dateTime);
        user.setUpdateTime(dateTime);

        int rows = userMapper.insert(user);
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        userMapper.update(updateWrapper
                .set(User::getCreateUser, user.getId())
                .set(User::getUpdateUser, user.getId())
                .eq(User::getId, user.getId())
        );
        if (rows <= 0) {
            return Result.error("注册失败");
        }

        return Result.success(true);
    }
}
