package com.example.ai_teacher_system_back.controller;

import com.example.ai_teacher_system_back.common.Result;
import com.example.ai_teacher_system_back.entity.DataBackup;
import com.example.ai_teacher_system_back.service.DataBackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据备份控制器
 */
@RestController
@RequestMapping("/api/data-backup")
public class DataBackupController {

    @Autowired
    private DataBackupService dataBackupService;
    
    /**
     * 创建数据备份
     * @param dataBackup 数据备份对象
     * @return 创建结果
     */
    @PostMapping
    public Result<DataBackup> createDataBackup(@RequestBody DataBackup dataBackup) {
        DataBackup created = dataBackupService.createDataBackup(dataBackup);
        return Result.success(created);
    }
    
    /**
     * 更新数据备份
     * @param id 数据备份ID
     * @param dataBackup 数据备份对象
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public Result<DataBackup> updateDataBackup(@PathVariable Integer id, @RequestBody DataBackup dataBackup) {
        dataBackup.setId(id);
        DataBackup updated = dataBackupService.updateDataBackup(dataBackup);
        return Result.success(updated);
    }
    
    /**
     * 根据ID获取数据备份
     * @param id 数据备份ID
     * @return 数据备份对象
     */
    @GetMapping("/{id}")
    public Result<DataBackup> getDataBackupById(@PathVariable Integer id) {
        DataBackup dataBackup = dataBackupService.getDataBackupById(id);
        if (dataBackup != null) {
            return Result.success(dataBackup);
        } else {
            return Result.error("数据备份不存在");
        }
    }
    
    /**
     * 根据创建者ID获取数据备份列表
     * @param creatorId 创建者ID
     * @return 数据备份列表
     */
    @GetMapping("/creator/{creatorId}")
    public Result<List<DataBackup>> getDataBackupsByCreatorId(@PathVariable Integer creatorId) {
        List<DataBackup> dataBackups = dataBackupService.getDataBackupsByCreatorId(creatorId);
        return Result.success(dataBackups);
    }
    
    /**
     * 根据备份类型获取数据备份列表
     * @param backupType 备份类型
     * @return 数据备份列表
     */
    @GetMapping("/type/{backupType}")
    public Result<List<DataBackup>> getDataBackupsByType(@PathVariable String backupType) {
        List<DataBackup> dataBackups = dataBackupService.getDataBackupsByType(backupType);
        return Result.success(dataBackups);
    }
    
    /**
     * 根据ID删除数据备份
     * @param id 数据备份ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteDataBackup(@PathVariable Integer id) {
        boolean success = dataBackupService.deleteDataBackup(id);
        if (success) {
            return Result.success(true);
        } else {
            return Result.error("删除数据备份失败");
        }
    }
    
    /**
     * 执行数据备份操作
     * @param backupType 备份类型
     * @param creatorId 创建者ID
     * @return 创建的数据备份对象
     */
    @PostMapping("/execute")
    public Result<DataBackup> executeBackup(@RequestParam String backupType, @RequestParam Integer creatorId) {
        DataBackup dataBackup = dataBackupService.executeBackup(backupType, creatorId);
        return Result.success(dataBackup);
    }
    
    /**
     * 恢复数据备份
     * @param id 数据备份ID
     * @return 恢复结果
     */
    @PostMapping("/restore/{id}")
    public Result<Boolean> restoreBackup(@PathVariable Integer id) {
        boolean success = dataBackupService.restoreBackup(id);
        if (success) {
            return Result.success(true);
        } else {
            return Result.error("恢复数据备份失败");
        }
    }
}