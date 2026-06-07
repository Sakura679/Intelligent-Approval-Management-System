package com.iams.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: 放学后海堤日记
 * @Date: 2026/5/14 11:10
 * @Desc:
 */
@Data
@TableName("tb_leave_approval")
@ApiModel("请假审批实体类")
@Builder
public class LeaveApproval implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("请假审批ID")
    @TableId(type = IdType.AUTO)
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

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("创建人")
    private Long createUser;

    @ApiModelProperty("更新人")
    private Long updateUser;

    @ApiModelProperty("审批状态: pending-待审 approved-通过 rejected-拒绝")
    private String status;
}
