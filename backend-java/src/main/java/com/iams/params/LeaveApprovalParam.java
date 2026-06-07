package com.iams.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: 放学后海堤日记
 * @Date: 2026/5/14 10:57
 * @Desc:
 */
@Data
@ApiModel("请假审批请求参数模型")
public class LeaveApprovalParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("请假审批业务ID")
    private Long id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("请假原因")
    private String reason;

    @ApiModelProperty("类型")
    private Long typeId;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    private LocalDateTime createTimeMin;
    private LocalDateTime createTimeMax;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
    private LocalDateTime updateTimeMin;
    private LocalDateTime updateTimeMax;

    @ApiModelProperty("创建人")
    private Long createUser;

    @ApiModelProperty("更新人")
    private Long updateUser;

    @ApiModelProperty("审批状态: pending-待审 approved-通过 rejected-拒绝")
    private String status;

    @ApiModelProperty(value = "页码", example = "1")
    private Long pageNum = 1L;

    @ApiModelProperty(value = "每页数量", example = "10")
    private Long pageSize = 10L;

    @ApiModelProperty("审批人")
    private Long approvalUser;

    @ApiModelProperty("任务ID")
    private Long taskId;

    @ApiModelProperty("是否通过")
    private Boolean flag;
}
