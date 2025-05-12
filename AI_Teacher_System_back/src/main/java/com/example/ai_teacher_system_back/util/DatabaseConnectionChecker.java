package com.example.ai_teacher_system_back.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 数据库连接检查工具
 * 用于诊断数据库连接问题和验证数据库表结构
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseConnectionChecker {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;
    
    @Value("${spring.datasource.url}")
    private String datasourceUrl;
    
    /**
     * 检查数据库连接并记录详细信息
     * @return 是否连接成功
     */
    public boolean checkConnection() {
        log.info("开始检查数据库连接: {}", datasourceUrl);
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            log.info("数据库连接成功: {}", metaData.getDatabaseProductName());
            log.info("数据库版本: {}", metaData.getDatabaseProductVersion());
            log.info("JDBC驱动: {} {}", metaData.getDriverName(), metaData.getDriverVersion());
            return true;
        } catch (SQLException e) {
            log.error("数据库连接失败: {}", e.getMessage(), e);
            log.error("请检查数据库服务是否启动，以及连接配置是否正确");
            return false;
        }
    }
    
    /**
     * 检查指定表是否存在
     * @param tableName 表名
     * @return 是否存在
     */
    public boolean checkTableExists(String tableName) {
        log.info("检查表是否存在: {}", tableName);
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, tableName, new String[]{"TABLE"});
            boolean exists = tables.next();
            log.info("表 {} {}", tableName, exists ? "存在" : "不存在");
            return exists;
        } catch (SQLException e) {
            log.error("检查表存在性失败: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 获取表的列信息
     * @param tableName 表名
     * @return 列名列表
     */
    public List<String> getTableColumns(String tableName) {
        log.info("获取表的列信息: {}", tableName);
        List<String> columns = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet columnsResultSet = metaData.getColumns(null, null, tableName, null);
            while (columnsResultSet.next()) {
                String columnName = columnsResultSet.getString("COLUMN_NAME");
                String columnType = columnsResultSet.getString("TYPE_NAME");
                columns.add(columnName);
                log.info("表 {} 列: {} (类型: {})", tableName, columnName, columnType);
            }
        } catch (SQLException e) {
            log.error("获取表列信息失败: {}", e.getMessage(), e);
        }
        return columns;
    }
    
    /**
     * 执行简单查询测试
     * @return 是否成功
     */
    public boolean testQuery() {
        log.info("执行数据库查询测试");
        try {
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            log.info("查询测试成功，结果: {}", result);
            return true;
        } catch (Exception e) {
            log.error("查询测试失败: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 检查User表结构
     */
    public void checkUserTableStructure() {
        if (checkTableExists("user")) {
            List<String> columns = getTableColumns("user");
            log.info("User表共有{}列", columns.size());
            
            // 检查必要的列是否存在
            List<String> requiredColumns = List.of("id", "username", "password", "status", "create_time", "update_time");
            for (String requiredColumn : requiredColumns) {
                if (!columns.contains(requiredColumn)) {
                    log.error("User表缺少必要的列: {}", requiredColumn);
                }
            }
        } else {
            log.error("User表不存在，请检查数据库初始化是否正确");
        }
    }
}