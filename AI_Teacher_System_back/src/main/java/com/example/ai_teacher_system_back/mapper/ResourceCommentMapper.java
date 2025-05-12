package com.example.ai_teacher_system_back.mapper;

import com.example.ai_teacher_system_back.entity.ResourceComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 资源评论Mapper接口
 */
@Mapper
public interface ResourceCommentMapper {
    
    /**
     * 添加评论
     * @param resourceComment 资源评论对象
     * @return 影响行数
     */
    int insert(ResourceComment resourceComment);
    
    /**
     * 更新评论
     * @param resourceComment 资源评论对象
     * @return 影响行数
     */
    int update(ResourceComment resourceComment);
    
    /**
     * 根据ID查询评论
     * @param id 评论ID
     * @return 资源评论对象
     */
    ResourceComment selectById(@Param("id") Integer id);
    
    /**
     * 根据资源ID查询评论列表
     * @param resourceId 资源ID
     * @return 资源评论列表
     */
    List<ResourceComment> selectByResourceId(@Param("resourceId") Integer resourceId);
    
    /**
     * 根据用户ID查询评论列表
     * @param userId 用户ID
     * @return 资源评论列表
     */
    List<ResourceComment> selectByUserId(@Param("userId") Integer userId);
    
    /**
     * 根据ID删除评论
     * @param id 评论ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Integer id);
    
    /**
     * 根据资源ID删除评论
     * @param resourceId 资源ID
     * @return 影响行数
     */
    int deleteByResourceId(@Param("resourceId") Integer resourceId);
}