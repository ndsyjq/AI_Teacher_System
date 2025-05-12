# AI教师智慧备课系统后端开发计划

## 项目概述

本文档详细说明AI教师智慧备课系统后端的开发计划和实施步骤。后端基于Spring Boot框架，使用Java 17开发，实现与前端对接的所有API接口。

## 已完成工作

1. **基础框架搭建**
   - 创建Spring Boot项目结构
   - 配置Maven依赖（MySQL、MyBatis、JPA、Spring Security、JWT等）
   - 配置application.yml（数据库连接、JWT配置等）
   - 创建全局异常处理机制
   - 实现JWT认证工具类
   - 配置Spring Security安全框架

2. **基础类创建**
   - 通用响应结果类（Result）
   - 业务异常类（BusinessException）
   - 用户实体类（User）

## 待实现模块

### 1. 用户认证与管理模块

**相关API文档：** `api_APP.md`, `api_UserProfile.md`, `api_PasswordChange.md`

**实现步骤：**
1. 创建UserMapper接口和XML映射文件
2. 实现UserService接口和实现类
3. 实现UserController，包含以下接口：
   - 登录接口
   - 注册接口
   - 退出登录接口
   - 获取用户信息接口
   - 更新用户信息接口
   - 修改密码接口
   - 上传头像接口

### 2. 仪表盘模块

**相关API文档：** `api_Dashboard.md`

**实现步骤：**
1. 创建UserActivityMapper接口和XML映射文件
2. 实现DashboardService接口和实现类
3. 实现DashboardController，包含以下接口：
   - 获取仪表盘数据接口（课程数量、备课进度、AI助手使用情况、最近活动等）

### 3. 课程管理模块

**相关API文档：** `api_CourseManager.md`

**实现步骤：**
1. 创建Course实体类
2. 创建CourseMapper接口和XML映射文件
3. 实现CourseService接口和实现类
4. 实现CourseController，包含以下接口：
   - 添加课程接口
   - 查看课程列表接口
   - 查看课程详情接口
   - 编辑课程接口
   - 删除课程接口
   - 获取课程进度概览数据接口
   - 获取课程学科分布数据接口
   - 获取学习进度趋势数据接口

### 4. 备课计划模块

**相关API文档：** `api_LessonPlanner.md`

**实现步骤：**
1. 创建LessonPlan实体类
2. 创建LessonPlanMapper接口和XML映射文件
3. 实现LessonPlanService接口和实现类
4. 实现LessonPlanController，包含以下接口：
   - 保存备课计划接口
   - AI智能生成备课内容接口
   - 编辑备课计划接口
   - AI优化备课内容接口
   - 查看备课详情接口

### 5. 资源库模块

**相关API文档：** `api_ResouceLibrary.md`

**实现步骤：**
1. 创建Resource、ResourceFavorite、ResourceComment实体类
2. 创建相应的Mapper接口和XML映射文件
3. 实现ResourceService接口和实现类
4. 实现ResourceController，包含以下接口：
   - 上传资源接口
   - 下载资源接口
   - 预览资源接口
   - 获取资源列表接口
   - 资源收藏接口
   - 资源评论接口

### 6. AI助手模块

**相关API文档：** `api_AIAssistant.md`

**实现步骤：**
1. 创建AIAssistantUsage实体类
2. 创建AIAssistantUsageMapper接口和XML映射文件
3. 实现AIAssistantService接口和实现类
4. 实现AIAssistantController，包含以下接口：
   - 发送用户消息接口

### 7. 系统设置模块

**相关API文档：** `api_systemSetting.md`

**实现步骤：**
1. 创建SystemSetting和DataBackup实体类
2. 创建相应的Mapper接口和XML映射文件
3. 实现SystemSettingService接口和实现类
4. 实现SystemSettingController，包含以下接口：
   - 保存系统设置接口
   - 重置系统设置接口
   - 导出系统设置接口
   - 导入系统设置接口
   - 立即备份接口
   - 测试通知接口

## 开发优先级

1. 用户认证与管理模块（登录/注册是系统基础）
2. 课程管理模块（核心业务功能）
3. 备课计划模块（核心业务功能）
4. 资源库模块（核心业务功能）
5. AI助手模块（辅助功能）
6. 仪表盘模块（数据展示功能）
7. 系统设置模块（辅助功能）

## 开发时间安排

| 模块 | 预计工作量（人天） | 优先级 |
| ---- | ---- | ---- |
| 用户认证与管理模块 | 3 | 高 |
| 课程管理模块 | 4 | 高 |
| 备课计划模块 | 5 | 高 |
| 资源库模块 | 4 | 中 |
| AI助手模块 | 3 | 中 |
| 仪表盘模块 | 2 | 低 |
| 系统设置模块 | 3 | 低 |
| 测试与优化 | 5 | 中 |

## 技术实现要点

1. **数据库交互**
   - 使用MyBatis和JPA混合方式操作数据库
   - 复杂查询使用MyBatis的XML配置
   - 简单CRUD操作使用JPA

2. **安全认证**
   - 使用Spring Security进行安全控制
   - 使用JWT进行无状态认证
   - 密码加密存储

3. **文件处理**
   - 使用MultipartFile处理文件上传
   - 文件存储在服务器本地或云存储
   - 文件类型和大小限制

4. **AI功能实现**
   - 可考虑接入第三方AI服务（如百度AI、讯飞AI等）
   - 或使用本地NLP模型

5. **异常处理**
   - 使用全局异常处理器统一处理异常
   - 自定义业务异常

6. **日志记录**
   - 使用SLF4J+Logback记录日志
   - 关键操作记录到数据库

## 测试计划

1. **单元测试**
   - 使用JUnit5编写单元测试
   - 覆盖所有Service层方法

2. **接口测试**
   - 使用Postman或JUnit测试所有接口
   - 验证接口返回格式和数据正确性

3. **性能测试**
   - 使用JMeter进行性能测试
   - 测试系统在高并发下的表现

## 部署计划

1. **开发环境**
   - 本地开发环境
   - 使用H2内存数据库进行测试

2. **测试环境**
   - 使用Docker容器部署
   - 使用MySQL测试数据库

3. **生产环境**
   - 使用Docker容器或直接部署到服务器
   - 使用MySQL生产数据库
   - 配置Nginx反向代理

## 后续优化方向

1. 引入Redis缓存提高性能
2. 引入消息队列处理异步任务
3. 引入ElasticSearch提升搜索性能
4. 引入分布式文件存储系统
5. 引入监控系统监控应用状态