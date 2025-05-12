<script setup>
import { ref, reactive, provide, onMounted } from 'vue';
import { ElMessage, ElLoading } from 'element-plus';
import mitt from 'mitt';
import AIAssistant from './components/AIAssistant.vue';
import LessonPlanner from './components/LessonPlanner.vue';
import Dashboard from './components/Dashboard.vue';
import CourseManager from './components/CourseManager.vue';
import ResourceLibrary from './components/ResourceLibrary.vue';
import UserProfile from './components/UserProfile.vue';
import PasswordChange from './components/PasswordChange.vue';
import SystemSettings from './components/SystemSettings.vue';
import { userService } from './services';

// Login and registration form data and methods
const loginForm = reactive({
  username: '',
  password: ''
});

const registerForm = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  realName: '',
  email: '',
  phone: ''
});

const isLoggedIn = ref(false);
const activeMenu = ref('dashboard');
const showRegister = ref(false);
const loading = ref(false);
const loginError = ref('');
const registerError = ref('');

// 检查是否已登录
onMounted(() => {
  const token = localStorage.getItem('token');
  if (token) {
    checkLoginStatus();
  }
});

// 检查登录状态
const checkLoginStatus = async () => {
  try {
    loading.value = true;
    const response = await userService.getUserInfo();
    if (response.code === 200) {
      isLoggedIn.value = true;
      // 可以在这里保存用户信息到全局状态
    } else {
      // 登录状态失效，清除token
      localStorage.removeItem('token');
      isLoggedIn.value = false;
    }
  } catch (error) {
    console.error('检查登录状态失败:', error);
    localStorage.removeItem('token');
    isLoggedIn.value = false;
  } finally {
    loading.value = false;
  }
};

// Toggle between login and registration forms
const toggleRegister = () => {
  showRegister.value = !showRegister.value;
  loginError.value = '';
  registerError.value = '';
};

const handleLogin = async () => {
  // 表单验证
  if (!loginForm.username) {
    loginError.value = '请输入用户名';
    return;
  }
  if (!loginForm.password) {
    loginError.value = '请输入密码';
    return;
  }
  
  try {
    loading.value = true;
    const response = await userService.login(loginForm.username, loginForm.password);
    if (response.code === 200) {
      // 保存token
      localStorage.setItem('token', response.data.token);
      isLoggedIn.value = true;
      ElMessage.success('登录成功');
    } else {
      loginError.value = response.message || '登录失败，请检查用户名和密码';
    }
  } catch (error) {
    console.error('登录失败:', error);
    loginError.value = '登录失败，请稍后再试';
  } finally {
    loading.value = false;
  }
};

const handleRegister = async () => {
  // 表单验证
  if (!registerForm.username) {
    registerError.value = '请输入用户名';
    return;
  }
  if (!registerForm.password) {
    registerError.value = '请输入密码';
    return;
  }
  if (registerForm.password !== registerForm.confirmPassword) {
    registerError.value = '两次输入的密码不一致';
    return;
  }
  
  try {
    loading.value = true;
    const userData = {
      username: registerForm.username,
      password: registerForm.password,
      confirmPassword: registerForm.confirmPassword,
      realName: registerForm.realName,
      email: registerForm.email,
      phone: registerForm.phone
    };
    
    // 打印出正在发送的数据
    console.log("发送注册请求的数据：", userData);
    
    const response = await userService.register(userData);
    
    // 打印出服务器返回的响应
    console.log("服务器返回的响应：", response);
    
    if (response.code === 200) {
      ElMessage.success('注册成功，请登录');
      showRegister.value = false;
      loginForm.username = registerForm.username;
      // 清空注册表单
      registerForm.username = '';
      registerForm.password = '';
      registerForm.confirmPassword = '';
      registerForm.realName = '';
      registerForm.email = '';
      registerForm.phone = '';
    } else {
      registerError.value = response.message || '注册失败';
    }
  } catch (error) {
    console.error('注册失败:', error);
    registerError.value = '注册失败，请稍后再试';
  } finally {
    loading.value = false;
  }
};

const handleLogout = async () => {
  try {
    loading.value = true;
    await userService.logout();
    // 无论成功与否，都清除本地token并退出登录
    localStorage.removeItem('token');
    isLoggedIn.value = false;
    loginForm.username = '';
    loginForm.password = '';
    ElMessage.success('已退出登录');
  } catch (error) {
    console.error('退出登录失败:', error);
    // 即使API调用失败，也强制退出登录
    localStorage.removeItem('token');
    isLoggedIn.value = false;
  } finally {
    loading.value = false;
  }
};

// 创建事件总线
const emitter = mitt();

// 提供事件总线给子组件
provide('emitter', emitter);

// 监听菜单变化事件
emitter.on('change-menu', (menu) => {
  activeMenu.value = menu;
});

</script>

<template>
  <!-- Login Page -->
  <div v-if="!isLoggedIn" class="login-container">
    <div class="form-container">
      <!-- Login Form -->
      <el-card class="login-card" v-if="!showRegister">
        <template #header>
          <div class="login-header">
            <h2>AI教师智慧备课系统</h2>
          </div>
        </template>
        <el-form :model="loginForm" label-position="top">
          <el-form-item label="用户名">
            <el-input v-model="loginForm.username" placeholder="请输入用户名">
              <template #prefix>
                <el-icon><User /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item label="密码">
            <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" @keyup.enter="handleLogin">
              <template #prefix>
                <el-icon><Lock /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <div v-if="loginError" class="error-message">{{ loginError }}</div>
          <el-form-item>
            <el-button type="primary" @click="handleLogin" style="width: 100%" :loading="loading">登录</el-button>
          </el-form-item>
          <div class="form-footer">
            <span>还没有账号？</span>
            <el-button link @click="toggleRegister">立即注册</el-button>
          </div>
        </el-form>
      </el-card>

      <!-- Register Form -->
      <el-card class="login-card" v-else>
        <template #header>
          <div class="login-header">
            <h2>用户注册</h2>
          </div>
        </template>
        <el-form :model="registerForm" label-position="top">
          <el-form-item label="用户名">
            <el-input v-model="registerForm.username" placeholder="请输入用户名">
              <template #prefix>
                <el-icon><User /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item label="密码">
            <el-input v-model="registerForm.password" type="password" placeholder="请输入密码">
              <template #prefix>
                <el-icon><Lock /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item label="确认密码">
            <el-input v-model="registerForm.confirmPassword" type="password" placeholder="请再次输入密码">
              <template #prefix>
                <el-icon><Lock /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item label="真实姓名">
            <el-input v-model="registerForm.realName" placeholder="请输入真实姓名">
              <template #prefix>
                <el-icon><UserFilled /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="registerForm.email" placeholder="请输入邮箱">
              <template #prefix>
                <el-icon><Message /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item label="手机号码">
            <el-input v-model="registerForm.phone" placeholder="请输入手机号码">
              <template #prefix>
                <el-icon><Phone /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <div v-if="registerError" class="error-message">{{ registerError }}</div>
          <el-form-item>
            <el-button type="primary" @click="handleRegister" style="width: 100%" :loading="loading">注册</el-button>
          </el-form-item>
          <div class="form-footer">
            <span>已有账号？</span>
            <el-button link @click="toggleRegister">返回登录</el-button>
          </div>
        </el-form>
      </el-card>
    </div>
  </div>

  <!-- Main Dashboard -->
  <el-container v-else class="app-container">
    <!-- Sidebar -->
    <el-aside width="200px" class="sidebar">
      <el-menu
        :default-active="activeMenu"
        class="sidebar-menu"
        @select="activeMenu = $event"
      >
        <el-menu-item index="dashboard">
          <el-icon><DataBoard /></el-icon>
          <span>控制面板</span>
        </el-menu-item>
        <el-menu-item index="courses">
          <el-icon><Reading /></el-icon>
          <span>课程管理</span>
        </el-menu-item>
        <el-menu-item index="lesson-plan">
          <el-icon><Document /></el-icon>
          <span>备课中心</span>
        </el-menu-item>
        <el-menu-item index="ai-assistant">
          <el-icon><ChatDotRound /></el-icon>
          <span>AI助手</span>
        </el-menu-item>
        <el-menu-item index="resources">
          <el-icon><Collection /></el-icon>
          <span>资源库</span>
        </el-menu-item>
        <el-menu-item index="settings">
          <el-icon><Setting /></el-icon>
          <span>系统设置</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <!-- Main Content -->
    <el-container class="main-section">
      <el-header class="app-header">
        <div class="header-title">AI教师智慧备课系统</div>
        <div class="header-user">
          <el-dropdown>
            <span class="el-dropdown-link">
              {{ loginForm.username }}<el-icon class="el-icon--right"><arrow-down /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="activeMenu = 'profile'">个人信息</el-dropdown-item>
                <el-dropdown-item @click="activeMenu = 'password'">修改密码</el-dropdown-item>
                <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main class="app-main">
        <!-- Active Content Based on Menu Selection -->
        <Dashboard v-if="activeMenu === 'dashboard'" />
        <CourseManager v-if="activeMenu === 'courses'" />
        <LessonPlanner v-if="activeMenu === 'lesson-plan'" />
        <AIAssistant v-if="activeMenu === 'ai-assistant'" />
        <ResourceLibrary v-if="activeMenu === 'resources'" />
        <UserProfile v-if="activeMenu === 'profile'" />
        <PasswordChange v-if="activeMenu === 'password'" />
        <SystemSettings v-if="activeMenu === 'settings'" />
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
/* 重置全局样式 */
html, body {
  margin: 0;
  padding: 0;
  height: 100%;
  width: 100%;
  overflow: hidden;
  overflow-x:hidden;
}

#app {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  height: 100%;
  width: 100%;
}


/* 登录页样式 */
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  height: 100vh;
  width: 100vw;
  margin: 0;
  padding: 0;
  position: fixed;
  top: 0;
  left: 0;
  background-color: #f5f7fa;
  background-image: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  overflow: hidden;
}

.form-container {
  width: 100%;
  max-width: 400px;
  padding: 20px;
  z-index: 10;
}

.login-card {
  width: 100%;
  box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  background-color: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
}

.login-header {
  text-align: center;
  padding: 10px 0;
}

.login-header h2 {
  margin: 0;
  color: #409EFF;
  font-size: 24px;
}

.form-footer {
  text-align: center;
  margin-top: 15px;
  padding-bottom: 10px;
}

.error-message {
  color: #F56C6C;
  font-size: 14px;
  margin: 5px 0 10px;
  text-align: center;
}

/* 添加一些装饰元素 */
.login-container::before {
  content: '';
  position: absolute;
  top: -50px;
  right: -50px;
  width: 300px;
  height: 300px;
  border-radius: 50%;
  background-color: rgba(64, 158, 255, 0.1);
  z-index: 1;
}

.login-container::after {
  content: '';
  position: absolute;
  bottom: -50px;
  left: -50px;
  width: 200px;
  height: 200px;
  border-radius: 50%;
  background-color: rgba(64, 158, 255, 0.15);
  z-index: 1;
}

/* 确保全局样式正确设置 */
html, body {
  margin: 0;
  padding: 0;
  height: 100%;
  width: 100%;
  overflow: hidden;
}

#app {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  height: 100%;
  width: 100%;
  overflow: hidden;
}


/* 主应用容器 */
.app-container {
  height: 100vh;
  width: 100vw;
  overflow: hidden;
  padding: 0;
  margin: 0;
}

/* 侧边栏样式 */
.sidebar {
  width: 200px;
  flex:0 0 auto;
  height: 100%;
  background-color: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 0;
  margin: 0;
}

.sidebar-menu {
  height: 100%;
  border-right: none;
}

/* 主内容区域 */
.main-section {
  flex:1;
  display: flex;
  flex-direction: column;
  padding: 0;
  margin: 0;
}

/* 头部样式 */
.app-header {
  background-color: #409EFF;
  color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  height: 60px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.header-title {
  font-size: 20px;
  font-weight: bold;
}

.header-user .el-dropdown-link {
  color: white;
  cursor: pointer;
  display: flex;
  align-items: center;
}

/* 主内容区域 */
.app-main {
  background-color: #f5f7fa;
  padding: 20px;
  height: calc(100% - 60px);
  overflow-y: auto;
}

/* 响应式设计 */
@media screen and (max-width: 768px) {
  .sidebar {
    width: 64px !important;
  }
  
  .main-section {
    width: calc(100% - 64px);
  }
  
  .header-title {
    font-size: 16px;
  }
}
</style>
