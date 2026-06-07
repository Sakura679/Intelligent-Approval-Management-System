package com.iams.service;

import com.iams.params.AiParam;
import com.iams.utils.Result;

import java.util.List;
import java.util.Map;

/**
 * @Author: 放学后海堤日记
 * @Date: 2026/5/22 10:56
 * @Desc:
 */
public interface AiService {
    Result<String> toChat(AiParam param);

    Result<List<Map<String, Object>>> getHistory(AiParam param);

    Result<String> clearHistory(AiParam param);
}
