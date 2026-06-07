package com.iams.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: 放学后海堤日记
 * @Date: 2026/5/6 10:47
 * @Desc:
 */
@Data
@ApiModel("登录参数")
public class UserParam implements Serializable {
    private static final Long serialVersionUID = 1L;

    @ApiModelProperty("用户ID")
    private Long id;

    @ApiModelProperty("账户")
    private String account;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTimeMax;
    private LocalDateTime createTimeMin;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTimeMax;
    private LocalDateTime updateTimeMin;

    @ApiModelProperty("创建人")
    private Long createUser;

    @ApiModelProperty("更新人")
    private Long updateUser;
}
