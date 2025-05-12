package com.example.ai_teacher_system_back.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ai_teacher_system_back.common.Result;
import com.example.ai_teacher_system_back.service.AIAssistantService;

/**
 * AI助手控制器
 */
@RestController
@RequestMapping("/api/ai-assistant")
public class AIAssistantController {

    @Autowired
    private AIAssistantService aiAssistantService;

    /**
     * 发送用户消息并获取AI回复
     * @param request 包含用户消息内容的请求对象
     * @return 结果
     */
    @PostMapping("/send-message")
    public Result sendMessage(@RequestBody Map<String, String> request) {
        // 从请求中获取消息内容
        String content = request.get("content");
        
        // 这里应该从JWT中获取用户ID，这里简单模拟
        Integer userId = 1;
        
        // 调用服务获取AI回复
        String aiResponse = aiAssistantService.sendMessage(content, userId);
        
        // 构建响应数据
        Map<String, String> responseData = new HashMap<>();
        responseData.put("type", "ai");
        responseData.put("content", aiResponse);
        
        return Result.success(responseData, "操作成功");
    }

    /**
     * 获取用户最近的AI助手使用记录
     * @param limit 限制条数
     * @return 结果
     */
    @GetMapping("/history")
    public Result getUsageHistory(@RequestParam(required = false, defaultValue = "10") Integer limit) {
        // 这里应该从JWT中获取用户ID，这里简单模拟
        Integer userId = 1;
        
        return Result.success(aiAssistantService.getRecentUsageRecords(userId, limit));
    }

    /**
     * 获取用户AI助手使用统计
     * @return 结果
     */
    @GetMapping("/stats")
    public Result getUsageStats() {
        // 这里应该从JWT中获取用户ID，这里简单模拟
        Integer userId = 1;
        
        int count = aiAssistantService.countUsageByUserId(userId);
        
        Map<String, Integer> stats = new HashMap<>();
        stats.put("totalUsage", count);
        
        return Result.success(stats);
    }
}