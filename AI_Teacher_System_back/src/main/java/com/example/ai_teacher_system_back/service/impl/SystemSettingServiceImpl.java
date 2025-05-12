package com.example.ai_teacher_system_back.service.impl;

import com.example.ai_teacher_system_back.entity.SystemSetting;
import com.example.ai_teacher_system_back.mapper.SystemSettingMapper;
import com.example.ai_teacher_system_back.service.SystemSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 系统设置服务实现类
 */
@Service
public class SystemSettingServiceImpl implements SystemSettingService {

    @Autowired
    private SystemSettingMapper systemSettingMapper;
    
    @Override
    public SystemSetting createSystemSetting(SystemSetting systemSetting) {
        // 设置创建时间和更新时间
        Date now = new Date();
        if (systemSetting.getCreateTime() == null) {
            systemSetting.setCreateTime(now);
        }
        if (systemSetting.getUpdateTime() == null) {
            systemSetting.setUpdateTime(now);
        }
        
        systemSettingMapper.insert(systemSetting);
        return systemSetting;
    }

    @Override
    public SystemSetting updateSystemSetting(SystemSetting systemSetting) {
        // 设置更新时间
        systemSetting.setUpdateTime(new Date());
        
        systemSettingMapper.update(systemSetting);
        return systemSetting;
    }

    @Override
    public SystemSetting getSystemSettingById(Integer id) {
        return systemSettingMapper.selectById(id);
    }

    @Override
    public SystemSetting getSystemSettingByKeyAndUserId(String settingKey, Integer userId) {
        return systemSettingMapper.selectByKeyAndUserId(settingKey, userId);
    }

    @Override
    public List<SystemSetting> getSystemSettingsByUserId(Integer userId) {
        return systemSettingMapper.selectByUserId(userId);
    }

    @Override
    public boolean deleteSystemSetting(Integer id) {
        return systemSettingMapper.deleteById(id) > 0;
    }

    @Override
    public int deleteSystemSettingsByUserId(Integer userId) {
        return systemSettingMapper.deleteByUserId(userId);
    }

    @Override
    public SystemSetting getOrCreateSystemSetting(String settingKey, String defaultValue, Integer userId) {
        // 先尝试获取设置
        SystemSetting setting = systemSettingMapper.selectByKeyAndUserId(settingKey, userId);
        
        // 如果设置不存在，则创建新设置
        if (setting == null) {
            setting = new SystemSetting();
            setting.setSettingKey(settingKey);
            setting.setSettingValue(defaultValue);
            setting.setUserId(userId);
            setting.setSettingDescription("自动创建的设置");
            
            // 创建设置
            createSystemSetting(setting);
        }
        
        return setting;
    }
}