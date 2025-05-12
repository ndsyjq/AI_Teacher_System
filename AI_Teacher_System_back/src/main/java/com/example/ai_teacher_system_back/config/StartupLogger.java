package com.example.ai_teacher_system_back.config;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 启动日志记录器
 * 用于记录应用启动过程中的关键信息，帮助诊断启动问题
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StartupLogger implements ApplicationListener<ApplicationReadyEvent> {

    private final DataSource dataSource;
    
    @Value("${spring.datasource.url}")
    private String datasourceUrl;
    
    @Value("${spring.datasource.username}")
    private String datasourceUsername;
    
    @Value("${spring.jpa.hibernate.ddl-auto:update}")
    private String hibernateDdlAuto;

    @PostConstruct
    public void init() {
        log.info("=== 应用启动初始化 ===");
        log.info("数据源URL: {}", datasourceUrl);
        log.info("数据源用户名: {}", datasourceUsername);
        log.info("Hibernate DDL模式: {}", hibernateDdlAuto);
    }
    
    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("=== Spring上下文已刷新 ===");
        testDatabaseConnection();
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("=== 应用已准备就绪 ===");
        log.info("服务器启动完成，可以接收请求");
    }
    
    /**
     * 测试数据库连接并记录数据库信息
     */
    private void testDatabaseConnection() {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            log.info("成功连接到数据库: {}", metaData.getDatabaseProductName());
            log.info("数据库版本: {}", metaData.getDatabaseProductVersion());
            log.info("JDBC驱动: {} {}", metaData.getDriverName(), metaData.getDriverVersion());
            
            // 测试查询
            try {
                JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
                jdbcTemplate.queryForObject("SELECT 1", Integer.class);
                log.info("数据库查询测试成功");
            } catch (Exception e) {
                log.error("数据库查询测试失败: {}", e.getMessage(), e);
            }
        } catch (SQLException e) {
            log.error("数据库连接测试失败: {}", e.getMessage(), e);
            log.error("请检查数据库连接配置和数据库服务是否正常运行");
        }
    }
}