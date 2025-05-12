package com.example.ai_teacher_system_back.controller;

import com.example.ai_teacher_system_back.common.Result;
import com.example.ai_teacher_system_back.entity.ResourceFavorite;
import com.example.ai_teacher_system_back.service.ResourceFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 资源收藏控制器
 */
@RestController
@RequestMapping("/api/resource-favorite")
public class ResourceFavoriteController {

    @Autowired
    private ResourceFavoriteService resourceFavoriteService;
    
    /**
     * 添加收藏
     * @param resourceFavorite 资源收藏对象
     * @return 创建结果
     */
    @PostMapping
    public Result<ResourceFavorite> addFavorite(@RequestBody ResourceFavorite resourceFavorite) {
        ResourceFavorite created = resourceFavoriteService.addFavorite(resourceFavorite);
        return Result.success(created);
    }
    
    /**
     * 根据ID获取收藏
     * @param id 收藏ID
     * @return 资源收藏对象
     */
    @GetMapping("/{id}")
    public Result<ResourceFavorite> getFavoriteById(@PathVariable Integer id) {
        ResourceFavorite favorite = resourceFavoriteService.getFavoriteById(id);
        if (favorite != null) {
            return Result.success(favorite);
        } else {
            return Result.error("收藏不存在");
        }
    }
    
    /**
     * 根据用户ID获取收藏列表
     * @param userId 用户ID
     * @return 资源收藏列表
     */
    @GetMapping("/user/{userId}")
    public Result<List<ResourceFavorite>> getFavoritesByUserId(@PathVariable Integer userId) {
        List<ResourceFavorite> favorites = resourceFavoriteService.getFavoritesByUserId(userId);
        return Result.success(favorites);
    }
    
    /**
     * 根据资源ID获取收藏列表
     * @param resourceId 资源ID
     * @return 资源收藏列表
     */
    @GetMapping("/resource/{resourceId}")
    public Result<List<ResourceFavorite>> getFavoritesByResourceId(@PathVariable Integer resourceId) {
        List<ResourceFavorite> favorites = resourceFavoriteService.getFavoritesByResourceId(resourceId);
        return Result.success(favorites);
    }
    
    /**
     * 根据ID删除收藏
     * @param id 收藏ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteFavorite(@PathVariable Integer id) {
        boolean success = resourceFavoriteService.deleteFavorite(id);
        if (success) {
            return Result.success(true);
        } else {
            return Result.error("删除收藏失败");
        }
    }
    
    /**
     * 根据资源ID和用户ID删除收藏
     * @param resourceId 资源ID
     * @param userId 用户ID
     * @return 删除结果
     */
    @DeleteMapping("/resource/{resourceId}/user/{userId}")
    public Result<Boolean> deleteFavoriteByResourceIdAndUserId(
            @PathVariable Integer resourceId, 
            @PathVariable Integer userId) {
        boolean success = resourceFavoriteService.deleteFavoriteByResourceIdAndUserId(resourceId, userId);
        if (success) {
            return Result.success(true);
        } else {
            return Result.error("删除收藏失败");
        }
    }
    
    /**
     * 检查资源是否已被用户收藏
     * @param resourceId 资源ID
     * @param userId 用户ID
     * @return 是否已收藏
     */
    @GetMapping("/check")
    public Result<Boolean> isFavorited(
            @RequestParam Integer resourceId, 
            @RequestParam Integer userId) {
        boolean favorited = resourceFavoriteService.isFavorited(resourceId, userId);
        return Result.success(favorited);
    }
}