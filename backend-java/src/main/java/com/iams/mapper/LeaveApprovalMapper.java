package com.iams.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iams.params.LeaveApprovalParam;
import com.iams.pojo.LeaveApproval;
import com.iams.vo.ApprovalStatisticsVO;
import com.iams.vo.LeaveApprovalVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: 放学后海堤日记
 * @Date: 2026/5/14 11:20
 * @Desc:
 */
@Mapper
public interface LeaveApprovalMapper extends BaseMapper<LeaveApproval> {
    Long insertApproval(@Param("param") LeaveApproval leaveApproval);

    List<LeaveApprovalVO> getProcessList(@Param("param") LeaveApprovalParam param);

    IPage<LeaveApprovalVO> getProcessPageList(@Param("param") LeaveApprovalParam param,
                                              @Param("page") Page<LeaveApprovalVO> page);

    IPage<LeaveApprovalVO> getApprovePageList(@Param("param") LeaveApprovalParam param,
                                              @Param("page") Page<LeaveApprovalVO> page);

    ApprovalStatisticsVO approvalStatistics(@Param("userId") Long userId,
                                            @Param("startTime") LocalDateTime startTime,
                                            @Param("endTime") LocalDateTime endTime);

    Long statisticsProcessd(@Param("userId") Long userId,
                            @Param("startTime") LocalDateTime startTime,
                            @Param("endTime") LocalDateTime endTime);
}
