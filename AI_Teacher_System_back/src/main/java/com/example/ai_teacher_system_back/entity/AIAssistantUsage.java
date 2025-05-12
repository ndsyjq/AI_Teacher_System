package com.example.ai_teacher_system_back.entity;

import java.util.Date;

/**
 * AI助手使用记录实体类
 */
public class AIAssistantUsage {
    private Integer id;
    private Integer userId;
    private String queryContent;
    private String responseContent;
    private Date usageTime;

    public AIAssistantUsage() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getQueryContent() {
        return queryContent;
    }

    public void setQueryContent(String queryContent) {
        this.queryContent = queryContent;
    }

    public String getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(String responseContent) {
        this.responseContent = responseContent;
    }

    public Date getUsageTime() {
        return usageTime;
    }

    public void setUsageTime(Date usageTime) {
        this.usageTime = usageTime;
    }

    @Override
    public String toString() {
        return "AIAssistantUsage{" +
                "id=" + id +
                ", userId=" + userId +
                ", queryContent='" + queryContent + '\'' +
                ", responseContent='" + responseContent + '\'' +
                ", usageTime=" + usageTime +
                '}';
    }
}