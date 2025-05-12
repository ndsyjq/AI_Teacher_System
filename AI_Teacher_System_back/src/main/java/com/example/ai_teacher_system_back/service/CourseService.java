package com.example.ai_teacher_system_back.service;

import com.example.ai_teacher_system_back.dto.CourseRequest;
import com.example.ai_teacher_system_back.entity.Course;

import java.util.List;

/**
 * 课程服务接口
 */
public interface CourseService {
    
    /**
     * 保存课程
     * @param request 课程请求对象
     * @param userId 用户ID
     * @return 保存后的课程对象
     */
    Course saveCourse(CourseRequest request, Integer userId);
    
    /**
     * 更新课程
     * @param id 课程ID
     * @param request 课程请求对象
     * @return 更新后的课程对象
     */
    Course updateCourse(Integer id, CourseRequest request);
    
    /**
     * 查看课程详情
     * @param id 课程ID
     * @return 课程对象
     */
    Course getCourseById(Integer id);
    
    /**
     * 根据用户ID查询课程列表
     * @param userId 用户ID
     * @return 课程列表
     */
    List<Course> getCoursesByUserId(Integer userId);
    
    /**
     * 根据学科查询课程列表
     * @param subject 学科
     * @return 课程列表
     */
    List<Course> getCoursesBySubject(String subject);
    
    /**
     * 根据年级查询课程列表
     * @param grade 年级
     * @return 课程列表
     */
    List<Course> getCoursesByGrade(String grade);
    
    /**
     * 删除课程
     * @param id 课程ID
     * @return 是否删除成功
     */
    boolean deleteCourse(Integer id);
}