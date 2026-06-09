package com.iams.service;

import org.flowable.task.api.Task;

import javax.servlet.http.HttpServletResponse;


/**
 * @Author: 放学后海堤日记
 * @Date: 2025/3/28 16:38
 * @Desc:
 */
public interface HighlightService {
    void getHighlight(Task task, HttpServletResponse response);
}
