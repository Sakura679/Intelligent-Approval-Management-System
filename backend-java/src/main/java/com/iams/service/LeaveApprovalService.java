package com.iams.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iams.params.LeaveApprovalParam;
import com.iams.pojo.LeaveApproval;
import com.iams.utils.PageResult;
import com.iams.utils.Result;
import com.iams.vo.LeaveApprovalVO;

import java.util.List;
import java.util.Map;

/**
 * @Author: 放学后海堤日记
 * @Date: 2026/5/14 11:10
 * @Desc:
 */
public interface LeaveApprovalService extends IService<LeaveApproval> {
    /**
     * @Author: 放学后海堤日记
     * @Date: 2026/5/14 11:18
     * @Desc: 启动请假流程
     */
    Result<String> initiateProcess(LeaveApprovalParam param);

    /**
     * @Author: 放学后海堤日记
     * @Date: 2026/5/14 11:18
     * @Desc: 获取请假流程列表
     */
    Result<List<LeaveApprovalVO>> getProcessList(LeaveApprovalParam param);

    /**
     * @Author: 放学后海堤日记
     * @Date: 2026/5/14 11:18
     * @Desc: 获取请假流程分页列表
     */
    PageResult<LeaveApprovalVO> getProcessPageList(LeaveApprovalParam param);

    /**
     * @Author: 放学后海堤日记
     * @Date: 2026/5/14 11:18
     * @Desc: 获取审批列表
     */
    PageResult<LeaveApprovalVO> getApprovePageList(LeaveApprovalParam param);

    /**
     * @Author: 放学后海堤日记
     * @Date: 2026/5/14 11:18
     * @Desc: 审批请假流程
     */
    Result<String> approveLeave(LeaveApprovalParam param);

    /**
     * @Author: 放学后海堤日记
     * @Date: 2026/5/14 11:18
     * @Desc: 审批统计
     */
    Result<Map<String, Object>> approvalStatistics();
}
