package com.example.ai_teacher_system_back.service;

import com.example.ai_teacher_system_back.entity.DataBackup;

import java.util.List;

/**
 * 数据备份服务接口
 */
public interface DataBackupService {
    
    /**
     * 创建数据备份
     * @param dataBackup 数据备份对象
     * @return 创建的数据备份对象
     */
    DataBackup createDataBackup(DataBackup dataBackup);
    
    /**
     * 更新数据备份
     * @param dataBackup 数据备份对象
     * @return 更新后的数据备份对象
     */
    DataBackup updateDataBackup(DataBackup dataBackup);
    
    /**
     * 根据ID获取数据备份
     * @param id 数据备份ID
     * @return 数据备份对象
     */
    DataBackup getDataBackupById(Integer id);
    
    /**
     * 根据创建者ID获取数据备份列表
     * @param creatorId 创建者ID
     * @return 数据备份列表
     */
    List<DataBackup> getDataBackupsByCreatorId(Integer creatorId);
    
    /**
     * 根据备份类型获取数据备份列表
     * @param backupType 备份类型
     * @return 数据备份列表
     */
    List<DataBackup> getDataBackupsByType(String backupType);
    
    /**
     * 根据ID删除数据备份
     * @param id 数据备份ID
     * @return 是否删除成功
     */
    boolean deleteDataBackup(Integer id);
    
    /**
     * 根据创建者ID删除数据备份
     * @param creatorId 创建者ID
     * @return 删除的记录数
     */
    int deleteDataBackupsByCreatorId(Integer creatorId);
    
    /**
     * 执行数据备份操作
     * @param backupType 备份类型
     * @param creatorId 创建者ID
     * @return 创建的数据备份对象
     */
    DataBackup executeBackup(String backupType, Integer creatorId);
    
    /**
     * 恢复数据备份
     * @param id 数据备份ID
     * @return 是否恢复成功
     */
    boolean restoreBackup(Integer id);
}