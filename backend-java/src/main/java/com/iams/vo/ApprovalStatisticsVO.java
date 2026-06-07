package com.iams.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: 放学后海堤日记
 * @Date: 2026/5/22
 * @Desc: 审批统计结果VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("审批统计结果VO")
public class ApprovalStatisticsVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("申请总数")
    private Long totalCount = 0L;

    @ApiModelProperty("待审数")
    private Long pendingCount = 0L;

    @ApiModelProperty("通过数")
    private Long approvedCount = 0L;

    @ApiModelProperty("拒绝数")
    private Long rejectedCount = 0L;

    @ApiModelProperty("审批数（通过+拒绝）")
    private Long processedCount = 0L;
}