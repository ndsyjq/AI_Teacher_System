package com.example.ai_teacher_system_back.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ai_teacher_system_back.common.Result;
import com.example.ai_teacher_system_back.util.DatabaseConnectionChecker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 诊断控制器
 * 提供系统诊断相关的API接口，用于排查系统问题
 */
@Slf4j
@RestController
@RequestMapping("/api/diagnostic")
@RequiredArgsConstructor
public class DiagnosticController {

    private final DatabaseConnectionChecker databaseConnectionChecker;
    
    @Value("${spring.datasource.url}")
    private String datasourceUrl;
    
    @Value("${spring.jpa.hibernate.ddl-auto:update}")
    private String hibernateDdlAuto;
    
    /**
     * 检查系统状态
     * @return 系统状态信息
     */
    @GetMapping("/status")
    public Result<Map<String, Object>> checkStatus() {
        log.info("执行系统状态检查");
        Map<String, Object> status = new HashMap<>();
        
        // 检查数据库连接
        boolean dbConnected = databaseConnectionChecker.checkConnection();
        status.put("databaseConnected", dbConnected);
        status.put("databaseUrl", datasourceUrl);
        status.put("hibernateDdlAuto", hibernateDdlAuto);
        
        // 如果数据库连接成功，检查表结构
        if (dbConnected) {
            boolean userTableExists = databaseConnectionChecker.checkTableExists("user");
            status.put("userTableExists", userTableExists);
            
            if (userTableExists) {
                status.put("userTableColumns", databaseConnectionChecker.getTableColumns("user"));
            }
            
            // 测试简单查询
            boolean querySuccess = databaseConnectionChecker.testQuery();
            status.put("querySuccess", querySuccess);
        }
        
        // 记录JVM信息
        status.put("javaVersion", System.getProperty("java.version"));
        status.put("javaHome", System.getProperty("java.home"));
        status.put("availableProcessors", Runtime.getRuntime().availableProcessors());
        status.put("freeMemory", Runtime.getRuntime().freeMemory());
        status.put("maxMemory", Runtime.getRuntime().maxMemory());
        
        log.info("系统状态检查完成: {}", status);
        return Result.success(status, "系统状态检查完成");
    }
    
    /**
     * 检查数据库连接
     * @return 数据库连接状态
     */
    @GetMapping("/database")
    public Result<Map<String, Object>> checkDatabase() {
        log.info("执行数据库连接检查");
        Map<String, Object> dbStatus = new HashMap<>();
        
        boolean connected = databaseConnectionChecker.checkConnection();
        dbStatus.put("connected", connected);
        dbStatus.put("url", datasourceUrl);
        
        if (connected) {
            // 检查User表结构
            databaseConnectionChecker.checkUserTableStructure();
            
            // 获取所有表
            try {
                dbStatus.put("userTableExists", databaseConnectionChecker.checkTableExists("user"));
                dbStatus.put("userTableColumns", databaseConnectionChecker.getTableColumns("user"));
            } catch (Exception e) {
                log.error("获取表信息失败", e);
                dbStatus.put("error", e.getMessage());
            }
        }
        
        log.info("数据库连接检查完成: {}", dbStatus);
        return Result.success(dbStatus, connected ? "数据库连接正常" : "数据库连接失败");
    }
}