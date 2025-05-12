package com.example.ai_teacher_system_back.mapper;

import com.example.ai_teacher_system_back.entity.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 课程Mapper接口
 */
@Mapper
public interface CourseMapper {
    
    /**
     * 保存课程
     * @param course 课程对象
     * @return 影响行数
     */
    int insert(Course course);
    
    /**
     * 更新课程
     * @param course 课程对象
     * @return 影响行数
     */
    int update(Course course);
    
    /**
     * 根据ID查询课程
     * @param id 课程ID
     * @return 课程对象
     */
    Course selectById(@Param("id") Integer id);
    
    /**
     * 根据创建者ID查询课程列表
     * @param creatorId 创建者ID
     * @return 课程列表
     */
    List<Course> selectByCreatorId(@Param("creatorId") Integer creatorId);
    
    /**
     * 根据学科查询课程列表
     * @param subject 学科
     * @return 课程列表
     */
    List<Course> selectBySubject(@Param("subject") String subject);
    
    /**
     * 根据年级查询课程列表
     * @param grade 年级
     * @return 课程列表
     */
    List<Course> selectByGrade(@Param("grade") String grade);
    
    /**
     * 根据ID删除课程
     * @param id 课程ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Integer id);
}