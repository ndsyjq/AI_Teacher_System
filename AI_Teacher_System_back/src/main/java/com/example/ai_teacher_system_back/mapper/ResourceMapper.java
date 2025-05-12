package com.example.ai_teacher_system_back.mapper;

import com.example.ai_teacher_system_back.entity.Resource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 教学资源Mapper接口
 */
@Mapper
public interface ResourceMapper {
    
    /**
     * 保存资源
     * @param resource 资源对象
     * @return 影响行数
     */
    int insert(Resource resource);
    
    /**
     * 更新资源
     * @param resource 资源对象
     * @return 影响行数
     */
    int update(Resource resource);
    
    /**
     * 根据ID查询资源
     * @param id 资源ID
     * @return 资源对象
     */
    Resource selectById(@Param("id") Integer id);
    
    /**
     * 根据上传者ID查询资源列表
     * @param uploaderId 上传者ID
     * @return 资源列表
     */
    List<Resource> selectByUploaderId(@Param("uploaderId") Integer uploaderId);
    
    /**
     * 根据学科查询资源列表
     * @param subject 学科
     * @return 资源列表
     */
    List<Resource> selectBySubject(@Param("subject") String subject);
    
    /**
     * 根据年级查询资源列表
     * @param grade 年级
     * @return 资源列表
     */
    List<Resource> selectByGrade(@Param("grade") String grade);
    
    /**
     * 根据文件类型查询资源列表
     * @param fileType 文件类型
     * @return 资源列表
     */
    List<Resource> selectByFileType(@Param("fileType") String fileType);
    
    /**
     * 更新资源下载次数
     * @param id 资源ID
     * @return 影响行数
     */
    int updateDownloadCount(@Param("id") Integer id);
    
    /**
     * 根据ID删除资源
     * @param id 资源ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Integer id);
}