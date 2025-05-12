# api_AIAssistant 接口文档

## 接口概述
此接口用于 AI 助手组件，支持用户发送消息和接收 AI 回复功能。

## 请求信息

### 发送用户消息
- **请求 URL**：/api/ai-assistant/send-message
- **请求方法**：POST
- **请求参数**：
```json
{
  "content": "用户输入的消息内容"
}
```

## 响应信息
### 通用成功响应
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "type": "ai",
    "content": "AI 回复的消息内容"
  }
}
```