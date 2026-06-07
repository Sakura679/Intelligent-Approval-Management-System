package com.iams.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.iams.context.UserContext;
import com.iams.params.AiParam;
import com.iams.service.AiService;
import com.iams.utils.Result;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * @Author: 放学后海堤日记
 * @Date: 2026/5/22 10:56
 * @Desc:
 */
@Service
public class AiServiceImpl implements AiService {

    @Autowired
    private RestTemplate restTemplate;


    private static final String BASE_URL = "http://127.0.0.1:8000/api/v1";

    /**
     * @Author: 放学后海堤日记
     * @Date: 2026/5/22 10:56
     * @Desc: 聊天接口实现
     */
    @Override
    public Result<String> toChat(AiParam param) {
        param.setThreadId("thread_" + UserContext.getCurrentUserId());

        HttpEntity<AiParam> httpEntity = setRequestBody(param);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                BASE_URL + "/agent/chat",
                httpEntity,
                String.class
        );

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return Result.success(responseEntity.getBody());
        } else {
            return Result.error("请求失败");
        }
    }

    /**
     * @Author: 放学后海堤日记
     * @Date: 2026/5/22 10:56
     * @Desc: 获取历史接口实现
     */
    @Override
    public Result<List<Map<String, Object>>> getHistory(AiParam param) {
        param.setThreadId("thread_" + UserContext.getCurrentUserId());

        HttpEntity<AiParam> httpEntity = setRequestBody(param);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                BASE_URL + "/agent/chat/history",
                httpEntity,
                String.class
        );

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            List<Map<String, Object>> parseObject = JSONObject.parseObject(responseEntity.getBody(), List.class);
            return Result.success(parseObject);
        } else {
            return Result.error("请求失败");
        }
    }

    /**
     * @Author: 放学后海堤日记
     * @Date: 2026/5/22 10:56
     * @Desc: 清空历史接口实现
     */
    @Override
    public Result<String> clearHistory(AiParam param) {
        param.setThreadId("thread_" + UserContext.getCurrentUserId());

        HttpEntity<AiParam> httpEntity = setRequestBody(param);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                BASE_URL + "/agent/chat/clear",
                httpEntity,
                String.class
        );

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return Result.success(responseEntity.getBody());
        } else {
            return Result.error("请求失败");
        }
    }

    @NotNull
    private <T> HttpEntity<T> setRequestBody(T param) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + UserContext.getCurrentToken());

        return new HttpEntity<>(param, headers);
    }
}
