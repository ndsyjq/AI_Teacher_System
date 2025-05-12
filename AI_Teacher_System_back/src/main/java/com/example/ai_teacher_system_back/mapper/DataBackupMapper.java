package com.example.ai_teacher_system_back.mapper;

import com.example.ai_teacher_system_back.entity.DataBackup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据备份Mapper接口
 */
@Mapper
public interface DataBackupMapper {
    
    /**
     * 保存数据备份
     * @param dataBackup 数据备份对象
     * @return 影响行数
     */
    int insert(DataBackup dataBackup);
    
    /**
     * 更新数据备份
     * @param dataBackup 数据备份对象
     * @return 影响行数
     */
    int update(DataBackup dataBackup);
    
    /**
     * 根据ID查询数据备份
     * @param id 数据备份ID
     * @return 数据备份对象
     */
    DataBackup selectById(@Param("id") Integer id);
    
    /**
     * 根据创建者ID查询数据备份列表
     * @param creatorId 创建者ID
     * @return 数据备份列表
     */
    List<DataBackup> selectByCreatorId(@Param("creatorId") Integer creatorId);
    
    /**
     * 根据备份类型查询数据备份列表
     * @param backupType 备份类型
     * @return 数据备份列表
     */
    List<DataBackup> selectByBackupType(@Param("backupType") String backupType);
    
    /**
     * 根据ID删除数据备份
     * @param id 数据备份ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Integer id);
    
    /**
     * 根据创建者ID删除数据备份
     * @param creatorId 创建者ID
     * @return 影响行数
     */
    int deleteByCreatorId(@Param("creatorId") Integer creatorId);
}