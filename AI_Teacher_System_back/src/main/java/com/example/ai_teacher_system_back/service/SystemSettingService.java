package com.example.ai_teacher_system_back.service;

import com.example.ai_teacher_system_back.entity.SystemSetting;

import java.util.List;

/**
 * 系统设置服务接口
 */
public interface SystemSettingService {
    
    /**
     * 创建系统设置
     * @param systemSetting 系统设置对象
     * @return 创建的系统设置对象
     */
    SystemSetting createSystemSetting(SystemSetting systemSetting);
    
    /**
     * 更新系统设置
     * @param systemSetting 系统设置对象
     * @return 更新后的系统设置对象
     */
    SystemSetting updateSystemSetting(SystemSetting systemSetting);
    
    /**
     * 根据ID获取系统设置
     * @param id 系统设置ID
     * @return 系统设置对象
     */
    SystemSetting getSystemSettingById(Integer id);
    
    /**
     * 根据设置键和用户ID获取系统设置
     * @param settingKey 设置键
     * @param userId 用户ID
     * @return 系统设置对象
     */
    SystemSetting getSystemSettingByKeyAndUserId(String settingKey, Integer userId);
    
    /**
     * 根据用户ID获取系统设置列表
     * @param userId 用户ID
     * @return 系统设置列表
     */
    List<SystemSetting> getSystemSettingsByUserId(Integer userId);
    
    /**
     * 根据ID删除系统设置
     * @param id 系统设置ID
     * @return 是否删除成功
     */
    boolean deleteSystemSetting(Integer id);
    
    /**
     * 根据用户ID删除系统设置
     * @param userId 用户ID
     * @return 删除的记录数
     */
    int deleteSystemSettingsByUserId(Integer userId);
    
    /**
     * 获取或创建系统设置
     * @param settingKey 设置键
     * @param defaultValue 默认值
     * @param userId 用户ID
     * @return 系统设置对象
     */
    SystemSetting getOrCreateSystemSetting(String settingKey, String defaultValue, Integer userId);
}