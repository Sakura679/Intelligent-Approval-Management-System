package com.iams.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: 放学后海堤日记
 * @Date: 2026/5/15 16:21
 * @Desc:
 */
@Data
@ApiModel("请假审批数据传输模型")
public class LeaveApprovalVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("请假审批ID")
    private Long id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("请假原因")
    private String reason;

    @ApiModelProperty("审批类型")
    private Long typeId;
    private String typeName;

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

    @ApiModelProperty("审批状态: pending-待审 approved-通过 rejected-拒绝")
    private String status;
    public String getStatus() {
        String status = "";
        switch (this.status) {
            case "pending":
                status = "待审";
                break;
            case "approved":
                status = "通过";
                break;
            case "rejected":
                status = "拒绝";
                break;
        }

        return status;
    }

    @ApiModelProperty("审批任务ID")
    private Long taskId;
}
