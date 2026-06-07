package com.iams.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: 放学后海堤日记
 * @Date: 2026/5/23 12:20
 * @Desc:
 */
@Data
@ApiModel("用户VO")
public class UserVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户ID")
    private Long id;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("账户")
    private String account;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("角色ID")
    private Long roleId;
    private String roleName;

    @ApiModelProperty("状态ID")
    private Long statusId;
    private String statusName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("创建人")
    private Long createUser;
    private String createUserName;

    @ApiModelProperty("更新人")
    private Long updateUser;
    private String updateUserName;
}
