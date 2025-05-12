package com.example.ai_teacher_system_back.util;

import org.springframework.stereotype.Component;

/**
 * AI服务工具类
 * 用于集成第三方AI服务，实现智能生成备课内容和优化备课内容等功能
 */
@Component
public class AIServiceUtil {

    /**
     * 智能生成备课内容
     * @param courseName 课程名称
     * @param chapter 章节
     * @return 生成的内容，包含教学目标和教学内容
     */
    public AIGeneratedContent generateLessonPlanContent(String courseName, String chapter) {
        // 这里应该调用实际的AI服务API，如百度AI、讯飞AI等
        // 目前使用模拟数据
        
        // 构建提示词
        String prompt = String.format(
                "请为课程《%s》的《%s》生成一份完整的备课内容，包括教学目标和教学内容。" +
                "教学目标应该包含认知、能力和情感态度价值观三个维度。" +
                "教学内容应该包括教学重点、教学难点、教学方法和教学过程等部分。",
                courseName, chapter);
        
        // 调用AI服务获取回复（这里模拟）
        return mockAIResponse(courseName, chapter, false);
    }
    
    /**
     * 优化备课内容
     * @param courseName 课程名称
     * @param chapter 章节
     * @param originalObjectives 原始教学目标
     * @param originalContent 原始教学内容
     * @return 优化后的内容
     */
    public AIGeneratedContent optimizeLessonPlanContent(String courseName, String chapter, 
                                                      String originalObjectives, String originalContent) {
        // 这里应该调用实际的AI服务API
        // 目前使用模拟数据
        
        // 构建提示词
        String prompt = String.format(
                "请优化以下课程《%s》的《%s》的备课内容：\n" +
                "原教学目标：%s\n" +
                "原教学内容：%s\n" +
                "请在保留原有内容的基础上，补充和完善教学目标和教学内容，使其更加全面和具体。",
                courseName, chapter, originalObjectives, originalContent);
        
        // 调用AI服务获取回复（这里模拟）
        return mockAIResponse(courseName, chapter, true);
    }
    
    /**
     * 模拟AI回复（实际项目中应替换为真实AI服务调用）
     */
    private AIGeneratedContent mockAIResponse(String courseName, String chapter, boolean isOptimize) {
        AIGeneratedContent content = new AIGeneratedContent();
        
        if (!isOptimize) {
            // 生成基础内容
            String objectives = "1. 理解" + courseName + "中" + chapter + "的基本概念\n" +
                    "2. 掌握相关知识点的应用\n" +
                    "3. 能够解决相关的基础问题";
            
            String lessonContent = "本节课主要讲解" + courseName + "中" + chapter + "的核心内容。\n\n" +
                    "一、教学重点：\n" +
                    "1. 基本概念的理解\n" +
                    "2. 典型例题分析\n\n" +
                    "二、教学难点：\n" +
                    "1. 知识点的融会贯通\n" +
                    "2. 解题思路的形成";
            
            content.setObjectives(objectives);
            content.setContent(lessonContent);
        } else {
            // 优化内容
            String objectives = "1. 理解" + courseName + "中" + chapter + "的基本概念\n" +
                    "2. 掌握相关知识点的应用\n" +
                    "3. 能够解决相关的基础问题\n" +
                    "4. 培养学生的创新思维和解决问题的能力";
            
            String lessonContent = "本节课主要讲解" + courseName + "中" + chapter + "的核心内容。\n\n" +
                    "一、教学重点：\n" +
                    "1. 基本概念的理解\n" +
                    "2. 典型例题分析\n\n" +
                    "二、教学难点：\n" +
                    "1. 知识点的融会贯通\n" +
                    "2. 解题思路的形成\n\n" +
                    "三、教学方法：\n" +
                    "1. 启发式教学\n" +
                    "2. 小组讨论\n" +
                    "3. 案例分析\n\n" +
                    "四、教学过程：\n" +
                    "1. 导入新课（5分钟）\n" +
                    "2. 讲解新知识（20分钟）\n" +
                    "3. 学生练习（15分钟）\n" +
                    "4. 总结归纳（5分钟）";
            
            content.setObjectives(objectives);
            content.setContent(lessonContent);
        }
        
        return content;
    }
    
    /**
     * AI生成内容的数据结构
     */
    public static class AIGeneratedContent {
        private String objectives; // 教学目标
        private String content;    // 教学内容
        
        public String getObjectives() {
            return objectives;
        }
        
        public void setObjectives(String objectives) {
            this.objectives = objectives;
        }
        
        public String getContent() {
            return content;
        }
        
        public void setContent(String content) {
            this.content = content;
        }
    }
}