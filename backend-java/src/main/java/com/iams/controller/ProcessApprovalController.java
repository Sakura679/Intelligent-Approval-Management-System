package com.iams.controller;

import com.iams.params.LeaveApprovalParam;
import com.iams.service.LeaveApprovalService;
import com.iams.utils.PageResult;
import com.iams.utils.Result;
import com.iams.vo.LeaveApprovalVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @Author: 放学后海堤日记
 * @Date: 2026/5/14 10:51
 * @Desc:
 */
@RestController
@Api(tags = "审批管理")
@RequestMapping("/api/v1/approval")
public class ProcessApprovalController {

    @Autowired
    private LeaveApprovalService leaveApprovalService;

    @PostMapping("/leave")
    @ApiOperation("发起请假流程")
    public Result<String> initiateProcess(@ApiParam("请假参数") @RequestBody LeaveApprovalParam param) {
        return leaveApprovalService.initiateProcess(param);
    }

    @PostMapping("/list")
    @ApiOperation("查询我的列表")
    public Result<List<LeaveApprovalVO>> getProcessList(@ApiParam("查询参数") @RequestBody LeaveApprovalParam param) {
        return leaveApprovalService.getProcessList(param);
    }

    @PostMapping("/page/list")
    @ApiOperation("分页查询我的列表")
    public PageResult<LeaveApprovalVO> getProcessPageList(@ApiParam("查询参数") @RequestBody LeaveApprovalParam param) {
        return leaveApprovalService.getProcessPageList(param);
    }

    @PostMapping("/approve/page/list")
    @ApiOperation("分页查询审批列表")
    public PageResult<LeaveApprovalVO> getApprovePageList(@ApiParam("查询参数") @RequestBody LeaveApprovalParam param) {
        return leaveApprovalService.getApprovePageList(param);
    }

    @PostMapping("/approve/leave")
    @ApiOperation("审批")
    public Result<String> approveLeave(@ApiParam("审批参数") @RequestBody LeaveApprovalParam param) {
        return leaveApprovalService.approveLeave(param);
    }

    @GetMapping("/statistics")
    @ApiOperation("审批统计")
    public Result<Map<String, Object>> approvalStatistics() {
        return leaveApprovalService.approvalStatistics();
    }

    @ApiOperation("流程追踪")
    @GetMapping("/trace")
    public void trace(@RequestParam("businessId") Long businessId, HttpServletResponse response) {
        leaveApprovalService.trace(businessId, response);
    }
}
