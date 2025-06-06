# api_UserProfile 接口文档

## 一、头像上传接口
### 1. 基本信息
- **接口地址**：`/api/upload`
- **请求方法**：`POST`

### 2. 请求参数
| 参数名 | 类型 | 描述 | 是否必填 |
| ---- | ---- | ---- | ---- |
| 文件 | File | 用户上传的头像文件 | 是 |

### 3. 请求限制
- **文件格式**：仅支持 JPG 或 PNG 格式。
- **文件大小**：不能超过 2MB。

### 4. 响应示例
```json
{
  "raw": "文件原始数据",
  "url": "文件上传后的访问地址"
}
```

### 5. 错误提示
- 若文件格式不符合要求，提示：`头像图片只能是 JPG 或 PNG 格式!`
- 若文件大小超过限制，提示：`头像图片大小不能超过 2MB!`

## 二、保存个人信息接口
### 1. 基本信息
- **接口地址**：暂未在代码中体现，需补充。
- **请求方法**：暂未在代码中体现，需补充。

### 2. 请求参数
| 参数名 | 类型 | 描述 | 是否必填 |
| ---- | ---- | ---- | ---- |
| username | String | 用户名 | 否（当前为只读） |
| realName | String | 真实姓名 | 是 |
| email | String | 邮箱 | 是 |
| phone | String | 手机号码 | 是 |
| department | String | 所属部门 | 是 |
| position | String | 职位 | 是 |
| avatar | String | 头像地址 | 否 |

### 3. 响应示例
目前代码中未实现实际逻辑，仅显示成功提示：`个人信息保存成功`。实际响应格式需根据后端实现补充。