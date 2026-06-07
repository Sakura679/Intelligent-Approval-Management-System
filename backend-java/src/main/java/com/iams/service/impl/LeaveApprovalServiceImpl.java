package com.iams.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iams.annotations.AutoFilled;
import com.iams.context.UserContext;
import com.iams.enums.AutoFillType;
import com.iams.enums.ProcessStatus;
import com.iams.mapper.LeaveApprovalMapper;
import com.iams.mapper.UserMapper;
import com.iams.params.LeaveApprovalParam;
import com.iams.pojo.LeaveApproval;
import com.iams.pojo.User;
import com.iams.service.LeaveApprovalService;
import com.iams.utils.PageResult;
import com.iams.utils.Result;
import com.iams.vo.ApprovalStatisticsVO;
import com.iams.vo.LeaveApprovalVO;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 放学后海堤日记
 * @Date: 2026/5/14 11:10
 * @Desc:
 */
@Service
public class LeaveApprovalServiceImpl extends ServiceImpl<LeaveApprovalMapper, LeaveApproval> implements LeaveApprovalService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private LeaveApprovalMapper leaveApprovalMapper;

    @Autowired
    private UserMapper userMapper;

    private static final String PROCESS_DEFINITION_KEY = "leave-approval";

    /**
     * @Author: 放学后海堤日记
     * @Date: 2026/5/14 11:10
     * @Desc: 启动流程
     */
    @Override
    @AutoFilled(AutoFillType.INSERT)
    @Transactional(rollbackFor = Exception.class)
    public Result<String> initiateProcess(LeaveApprovalParam param) {
        // 业务代码
        // 先查询是否存在为完成的请假流程
        LeaveApproval leaveApproval = leaveApprovalMapper.selectOne(new LambdaQueryWrapper<LeaveApproval>()
                .eq(LeaveApproval::getCreateUser, UserContext.getCurrentUserId())
                .eq(LeaveApproval::getTypeId, param.getTypeId())
                .ne(LeaveApproval::getStatus, ProcessStatus.APPROVED));
        if (leaveApproval != null) {
            return Result.error("当前有正在审批的请假申请");
        }
        // 查询是否存在未结束的请假流程
        leaveApproval = leaveApprovalMapper.selectOne(new LambdaQueryWrapper<LeaveApproval>()
                .eq(LeaveApproval::getCreateUser, UserContext.getCurrentUserId())
                .gt(LeaveApproval::getEndTime, LocalDateTime.now()));
        if (leaveApproval != null) {
            return Result.error("当前有尚未到期的请假申请");
        }

        leaveApproval = LeaveApproval.builder()
                .title(param.getTitle())
                .startTime(param.getStartTime())
                .endTime(param.getEndTime())
                .reason(param.getReason())
                .typeId(param.getTypeId())
                .status(ProcessStatus.PENDING.getValue())
                .createTime(param.getCreateTime())
                .updateTime(param.getUpdateTime())
                .createUser(param.getCreateUser())
                .updateUser(param.getUpdateUser())
                .build();
        leaveApprovalMapper.insertApproval(leaveApproval);

        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getRoleId, 3));

        HashMap<String, Object> map = new HashMap<>();
        map.put("businessId", leaveApproval.getId());
        map.put("zuzhang", user.getId());
        String processDefinitionId = getProcessDefinitionId();
        runtimeService.startProcessInstanceById(processDefinitionId, map);

        return Result.success();
    }

    /**
     * @Author: 放学后海堤日记
     * @Date: 2026/5/14 11:18
     * @Desc: 获取请假流程列表
     */
    @Override
    public Result<List<LeaveApprovalVO>> getProcessList(LeaveApprovalParam param) {
        param.setCreateUser(UserContext.getCurrentUserId());
        return Result.success(leaveApprovalMapper.getProcessList(param));
    }

    /**
     * @Author: 放学后海堤日记
     * @Date: 2026/5/14 11:18
     * @Desc: 获取请假流程列表
     */
    @Override
    public PageResult<LeaveApprovalVO> getProcessPageList(LeaveApprovalParam param) {
        Page<LeaveApprovalVO> page = Page.of(param.getPageNum(), param.getPageSize());
        param.setCreateUser(UserContext.getCurrentUserId());
        IPage<LeaveApprovalVO> iPage = leaveApprovalMapper.getProcessPageList(param, page);
        return PageResult.success(iPage);
    }

    /**
     * @Author: 放学后海堤日记
     * @Date: 2026/5/14 11:18
     * @Desc: 获取审批列表
     */
    @Override
    public PageResult<LeaveApprovalVO> getApprovePageList(LeaveApprovalParam param) {
        Page<LeaveApprovalVO> page = Page.of(param.getPageNum(), param.getPageSize());
        param.setApprovalUser(UserContext.getCurrentUserId());

        IPage<LeaveApprovalVO> iPage = leaveApprovalMapper.getApprovePageList(param, page);
        return PageResult.success(iPage);
    }

    /**
     * @Author: 放学后海堤日记
     * @Date: 2026/5/14 11:18
     * @Desc: 审批请假流程
     */
    @Override
    @AutoFilled(AutoFillType.UPDATE)
    @Transactional(rollbackFor = Exception.class)
    public Result<String> approveLeave(LeaveApprovalParam param) {
        // 业务处理
        HashMap<String, Object> map = new HashMap<>();
        map.put("flag1", param.getFlag());
        if (param.getFlag()) {
            User currentUser = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, UserContext.getCurrentUserId()));
            if (currentUser.getRoleId() == 3) {
                User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                        .eq(User::getRoleId, 2));
                map.put("jingli", user.getId());
            } else if (currentUser.getRoleId() == 2) {
                leaveApprovalMapper.update(new LambdaUpdateWrapper<LeaveApproval>()
                        .set(LeaveApproval::getUpdateTime, param.getUpdateTime())
                        .set(LeaveApproval::getUpdateUser, param.getUpdateUser())
                        .set(LeaveApproval::getStatus, ProcessStatus.APPROVED.getValue())
                        .eq(LeaveApproval::getId, param.getId()));
            }
        } else {
            leaveApprovalMapper.update(new LambdaUpdateWrapper<LeaveApproval>()
                    .set(LeaveApproval::getUpdateTime, param.getUpdateTime())
                    .set(LeaveApproval::getUpdateUser, param.getUpdateUser())
                    .set(LeaveApproval::getStatus, ProcessStatus.REJECTED.getValue())
                    .eq(LeaveApproval::getId, param.getId()));
        }
        taskService.complete(param.getTaskId().toString(), map);
        return Result.success();
    }

    /**
     * @Author: 放学后海堤日记
     * @Date: 2026/5/14 11:18
     * @Desc: 审批统计
     */
    @Override
    public Result<Map<String, Object>> approvalStatistics() {
        Long userId = UserContext.getCurrentUserId();

        LocalDate date = LocalDate.now();
        LocalDateTime dayStart = date.atTime(LocalTime.MIN);
        LocalDateTime dayEnd = date.atTime(LocalTime.MAX);
        LocalDateTime weekStart = date.minusDays(7).atTime(LocalTime.MIN);
        LocalDateTime weekEnd = date.atTime(LocalTime.MAX);
        LocalDateTime monthStart = date.minusMonths(1).atTime(LocalTime.MIN);
        LocalDateTime monthEnd = date.atTime(LocalTime.MAX);

        ApprovalStatisticsVO dayStats = leaveApprovalMapper.approvalStatistics(userId, dayStart, dayEnd);
        ApprovalStatisticsVO weekStats = leaveApprovalMapper.approvalStatistics(userId, weekStart, weekEnd);
        ApprovalStatisticsVO monthStats = leaveApprovalMapper.approvalStatistics(userId, monthStart, monthEnd);

        // 处理当前用户审批的申请数量
        Long dayProcessedStats = leaveApprovalMapper.statisticsProcessd(userId, dayStart, dayEnd);
        Long weekProcessedStats = leaveApprovalMapper.statisticsProcessd(userId, weekStart, weekEnd);
        Long monthProcessedStats = leaveApprovalMapper.statisticsProcessd(userId, monthStart, monthEnd);
        dayStats.setProcessedCount(dayProcessedStats);
        weekStats.setProcessedCount(weekProcessedStats);
        monthStats.setProcessedCount(monthProcessedStats);

        Map<String, Object> result = new HashMap<>();
        result.put("day", dayStats);
        result.put("week", weekStats);
        result.put("month", monthStats);

        return Result.success(result);
    }

    private String getProcessDefinitionId() {
        // 查询流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(PROCESS_DEFINITION_KEY)
                .singleResult();

        if (processDefinition == null) {
            // 加载流程定义
            repositoryService.createDeployment()
                    .addClasspathResource("process/请假审批.bpmn20.xml")
                    .key(PROCESS_DEFINITION_KEY)
                    .name("请假审批")
                    .deploy();

            processDefinition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionKey(PROCESS_DEFINITION_KEY)
                    .singleResult();
        }

        return processDefinition.getId();
    }
}
