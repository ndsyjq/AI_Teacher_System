package com.example.ai_teacher_system_back.mapper;

import com.example.ai_teacher_system_back.entity.LessonPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 备课计划Mapper接口
 */
@Mapper
public interface LessonPlanMapper {
    
    /**
     * 保存备课计划
     * @param lessonPlan 备课计划对象
     * @return 影响行数
     */
    int insert(LessonPlan lessonPlan);
    
    /**
     * 更新备课计划
     * @param lessonPlan 备课计划对象
     * @return 影响行数
     */
    int update(LessonPlan lessonPlan);
    
    /**
     * 根据ID查询备课计划
     * @param id 备课计划ID
     * @return 备课计划对象
     */
    LessonPlan selectById(@Param("id") Integer id);
    
    /**
     * 根据课程ID查询备课计划列表
     * @param courseId 课程ID
     * @return 备课计划列表
     */
    List<LessonPlan> selectByCourseId(@Param("courseId") Integer courseId);
    
    /**
     * 根据创建者ID查询备课计划列表
     * @param creatorId 创建者ID
     * @return 备课计划列表
     */
    List<LessonPlan> selectByCreatorId(@Param("creatorId") Integer creatorId);
    
    /**
     * 删除备课计划
     * @param id 备课计划ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Integer id);
}