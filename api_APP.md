# App.vue API 文档

## 概述
`App.vue` 是 AI 教师智慧备课系统的主组件，包含登录、注册、仪表盘等功能模块。同时，它还管理用户的登录状态和菜单导航。

## 导入模块
| 模块名 | 说明 |
| ---- | ---- |
| `{ ref, reactive, provide }` from `vue` | Vue 3 的组合式 API，用于状态管理和依赖注入 |
| `mitt` | 用于创建事件总线 |
| `AIAssistant` | AI 助手组件 |
| `LessonPlanner` | 备课中心组件 |
| `Dashboard` | 控制面板组件 |
| `CourseManager` | 课程管理组件 |
| `ResourceLibrary` | 资源库组件 |
| `UserProfile` | 用户个人信息组件 |
| `PasswordChange` | 修改密码组件 |
| `SystemSettings` | 系统设置组件 |

## 响应式数据
| 变量名 | 类型 | 说明 |
| ---- | ---- | ---- |
| `loginForm` | `Reactive` | 登录表单数据，包含用户名和密码 |
| `registerForm` | `Reactive` | 注册表单数据，包含用户名、密码和确认密码 |
| `isLoggedIn` | `Ref<boolean>` | 用户登录状态 |
| `activeMenu` | `Ref<string>` | 当前激活的菜单 |
| `showRegister` | `Ref<boolean>` | 是否显示注册表单 |

## 方法
| 方法名 | 参数 | 返回值 | 说明 |
| ---- | ---- | ---- | ---- |
| `toggleRegister` | 无 | 无 | 切换登录和注册表单的显示状态 |
| `handleLogin` | 无 | 无 | 处理用户登录逻辑，验证用户名和密码后更新登录状态 |
| `handleRegister` | 无 | 无 | 处理用户注册逻辑，验证表单数据后隐藏注册表单并填充登录表单 |
| `handleLogout` | 无 | 无 | 处理用户退出登录逻辑，重置登录状态和表单数据 |

## 事件总线
- **创建**：使用 `mitt` 创建事件总线 `emitter`。
- **提供**：通过 `provide` 方法将事件总线提供给子组件。
- **监听**：监听 `change-menu` 事件，更新当前激活的菜单。

## 模板功能
### 登录页面
包含登录和注册表单，根据 `isLoggedIn` 和 `showRegister` 的状态切换显示。

### 主仪表盘
当用户登录后显示，包含侧边栏菜单和主内容区域。侧边栏菜单用于导航，主内容区域根据当前激活的菜单显示相应的组件。

## 样式
包含全局样式重置、登录页样式、主应用容器样式、侧边栏样式、头部样式和主内容区域样式，同时支持响应式设计。