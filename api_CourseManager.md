# api_CourseManager 接口文档

## 接口概述
此接口用于课程管理组件，支持课程的增删改查操作以及课程分析图表数据的获取。

## 请求信息

### 添加课程
- **请求 URL**：/api/courses
- **请求方法**：POST
- **请求参数**：
```json
{
  "name": "课程名称",
  "subject": "学科",
  "grade": "年级",
  "semester": "学期",
  "description": "课程描述"
}
```

### 查看课程列表
- **请求 URL**：/api/courses
- **请求方法**：GET
- **请求参数**：无

### 查看课程详情
- **请求 URL**：/api/courses/{courseId}
- **请求方法**：GET
- **请求参数**：无

### 编辑课程
- **请求 URL**：/api/courses/{courseId}
- **请求方法**：PUT
- **请求参数**：
```json
{
  "name": "课程名称",
  "subject": "学科",
  "grade": "年级",
  "semester": "学期",
  "description": "课程描述"
}
```

### 删除课程
- **请求 URL**：/api/courses/{courseId}
- **请求方法**：DELETE
- **请求参数**：无

### 获取课程进度概览数据
- **请求 URL**：/api/courses/progress-overview
- **请求方法**：GET
- **请求参数**：无

### 获取课程学科分布数据
- **请求 URL**：/api/courses/subject-distribution
- **请求方法**：GET
- **请求参数**：无

### 获取学习进度趋势数据
- **请求 URL**：/api/courses/progress-trend
- **请求方法**：GET
- **请求参数**：无

## 响应信息

### 通用成功响应
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {}
}
```

### 通用失败响应
```json
{
  "code": 400,
  "message": "操作失败",
  "data": {}
}
```