package com.example.ai_teacher_system_back.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ai_teacher_system_back.common.Result;
import com.example.ai_teacher_system_back.dto.LessonPlanRequest;
import com.example.ai_teacher_system_back.entity.LessonPlan;
import com.example.ai_teacher_system_back.service.LessonPlanService;

/**
 * 备课计划控制器
 */
@RestController
@RequestMapping("/api/lesson-plans")
public class LessonPlanController {

    @Autowired
    private LessonPlanService lessonPlanService;

    /**
     * 保存备课计划
     * @param request 备课计划请求对象
     * @return 结果
     */
    @PostMapping
    public Result saveLessonPlan(@RequestBody LessonPlanRequest request) {
        // 这里应该从JWT中获取用户ID，这里简单模拟
        Integer userId = 1;
        Integer courseId = 1; // 实际应该从请求中获取
        
        LessonPlan lessonPlan = lessonPlanService.saveLessonPlan(request, userId, courseId);
        
        return Result.success(lessonPlan, "备课计划保存成功");
    }

    /**
     * AI智能生成备课内容
     * @param request 包含课程名称和章节的请求对象
     * @return 结果
     */
    @PostMapping("/generate")
    public Result generateLessonPlanContent(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        String chapter = request.get("chapter");
        
        LessonPlan generatedContent = lessonPlanService.generateLessonPlanContent(name, chapter);
        
        Map<String, String> result = new HashMap<>();
        result.put("objectives", generatedContent.getObjectives());
        result.put("content", generatedContent.getContent());
        
        return Result.success(result, "AI 生成内容成功");
    }

    /**
     * 编辑备课计划
     * @param id 备课计划ID
     * @param request 备课计划请求对象
     * @return 结果
     */
    @PutMapping("/{id}")
    public Result updateLessonPlan(@PathVariable Integer id, @RequestBody LessonPlanRequest request) {
        lessonPlanService.updateLessonPlan(id, request);
        
        return Result.success("备课计划编辑成功");
    }

    /**
     * AI优化备课内容
     * @param id 备课计划ID
     * @param request 备课计划请求对象
     * @return 结果
     */
    @PostMapping("/{id}/optimize")
    public Result optimizeLessonPlanContent(@PathVariable Integer id, @RequestBody LessonPlanRequest request) {
        LessonPlan optimizedPlan = lessonPlanService.optimizeLessonPlanContent(id, request);
        
        Map<String, String> result = new HashMap<>();
        result.put("objectives", optimizedPlan.getObjectives());
        result.put("content", optimizedPlan.getContent());
        
        return Result.success(result, "AI 优化内容成功");
    }

    /**
     * 查看备课详情
     * @param id 备课计划ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public Result getLessonPlanDetail(@PathVariable Integer id) {
        LessonPlan lessonPlan = lessonPlanService.getLessonPlanById(id);
        
        return Result.success(lessonPlan, "备课详情获取成功");
    }

    /**
     * 根据课程ID查询备课计划列表
     * @param courseId 课程ID
     * @return 结果
     */
    @GetMapping("/course/{courseId}")
    public Result getLessonPlansByCourseId(@PathVariable Integer courseId) {
        return Result.success(lessonPlanService.getLessonPlansByCourseId(courseId));
    }

    /**
     * 删除备课计划
     * @param id 备课计划ID
     * @return 结果
     */
    @DeleteMapping("/{id}")
    public Result deleteLessonPlan(@PathVariable Integer id) {
        boolean success = lessonPlanService.deleteLessonPlan(id);
        
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }
}