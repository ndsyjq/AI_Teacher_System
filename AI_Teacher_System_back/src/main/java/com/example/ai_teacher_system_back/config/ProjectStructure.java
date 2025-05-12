package com.example.ai_teacher_system_back.config;

/**
 * 项目结构说明
 * 
 * 本项目采用分层架构设计，主要包含以下几个层次：
 * 
 * 1. 实体层(entity)：对应数据库表结构的Java对象
 *    - User: 用户实体
 *    - Course: 课程实体
 *    - LessonPlan: 备课计划实体
 *    - Resource: 教学资源实体
 *    - SystemSetting: 系统设置实体
 *    - DataBackup: 数据备份记录实体
 *    - UserActivity: 用户活动记录实体
 *    - AIAssistantUsage: AI助手使用记录实体
 *    - ResourceFavorite: 资源收藏实体
 *    - ResourceComment: 资源评论实体
 * 
 * 2. DAO层(mapper)：数据访问对象，负责与数据库交互
 *    - UserMapper: 用户数据访问
 *    - CourseMapper: 课程数据访问
 *    - LessonPlanMapper: 备课计划数据访问
 *    - ResourceMapper: 教学资源数据访问
 *    - SystemSettingMapper: 系统设置数据访问
 *    - DataBackupMapper: 数据备份记录数据访问
 *    - UserActivityMapper: 用户活动记录数据访问
 *    - AIAssistantUsageMapper: AI助手使用记录数据访问
 *    - ResourceFavoriteMapper: 资源收藏数据访问
 *    - ResourceCommentMapper: 资源评论数据访问
 * 
 * 3. Service层(service)：业务逻辑层，处理具体业务逻辑
 *    - UserService: 用户相关业务
 *    - CourseService: 课程相关业务
 *    - LessonPlanService: 备课计划相关业务
 *    - ResourceService: 教学资源相关业务
 *    - SystemSettingService: 系统设置相关业务
 *    - DataBackupService: 数据备份相关业务
 *    - UserActivityService: 用户活动记录相关业务
 *    - AIAssistantService: AI助手相关业务
 *    - ResourceFavoriteService: 资源收藏相关业务
 *    - ResourceCommentService: 资源评论相关业务
 * 
 * 4. Controller层(controller)：控制器层，处理HTTP请求
 *    - UserController: 用户相关接口
 *    - CourseController: 课程相关接口
 *    - LessonPlanController: 备课计划相关接口
 *    - ResourceController: 教学资源相关接口
 *    - SystemSettingController: 系统设置相关接口
 *    - DataBackupController: 数据备份相关接口
 *    - UserActivityController: 用户活动记录相关接口
 *    - AIAssistantController: AI助手相关接口
 *    - ResourceFavoriteController: 资源收藏相关接口
 *    - ResourceCommentController: 资源评论相关接口
 * 
 * 5. 配置层(config)：系统配置类
 *    - SecurityConfig: 安全配置
 *    - JwtConfig: JWT配置
 *    - WebConfig: Web配置
 *    - CorsConfig: 跨域配置
 * 
 * 6. 工具类(util)：通用工具类
 *    - JwtUtil: JWT工具类
 *    - FileUtil: 文件处理工具类
 *    - ResponseUtil: 响应结果工具类
 * 
 * 7. 常量类(constant)：系统常量定义
 *    - ResultCode: 响应状态码
 *    - SystemConstant: 系统常量
 * 
 * 8. 异常处理(exception)：自定义异常和全局异常处理
 *    - GlobalExceptionHandler: 全局异常处理器
 *    - BusinessException: 业务异常
 * 
 * 9. DTO(dto)：数据传输对象
 *    - 请求DTO: 接收前端请求参数
 *    - 响应DTO: 返回给前端的数据结构
 * 
 * 10. VO(vo)：视图对象
 *    - 用于展示层的数据封装
 */
public class ProjectStructure {
    // 此类仅用于说明项目结构，不包含实际代码
}