package com.example.ai_teacher_system_back.service.impl;

import com.example.ai_teacher_system_back.dto.CourseRequest;
import com.example.ai_teacher_system_back.entity.Course;
import com.example.ai_teacher_system_back.exception.BusinessException;
import com.example.ai_teacher_system_back.mapper.CourseMapper;
import com.example.ai_teacher_system_back.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 课程服务实现类
 */
@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public Course saveCourse(CourseRequest request, Integer userId) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new BusinessException("课程名称不能为空");
        }

        Course course = new Course();
        course.setName(request.getName());
        course.setDescription(request.getDescription());
        course.setSubject(request.getSubject());
        course.setGrade(request.getGrade());
        course.setCreatorId(userId);
        course.setCreateTime(new Date());
        course.setUpdateTime(new Date());

        courseMapper.insert(course);
        return course;
    }

    @Override
    public Course updateCourse(Integer id, CourseRequest request) {
        Course existingCourse = courseMapper.selectById(id);
        if (existingCourse == null) {
            throw new BusinessException("课程不存在");
        }

        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            existingCourse.setName(request.getName());
        }
        if (request.getDescription() != null) {
            existingCourse.setDescription(request.getDescription());
        }
        if (request.getSubject() != null) {
            existingCourse.setSubject(request.getSubject());
        }
        if (request.getGrade() != null) {
            existingCourse.setGrade(request.getGrade());
        }
        existingCourse.setUpdateTime(new Date());

        courseMapper.update(existingCourse);
        return existingCourse;
    }

    @Override
    public Course getCourseById(Integer id) {
        Course course = courseMapper.selectById(id);
        if (course == null) {
            throw new BusinessException("课程不存在");
        }
        return course;
    }

    @Override
    public List<Course> getCoursesByUserId(Integer userId) {
        return courseMapper.selectByCreatorId(userId);
    }

    @Override
    public List<Course> getCoursesBySubject(String subject) {
        return courseMapper.selectBySubject(subject);
    }

    @Override
    public List<Course> getCoursesByGrade(String grade) {
        return courseMapper.selectByGrade(grade);
    }

    @Override
    public boolean deleteCourse(Integer id) {
        Course course = courseMapper.selectById(id);
        if (course == null) {
            throw new BusinessException("课程不存在");
        }
        return courseMapper.deleteById(id) > 0;
    }
}