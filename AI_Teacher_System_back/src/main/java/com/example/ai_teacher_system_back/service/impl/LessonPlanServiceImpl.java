package com.example.ai_teacher_system_back.service.impl;

import com.example.ai_teacher_system_back.dto.LessonPlanRequest;
import com.example.ai_teacher_system_back.entity.LessonPlan;
import com.example.ai_teacher_system_back.exception.BusinessException;
import com.example.ai_teacher_system_back.mapper.LessonPlanMapper;
import com.example.ai_teacher_system_back.service.LessonPlanService;
import com.example.ai_teacher_system_back.util.AIServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 备课计划服务实现类
 */
@Service
public class LessonPlanServiceImpl implements LessonPlanService {

    @Autowired
    private LessonPlanMapper lessonPlanMapper;
    
    @Autowired
    private AIServiceUtil aiServiceUtil;

    @Override
    public LessonPlan saveLessonPlan(LessonPlanRequest request, Integer userId, Integer courseId) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new BusinessException("课程名称不能为空");
        }
        if (request.getChapter() == null || request.getChapter().trim().isEmpty()) {
            throw new BusinessException("章节不能为空");
        }

        LessonPlan lessonPlan = new LessonPlan();
        lessonPlan.setName(request.getName());
        lessonPlan.setChapter(request.getChapter());
        lessonPlan.setObjectives(request.getObjectives());
        lessonPlan.setContent(request.getContent());
        lessonPlan.setStatus(request.getStatus() != null ? request.getStatus() : "未开始");
        lessonPlan.setCourseId(courseId);
        lessonPlan.setCreatorId(userId);
        lessonPlan.setIsAiGenerated(false);
        lessonPlan.setPlanDate(new Date());
        lessonPlan.setCreateTime(new Date());
        lessonPlan.setUpdateTime(new Date());

        lessonPlanMapper.insert(lessonPlan);
        return lessonPlan;
    }

    @Override
    public LessonPlan generateLessonPlanContent(String name, String chapter) {
        if (name == null || name.trim().isEmpty()) {
            throw new BusinessException("课程名称不能为空");
        }
        if (chapter == null || chapter.trim().isEmpty()) {
            throw new BusinessException("章节不能为空");
        }

        // 调用AI服务工具生成内容
        AIServiceUtil.AIGeneratedContent generatedContent = aiServiceUtil.generateLessonPlanContent(name, chapter);
        
        LessonPlan lessonPlan = new LessonPlan();
        lessonPlan.setName(name);
        lessonPlan.setChapter(chapter);
        lessonPlan.setObjectives(generatedContent.getObjectives());
        lessonPlan.setContent(generatedContent.getContent());
        lessonPlan.setIsAiGenerated(true);
        
        return lessonPlan;
    }

    @Override
    public LessonPlan updateLessonPlan(Integer id, LessonPlanRequest request) {
        LessonPlan existingPlan = lessonPlanMapper.selectById(id);
        if (existingPlan == null) {
            throw new BusinessException("备课计划不存在");
        }

        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            existingPlan.setName(request.getName());
        }
        if (request.getChapter() != null && !request.getChapter().trim().isEmpty()) {
            existingPlan.setChapter(request.getChapter());
        }
        if (request.getObjectives() != null) {
            existingPlan.setObjectives(request.getObjectives());
        }
        if (request.getContent() != null) {
            existingPlan.setContent(request.getContent());
        }
        if (request.getStatus() != null) {
            existingPlan.setStatus(request.getStatus());
        }
        existingPlan.setUpdateTime(new Date());

        lessonPlanMapper.update(existingPlan);
        return existingPlan;
    }

    @Override
    public LessonPlan optimizeLessonPlanContent(Integer id, LessonPlanRequest request) {
        LessonPlan existingPlan = lessonPlanMapper.selectById(id);
        if (existingPlan == null) {
            throw new BusinessException("备课计划不存在");
        }

        // 调用AI服务工具优化内容
        AIServiceUtil.AIGeneratedContent optimizedContent = aiServiceUtil.optimizeLessonPlanContent(
                existingPlan.getName(),
                existingPlan.getChapter(),
                existingPlan.getObjectives(),
                existingPlan.getContent()
        );
        
        existingPlan.setObjectives(optimizedContent.getObjectives());
        existingPlan.setContent(optimizedContent.getContent());
        existingPlan.setUpdateTime(new Date());
        
        lessonPlanMapper.update(existingPlan);
        return existingPlan;
    }

    @Override
    public LessonPlan getLessonPlanById(Integer id) {
        LessonPlan lessonPlan = lessonPlanMapper.selectById(id);
        if (lessonPlan == null) {
            throw new BusinessException("备课计划不存在");
        }
        return lessonPlan;
    }

    @Override
    public List<LessonPlan> getLessonPlansByCourseId(Integer courseId) {
        return lessonPlanMapper.selectByCourseId(courseId);
    }

    @Override
    public List<LessonPlan> getLessonPlansByUserId(Integer userId) {
        return lessonPlanMapper.selectByCreatorId(userId);
    }

    @Override
    public boolean deleteLessonPlan(Integer id) {
        LessonPlan lessonPlan = lessonPlanMapper.selectById(id);
        if (lessonPlan == null) {
            throw new BusinessException("备课计划不存在");
        }
        return lessonPlanMapper.deleteById(id) > 0;
    }
}