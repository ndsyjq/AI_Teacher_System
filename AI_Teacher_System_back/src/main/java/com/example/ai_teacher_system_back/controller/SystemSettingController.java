package com.example.ai_teacher_system_back.controller;

import com.example.ai_teacher_system_back.common.Result;
import com.example.ai_teacher_system_back.entity.SystemSetting;
import com.example.ai_teacher_system_back.service.SystemSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统设置控制器
 */
@RestController
@RequestMapping("/api/system-setting")
public class SystemSettingController {

    @Autowired
    private SystemSettingService systemSettingService;
    
    /**
     * 创建系统设置
     * @param systemSetting 系统设置对象
     * @return 创建结果
     */
    @PostMapping
    public Result<SystemSetting> createSystemSetting(@RequestBody SystemSetting systemSetting) {
        SystemSetting created = systemSettingService.createSystemSetting(systemSetting);
        return Result.success(created);
    }
    
    /**
     * 更新系统设置
     * @param id 系统设置ID
     * @param systemSetting 系统设置对象
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public Result<SystemSetting> updateSystemSetting(@PathVariable Integer id, @RequestBody SystemSetting systemSetting) {
        systemSetting.setId(id);
        SystemSetting updated = systemSettingService.updateSystemSetting(systemSetting);
        return Result.success(updated);
    }
    
    /**
     * 根据ID获取系统设置
     * @param id 系统设置ID
     * @return 系统设置对象
     */
    @GetMapping("/{id}")
    public Result<SystemSetting> getSystemSettingById(@PathVariable Integer id) {
        SystemSetting systemSetting = systemSettingService.getSystemSettingById(id);
        if (systemSetting != null) {
            return Result.success(systemSetting);
        } else {
            return Result.error("系统设置不存在");
        }
    }
    
    /**
     * 根据设置键和用户ID获取系统设置
     * @param settingKey 设置键
     * @param userId 用户ID
     * @return 系统设置对象
     */
    @GetMapping("/key")
    public Result<SystemSetting> getSystemSettingByKeyAndUserId(
            @RequestParam String settingKey, 
            @RequestParam Integer userId) {
        SystemSetting systemSetting = systemSettingService.getSystemSettingByKeyAndUserId(settingKey, userId);
        if (systemSetting != null) {
            return Result.success(systemSetting);
        } else {
            return Result.error("系统设置不存在");
        }
    }
    
    /**
     * 根据用户ID获取系统设置列表
     * @param userId 用户ID
     * @return 系统设置列表
     */
    @GetMapping("/user/{userId}")
    public Result<List<SystemSetting>> getSystemSettingsByUserId(@PathVariable Integer userId) {
        List<SystemSetting> systemSettings = systemSettingService.getSystemSettingsByUserId(userId);
        return Result.success(systemSettings);
    }
    
    /**
     * 根据ID删除系统设置
     * @param id 系统设置ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteSystemSetting(@PathVariable Integer id) {
        boolean success = systemSettingService.deleteSystemSetting(id);
        if (success) {
            return Result.success(true);
        } else {
            return Result.error("删除系统设置失败");
        }
    }
    
    /**
     * 获取或创建系统设置
     * @param settingKey 设置键
     * @param defaultValue 默认值
     * @param userId 用户ID
     * @return 系统设置对象
     */
    @GetMapping("/get-or-create")
    public Result<SystemSetting> getOrCreateSystemSetting(
            @RequestParam String settingKey, 
            @RequestParam String defaultValue, 
            @RequestParam Integer userId) {
        SystemSetting systemSetting = systemSettingService.getOrCreateSystemSetting(settingKey, defaultValue, userId);
        return Result.success(systemSetting);
    }
}