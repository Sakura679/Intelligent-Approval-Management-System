package com.iams.controller;

import com.iams.params.AiParam;
import com.iams.service.AiService;
import com.iams.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author: 放学后海堤日记
 * @Date: 2026/5/22 10:50
 * @Desc:
 */
@Api(tags = "AI")
@RestController
@RequestMapping("/api/v1/ai")
public class AiController {

    @Autowired
    private AiService aiService;

    @ApiOperation("ai聊天")
    @PostMapping("/chat")
    public Result<String> toChat(@RequestBody AiParam param) {
        return aiService.toChat(param);
    }

    @ApiOperation("获取历史")
    @PostMapping("/chat/history")
    public Result<List<Map<String, Object>>> getHistory(@RequestBody AiParam param) {
        return aiService.getHistory(param);
    }

    @ApiOperation("清空历史")
    @PostMapping("/chat/clear")
    public Result<String> clearHistory(@RequestBody AiParam param) {
        return aiService.clearHistory(param);
    }
}
