package com.example.ai_teacher_system_back.service;

import com.example.ai_teacher_system_back.entity.ResourceFavorite;

import java.util.List;

/**
 * 资源收藏服务接口
 */
public interface ResourceFavoriteService {
    
    /**
     * 添加收藏
     * @param resourceFavorite 资源收藏对象
     * @return 创建的资源收藏对象
     */
    ResourceFavorite addFavorite(ResourceFavorite resourceFavorite);
    
    /**
     * 根据ID获取收藏
     * @param id 收藏ID
     * @return 资源收藏对象
     */
    ResourceFavorite getFavoriteById(Integer id);
    
    /**
     * 根据资源ID和用户ID获取收藏
     * @param resourceId 资源ID
     * @param userId 用户ID
     * @return 资源收藏对象
     */
    ResourceFavorite getFavoriteByResourceIdAndUserId(Integer resourceId, Integer userId);
    
    /**
     * 根据用户ID获取收藏列表
     * @param userId 用户ID
     * @return 资源收藏列表
     */
    List<ResourceFavorite> getFavoritesByUserId(Integer userId);
    
    /**
     * 根据资源ID获取收藏列表
     * @param resourceId 资源ID
     * @return 资源收藏列表
     */
    List<ResourceFavorite> getFavoritesByResourceId(Integer resourceId);
    
    /**
     * 根据ID删除收藏
     * @param id 收藏ID
     * @return 是否删除成功
     */
    boolean deleteFavorite(Integer id);
    
    /**
     * 根据资源ID和用户ID删除收藏
     * @param resourceId 资源ID
     * @param userId 用户ID
     * @return 是否删除成功
     */
    boolean deleteFavoriteByResourceIdAndUserId(Integer resourceId, Integer userId);
    
    /**
     * 检查资源是否已被用户收藏
     * @param resourceId 资源ID
     * @param userId 用户ID
     * @return 是否已收藏
     */
    boolean isFavorited(Integer resourceId, Integer userId);
}