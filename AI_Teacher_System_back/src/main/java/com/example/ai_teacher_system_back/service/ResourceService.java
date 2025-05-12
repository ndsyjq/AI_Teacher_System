package com.example.ai_teacher_system_back.service;

import com.example.ai_teacher_system_back.entity.Resource;
import com.example.ai_teacher_system_back.entity.ResourceComment;
import com.example.ai_teacher_system_back.entity.ResourceFavorite;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 资源服务接口
 */
public interface ResourceService {
    
    /**
     * 上传资源
     * @param file 文件
     * @param name 资源名称
     * @param description 资源描述
     * @param subject 学科
     * @param grade 年级
     * @param userId 用户ID
     * @return 上传后的资源对象
     */
    Resource uploadResource(MultipartFile file, String name, String description, String subject, String grade, Integer userId);
    
    /**
     * 下载资源
     * @param id 资源ID
     * @return 资源对象
     */
    Resource downloadResource(Integer id);
    
    /**
     * 预览资源
     * @param id 资源ID
     * @return 资源对象
     */
    Resource previewResource(Integer id);
    
    /**
     * 获取资源详情
     * @param id 资源ID
     * @return 资源对象
     */
    Resource getResourceById(Integer id);
    
    /**
     * 根据上传者ID查询资源列表
     * @param uploaderId 上传者ID
     * @return 资源列表
     */
    List<Resource> getResourcesByUploaderId(Integer uploaderId);
    
    /**
     * 根据学科查询资源列表
     * @param subject 学科
     * @return 资源列表
     */
    List<Resource> getResourcesBySubject(String subject);
    
    /**
     * 根据年级查询资源列表
     * @param grade 年级
     * @return 资源列表
     */
    List<Resource> getResourcesByGrade(String grade);
    
    /**
     * 根据文件类型查询资源列表
     * @param fileType 文件类型
     * @return 资源列表
     */
    List<Resource> getResourcesByFileType(String fileType);
    
    /**
     * 收藏资源
     * @param resourceId 资源ID
     * @param userId 用户ID
     * @return 收藏对象
     */
    ResourceFavorite favoriteResource(Integer resourceId, Integer userId);
    
    /**
     * 取消收藏
     * @param resourceId 资源ID
     * @param userId 用户ID
     * @return 是否取消成功
     */
    boolean cancelFavorite(Integer resourceId, Integer userId);
    
    /**
     * 获取用户收藏的资源列表
     * @param userId 用户ID
     * @return 收藏列表
     */
    List<ResourceFavorite> getUserFavorites(Integer userId);
    
    /**
     * 评论资源
     * @param resourceId 资源ID
     * @param userId 用户ID
     * @param content 评论内容
     * @param rating 评分
     * @return 评论对象
     */
    ResourceComment commentResource(Integer resourceId, Integer userId, String content, Integer rating);
    
    /**
     * 获取资源评论列表
     * @param resourceId 资源ID
     * @return 评论列表
     */
    List<ResourceComment> getResourceComments(Integer resourceId);
    
    /**
     * 删除资源
     * @param id 资源ID
     * @return 是否删除成功
     */
    boolean deleteResource(Integer id);
}