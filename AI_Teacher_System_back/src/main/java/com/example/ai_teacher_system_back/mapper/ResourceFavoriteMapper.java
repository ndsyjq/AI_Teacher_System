package com.example.ai_teacher_system_back.mapper;

import com.example.ai_teacher_system_back.entity.ResourceFavorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 资源收藏Mapper接口
 */
@Mapper
public interface ResourceFavoriteMapper {
    
    /**
     * 添加收藏
     * @param resourceFavorite 资源收藏对象
     * @return 影响行数
     */
    int insert(ResourceFavorite resourceFavorite);
    
    /**
     * 根据ID查询收藏
     * @param id 收藏ID
     * @return 资源收藏对象
     */
    ResourceFavorite selectById(@Param("id") Integer id);
    
    /**
     * 根据资源ID和用户ID查询收藏
     * @param resourceId 资源ID
     * @param userId 用户ID
     * @return 资源收藏对象
     */
    ResourceFavorite selectByResourceIdAndUserId(@Param("resourceId") Integer resourceId, @Param("userId") Integer userId);
    
    /**
     * 根据用户ID查询收藏列表
     * @param userId 用户ID
     * @return 资源收藏列表
     */
    List<ResourceFavorite> selectByUserId(@Param("userId") Integer userId);
    
    /**
     * 根据资源ID查询收藏列表
     * @param resourceId 资源ID
     * @return 资源收藏列表
     */
    List<ResourceFavorite> selectByResourceId(@Param("resourceId") Integer resourceId);
    
    /**
     * 根据ID删除收藏
     * @param id 收藏ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Integer id);
    
    /**
     * 根据资源ID和用户ID删除收藏
     * @param resourceId 资源ID
     * @param userId 用户ID
     * @return 影响行数
     */
    int deleteByResourceIdAndUserId(@Param("resourceId") Integer resourceId, @Param("userId") Integer userId);
}