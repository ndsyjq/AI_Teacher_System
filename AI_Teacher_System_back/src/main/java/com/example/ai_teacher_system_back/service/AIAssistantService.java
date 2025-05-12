package com.example.ai_teacher_system_back.service;

import com.example.ai_teacher_system_back.entity.AIAssistantUsage;

import java.util.List;

/**
 * AI助手服务接口
 */
public interface AIAssistantService {
    
    /**
     * 发送用户消息并获取AI回复
     * @param content 用户消息内容
     * @param userId 用户ID
     * @return AI回复内容
     */
    String sendMessage(String content, Integer userId);
    
    /**
     * 保存AI助手使用记录
     * @param userId 用户ID
     * @param queryContent 查询内容
     * @param responseContent 响应内容
     * @return 保存后的记录对象
     */
    AIAssistantUsage saveUsageRecord(Integer userId, String queryContent, String responseContent);
    
    /**
     * 获取用户最近的AI助手使用记录
     * @param userId 用户ID
     * @param limit 限制条数
     * @return AI助手使用记录列表
     */
    List<AIAssistantUsage> getRecentUsageRecords(Integer userId, Integer limit);
    
    /**
     * 统计用户AI助手使用次数
     * @param userId 用户ID
     * @return 使用次数
     */
    int countUsageByUserId(Integer userId);
}