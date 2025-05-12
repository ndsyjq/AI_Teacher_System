package com.example.ai_teacher_system_back.mapper;

import com.example.ai_teacher_system_back.entity.SystemSetting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统设置Mapper接口
 */
@Mapper
public interface SystemSettingMapper {
    
    /**
     * 保存系统设置
     * @param systemSetting 系统设置对象
     * @return 影响行数
     */
    int insert(SystemSetting systemSetting);
    
    /**
     * 更新系统设置
     * @param systemSetting 系统设置对象
     * @return 影响行数
     */
    int update(SystemSetting systemSetting);
    
    /**
     * 根据ID查询系统设置
     * @param id 系统设置ID
     * @return 系统设置对象
     */
    SystemSetting selectById(@Param("id") Integer id);
    
    /**
     * 根据设置键查询系统设置
     * @param settingKey 设置键
     * @param userId 用户ID
     * @return 系统设置对象
     */
    SystemSetting selectByKeyAndUserId(@Param("settingKey") String settingKey, @Param("userId") Integer userId);
    
    /**
     * 根据用户ID查询系统设置列表
     * @param userId 用户ID
     * @return 系统设置列表
     */
    List<SystemSetting> selectByUserId(@Param("userId") Integer userId);
    
    /**
     * 根据ID删除系统设置
     * @param id 系统设置ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Integer id);
    
    /**
     * 根据用户ID删除系统设置
     * @param userId 用户ID
     * @return 影响行数
     */
    int deleteByUserId(@Param("userId") Integer userId);
}