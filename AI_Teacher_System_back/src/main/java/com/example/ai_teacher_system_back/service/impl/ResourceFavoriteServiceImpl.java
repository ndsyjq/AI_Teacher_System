package com.example.ai_teacher_system_back.service.impl;

import com.example.ai_teacher_system_back.entity.ResourceFavorite;
import com.example.ai_teacher_system_back.mapper.ResourceFavoriteMapper;
import com.example.ai_teacher_system_back.service.ResourceFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 资源收藏服务实现类
 */
@Service
public class ResourceFavoriteServiceImpl implements ResourceFavoriteService {

    @Autowired
    private ResourceFavoriteMapper resourceFavoriteMapper;
    
    @Override
    public ResourceFavorite addFavorite(ResourceFavorite resourceFavorite) {
        // 检查是否已经收藏
        ResourceFavorite existing = resourceFavoriteMapper.selectByResourceIdAndUserId(
                resourceFavorite.getResourceId(), resourceFavorite.getUserId());
        
        if (existing != null) {
            // 已经收藏过，直接返回现有收藏
            return existing;
        }
        
        // 设置创建时间
        if (resourceFavorite.getCreateTime() == null) {
            resourceFavorite.setCreateTime(new Date());
        }
        
        resourceFavoriteMapper.insert(resourceFavorite);
        return resourceFavorite;
    }

    @Override
    public ResourceFavorite getFavoriteById(Integer id) {
        return resourceFavoriteMapper.selectById(id);
    }

    @Override
    public ResourceFavorite getFavoriteByResourceIdAndUserId(Integer resourceId, Integer userId) {
        return resourceFavoriteMapper.selectByResourceIdAndUserId(resourceId, userId);
    }

    @Override
    public List<ResourceFavorite> getFavoritesByUserId(Integer userId) {
        return resourceFavoriteMapper.selectByUserId(userId);
    }

    @Override
    public List<ResourceFavorite> getFavoritesByResourceId(Integer resourceId) {
        return resourceFavoriteMapper.selectByResourceId(resourceId);
    }

    @Override
    public boolean deleteFavorite(Integer id) {
        return resourceFavoriteMapper.deleteById(id) > 0;
    }

    @Override
    public boolean deleteFavoriteByResourceIdAndUserId(Integer resourceId, Integer userId) {
        return resourceFavoriteMapper.deleteByResourceIdAndUserId(resourceId, userId) > 0;
    }

    @Override
    public boolean isFavorited(Integer resourceId, Integer userId) {
        ResourceFavorite favorite = resourceFavoriteMapper.selectByResourceIdAndUserId(resourceId, userId);
        return favorite != null;
    }
}