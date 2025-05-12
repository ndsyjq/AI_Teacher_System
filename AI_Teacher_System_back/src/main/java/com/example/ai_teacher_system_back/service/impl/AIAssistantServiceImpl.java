package com.example.ai_teacher_system_back.service.impl;

import com.example.ai_teacher_system_back.entity.AIAssistantUsage;
import com.example.ai_teacher_system_back.exception.BusinessException;
import com.example.ai_teacher_system_back.mapper.AIAssistantUsageMapper;
import com.example.ai_teacher_system_back.service.AIAssistantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.URL;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AI助手服务实现类
 * 实现与AI服务的交互，处理用户消息并获取AI回复
 */
@Service
public class AIAssistantServiceImpl implements AIAssistantService {
    private static final Logger logger = LoggerFactory.getLogger(AIAssistantServiceImpl.class);
    
    // AI服务API地址
    private static final String AI_API_URL = "http://172.18.68.8/v1/chat-messages";
    
    // API认证令牌
    private static final String TOKEN = "app-9owkbQvP2dfPsmtMelQA64fF";
    
    @Autowired
    private AIAssistantUsageMapper aiAssistantUsageMapper;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String sendMessage(String content, Integer userId) {
        if (content == null || content.trim().isEmpty()) {
            throw new BusinessException("消息内容不能为空");
        }
        
        logger.debug("用户[{}]发送消息: {}", userId, content);
        
        try {
            // 调用AI服务获取回复
            String aiResponse = generateAIResponse(content);
            
            // 保存使用记录
            saveUsageRecord(userId, content, aiResponse);
            
            logger.debug("AI回复: {}", aiResponse);
            return aiResponse;
        } catch (Exception e) {
            logger.error("AI服务调用失败", e);
            throw new BusinessException("AI服务暂时不可用，请稍后再试: " + e.getMessage());
        }
    }

    @Override
    public AIAssistantUsage saveUsageRecord(Integer userId, String queryContent, String responseContent) {
        AIAssistantUsage usage = new AIAssistantUsage();
        usage.setUserId(userId);
        usage.setQueryContent(queryContent);
        usage.setResponseContent(responseContent);
        usage.setUsageTime(new Date());
        
        aiAssistantUsageMapper.insert(usage);
        return usage;
    }

    @Override
    public List<AIAssistantUsage> getRecentUsageRecords(Integer userId, Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10; // 默认获取10条
        }
        return aiAssistantUsageMapper.selectRecentByUserId(userId, limit);
    }

    @Override
    public int countUsageByUserId(Integer userId) {
        return aiAssistantUsageMapper.countByUserId(userId);
    }
    
    /**
     * 生成AI回复内容
     * @param userMessage 用户消息
     * @return AI回复内容
     * @throws IOException 如果API调用失败
     */
    private String generateAIResponse(String userMessage) throws IOException {
        // 构建请求体
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("inputs", new HashMap<>());
        requestMap.put("query", userMessage);
        requestMap.put("stream", "False");
        requestMap.put("conversation_id", "");
        requestMap.put("user", "user");
        
        String requestBody = objectMapper.writeValueAsString(requestMap);
        logger.debug("发送AI请求: {}", requestBody);

        // 创建HTTP连接
        URL url = new URL(AI_API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + TOKEN);
        connection.setConnectTimeout(10000); // 连接超时10秒
        connection.setReadTimeout(30000);    // 读取超时30秒
        connection.setDoOutput(true);

        // 发送请求
        try (OutputStream os = connection.getOutputStream()) {
            byte[] inputBytes = requestBody.getBytes(StandardCharsets.UTF_8);
            os.write(inputBytes, 0, inputBytes.length);
        }

        // 处理响应
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // 读取响应内容
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                
                // 解析JSON响应
                String responseBody = response.toString();
                logger.debug("AI响应: {}", responseBody);
                
                Map<String, Object> jsonMap = objectMapper.readValue(responseBody, Map.class);
                String answer = (String) jsonMap.get("answer");
                
                if (answer == null || answer.trim().isEmpty()) {
                    logger.warn("AI返回空回复");
                    return "抱歉，AI助手暂时无法提供有效回复，请稍后再试。";
                }
                
                return answer;
            }
        } else {
            // 处理错误响应
            try (BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                StringBuilder errorResponse = new StringBuilder();
                String errorLine;
                while ((errorLine = errorReader.readLine()) != null) {
                    errorResponse.append(errorLine);
                }
                logger.error("AI服务错误响应: {}", errorResponse.toString());
            } catch (Exception e) {
                logger.error("读取错误响应失败", e);
            }
            
            throw new IOException("AI服务请求失败，HTTP状态码: " + responseCode);
        }
    }
    
    /**
     * 处理AI回复内容，进行必要的后处理
     * @param rawResponse 原始AI回复
     * @return 处理后的回复
     */
    private String processAIResponse(String rawResponse) {
        if (rawResponse == null || rawResponse.trim().isEmpty()) {
            return "抱歉，AI助手暂时无法提供有效回复，请稍后再试。";
        }
        
        // 这里可以添加对AI回复的后处理逻辑，如敏感词过滤、格式调整等
        return rawResponse;
    }
}