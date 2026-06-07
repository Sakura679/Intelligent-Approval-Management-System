package com.iams.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iams.params.UserParam;
import com.iams.pojo.User;
import com.iams.utils.Result;

import java.util.Map;

/**
 * @Author: 放学后海堤日记
 * @Date: 2026/5/4 17:56
 * @Desc:
 */
public interface UserService extends IService<User> {
    /**
     * @Author: 放学后海堤日记
     * @Date: 2026/5/4 18:27
     * @Desc: 登录接口
     */
    Result<Map<String, Object>> login(UserParam param);

    /**
     * @Author: 放学后海堤日记
     * @Date: 2026/5/4 18:27
     * @Desc: 注册接口
     */
    Result<Boolean> register(UserParam param);
}
