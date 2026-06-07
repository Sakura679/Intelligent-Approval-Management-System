package com.iams.controller;

import com.iams.params.UserParam;
import com.iams.service.UserService;
import com.iams.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * @Author: 放学后海堤日记
 * @Date: 2026/5/4 17:55
 * @Desc: 用户相关接口
 */
@RestController
@RequestMapping("/api/v1/user")
@Api(tags = "用户相关接口")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @ApiOperation("登录接口")
    public Result<Map<String, Object>> login(@ApiParam("用户请求参数") @RequestBody UserParam param) {
        return userService.login(param);
    }

    @PostMapping("/register")
    @ApiOperation("注册接口")
    public Result<Boolean> register(@ApiParam("用户请求参数") @RequestBody UserParam param) {
        return userService.register(param);
    }
}
