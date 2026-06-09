package com.iams.service.impl;

import com.iams.service.HighlightService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ActivityInstance;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @Author: 放学后海堤日记
 * @Date: 2025/3/28 16:38
 * @Desc:
 */
@Slf4j
@Service
public class HighlightServiceImpl implements HighlightService {
    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @Override
    public void getHighlight(Task task, HttpServletResponse response) {
        if (task == null) return;

        try {
            InputStream inputStream = processRecordImage(task.getProcessInstanceId());

            // 本地存储
            //文件名处理
//            String fileName = "src/main/resources/static/" + task.getProcessInstanceId() + ".png";
//            FileOutputStream outputStream = new FileOutputStream(fileName);

            // 不存储，返回给前端
            response.setContentType("image/png");
            response.setHeader("Content-Disposition", "inline; filename=process_trace.png");
            ServletOutputStream outputStream = response.getOutputStream();

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
//                outputStream.flush();
            }

            inputStream.close();
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 流程记录
     */
    private InputStream processRecordImage(String processInstanceId) throws IOException {
        HistoricProcessInstance hiProcInst =
                Optional.ofNullable(historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult())
                        .orElseThrow(() -> new IOException("流程实例没有找到"));
        List<String> highLightedActivities = taskService.createTaskQuery().processInstanceId(processInstanceId).list().stream()
                .map(task -> runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult().getActivityId()).collect(Collectors.toList());

        List<String> flowIds;
        if (!highLightedActivities.isEmpty()) {
            flowIds = runtimeService.createActivityInstanceQuery()
                    .orderByActivityInstanceStartTime().asc()
                    .orderByActivityInstanceEndTime().asc()
                    .processInstanceId(processInstanceId).list().stream()
                    .filter(hiActInst -> "sequenceFlow".equals(hiActInst.getActivityType()))
                    .map(ActivityInstance::getActivityId)
                    .collect(Collectors.toCollection(CopyOnWriteArrayList::new));
        } else {
            flowIds = historyService.createHistoricActivityInstanceQuery().orderByHistoricActivityInstanceStartTime().asc()
                    .orderByHistoricActivityInstanceEndTime().asc()
                    .processInstanceId(processInstanceId).list().stream()
                    .filter(hiActInst -> "sequenceFlow".equals(hiActInst.getActivityType()))
                    .map(HistoricActivityInstance::getActivityId)
                    .collect(Collectors.toCollection(CopyOnWriteArrayList::new));
        }

        List<String> highLightedFlows = new CopyOnWriteArrayList<>();

        flowIds.forEach(id -> {
            if (highLightedFlows.contains(id)) {
                int index = highLightedFlows.indexOf(id);
                highLightedFlows.removeIf(id1 -> highLightedFlows.indexOf(id1) > index);
            } else {
                highLightedFlows.add(id);
            }
        });

        return new DefaultProcessDiagramGenerator().generateDiagram(
                repositoryService.getBpmnModel(hiProcInst.getProcessDefinitionId()),
                "PNG",
                highLightedActivities,
                highLightedFlows,
                "宋体",
                "宋体",
                "宋体",
                null,
                1.0,
                true);
    }
}
