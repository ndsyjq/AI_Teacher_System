package com.example.ai_teacher_system_back.service;

import com.example.ai_teacher_system_back.dto.LessonPlanRequest;
import com.example.ai_teacher_system_back.entity.LessonPlan;

import java.util.List;

/**
 * 备课计划服务接口
 */
public interface LessonPlanService {
    
    /**
     * 保存备课计划
     * @param request 备课计划请求对象
     * @param userId 用户ID
     * @param courseId 课程ID
     * @return 保存后的备课计划对象
     */
    LessonPlan saveLessonPlan(LessonPlanRequest request, Integer userId, Integer courseId);
    
    /**
     * AI智能生成备课内容
     * @param name 课程名称
     * @param chapter 章节
     * @return 生成的备课内容
     */
    LessonPlan generateLessonPlanContent(String name, String chapter);
    
    /**
     * 编辑备课计划
     * @param id 备课计划ID
     * @param request 备课计划请求对象
     * @return 更新后的备课计划对象
     */
    LessonPlan updateLessonPlan(Integer id, LessonPlanRequest request);
    
    /**
     * AI优化备课内容
     * @param id 备课计划ID
     * @param request 备课计划请求对象
     * @return 优化后的备课内容
     */
    LessonPlan optimizeLessonPlanContent(Integer id, LessonPlanRequest request);
    
    /**
     * 查看备课详情
     * @param id 备课计划ID
     * @return 备课计划对象
     */
    LessonPlan getLessonPlanById(Integer id);
    
    /**
     * 根据课程ID查询备课计划列表
     * @param courseId 课程ID
     * @return 备课计划列表
     */
    List<LessonPlan> getLessonPlansByCourseId(Integer courseId);
    
    /**
     * 根据用户ID查询备课计划列表
     * @param userId 用户ID
     * @return 备课计划列表
     */
    List<LessonPlan> getLessonPlansByUserId(Integer userId);
    
    /**
     * 删除备课计划
     * @param id 备课计划ID
     * @return 是否删除成功
     */
    boolean deleteLessonPlan(Integer id);
}