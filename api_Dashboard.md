# api_Dashboard 接口文档

## 接口概述
此接口用于 AI 教师智慧备课系统的仪表盘页面，支持获取课程信息、备课进度、AI 助手使用情况以及最近活动数据等功能。

## 请求信息

### 获取仪表盘数据
- **请求 URL**：/api/dashboard/data
- **请求方法**：GET
- **请求参数**：无

## 响应信息
### 通用成功响应
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "courseCount": 5,
    "courseProgress": 70,
    "lessonPlanProgress": 60,
    "aiAssistantUsage": 12,
    "aiAssistantProgress": 40,
    "recentActivities": [
      {
        "date": "2023-06-01",
        "content": "完成了《高中数学必修一》第1章的备课"
      },
      {
        "date": "2023-06-02",
        "content": "完成了《高中数学必修一》第2章的备课"
      },
      {
        "date": "2023-06-03",
        "content": "完成了《高中数学必修一》第3章的备课"
      },
      {
        "date": "2023-06-04",
        "content": "完成了《高中数学必修一》第4章的备课"
      }
    ]
  }
}
```