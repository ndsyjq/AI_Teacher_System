<script setup>
import { ref, reactive, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { systemSettingService } from '../services';

// 设置表单数据
const settingsForm = reactive({
  theme: 'light',
  fontSize: 'medium',
  language: 'zh-cn',
  notifications: {
    email: true,
    browser: true,
    sound: true
  },
  autoSave: true,
  saveInterval: 5,
  // 新增数据备份设置
  dataBackup: {
    enabled: false,
    frequency: 'weekly',
    keepCount: 3
  },
  // 新增隐私设置
  privacy: {
    shareUsageData: true,
    allowCookies: true
  },
  // 新增AI助手设置
  aiAssistant: {
    enabled: true,
    autoSuggest: true,
    voiceControl: false
  }
});

// 原始设置，用于检测变更
const originalSettings = ref({});

// 加载中状态
const loading = ref(false);

// 获取系统设置
const fetchSettings = async () => {
  try {
    loading.value = true;
    try {
      const response = await systemSettingService.getSettings();
      Object.assign(settingsForm, response.data);
      // 保存原始设置用于比较
      originalSettings.value = JSON.parse(JSON.stringify(settingsForm));
      ElMessage.success('系统设置加载成功');
    } catch (error) {
      console.error('加载系统设置失败:', error);
      ElMessage.error(error.message || '加载系统设置失败');
    }
  } catch (error) {
    console.error('加载系统设置失败:', error);
    ElMessage.error('加载系统设置失败，请稍后再试');
  } finally {
    loading.value = false;
  }
};

// 组件挂载时加载设置
onMounted(() => {
  fetchSettings();
});

// 主题选项
const themeOptions = [
  { label: '浅色主题', value: 'light' },
  { label: '深色主题', value: 'dark' },
  { label: '跟随系统', value: 'system' }
];

// 字体大小选项
const fontSizeOptions = [
  { label: '小', value: 'small' },
  { label: '中', value: 'medium' },
  { label: '大', value: 'large' }
];

// 语言选项
const languageOptions = [
  { label: '简体中文', value: 'zh-cn' },
  { label: '繁体中文', value: 'zh-tw' },
  { label: '英文', value: 'en' }
];

// 备份频率选项
const backupFrequencyOptions = [
  { label: '每天', value: 'daily' },
  { label: '每周', value: 'weekly' },
  { label: '每月', value: 'monthly' }
];

// 是否有未保存的更改
const hasUnsavedChanges = ref(false);

// 当前激活的标签页
const activeTab = ref('interface');

// 检查是否有未保存的更改
const checkUnsavedChanges = () => {
  // 深度比较当前设置和原始设置
  const currentSettingsStr = JSON.stringify(settingsForm);
  const originalSettingsStr = JSON.stringify(originalSettings.value);
  hasUnsavedChanges.value = currentSettingsStr !== originalSettingsStr;
};

// 监听设置变化
const watchSettings = () => {
  // 这里可以使用Vue的watch API，但为简化示例，我们在每个表单变化时手动调用
  checkUnsavedChanges();
};

// 保存设置
const saveSettings = () => {
  // 显示确认对话框
  ElMessageBox.confirm(
    '确定要保存当前设置吗？这将覆盖之前的设置。',
    '保存设置',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'info'
    }
  )
    .then(async () => {
      try {
        loading.value = true;
        try {
          const response = await systemSettingService.saveSettings(settingsForm);
          // 更新原始设置
          originalSettings.value = JSON.parse(JSON.stringify(settingsForm));
          hasUnsavedChanges.value = false;
          
          // 应用主题设置
          applyTheme(settingsForm.theme);
          
          // 应用字体大小
          applyFontSize(settingsForm.fontSize);
          
          ElMessage.success('系统设置保存成功');
        } catch (error) {
          console.error('保存系统设置失败:', error);
          ElMessage.error(error.message || '保存系统设置失败');
        }
      } catch (error) {
        console.error('保存系统设置失败:', error);
        ElMessage.error('保存系统设置失败，请稍后再试');
      } finally {
        loading.value = false;
      }
    })
    .catch(() => {
      // 用户取消
    });
};

// 重置设置
const resetSettings = () => {
  ElMessageBox.confirm(
    '确定要恢复默认设置吗？所有自定义设置将被重置。',
    '恢复默认设置',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  )
    .then(async () => {
      try {
        loading.value = true;
        const response = await systemSettingService.resetSettings();
        if (response.code === 200) {
          // 更新本地设置为后端返回的默认设置
          Object.assign(settingsForm, response.data);
          
          // 应用默认主题
          applyTheme(settingsForm.theme);
          
          // 应用默认字体大小
          applyFontSize(settingsForm.fontSize);
          
          // 更新原始设置
          originalSettings.value = JSON.parse(JSON.stringify(settingsForm));
          
          checkUnsavedChanges();
          ElMessage.success('已重置为默认设置');
        } else {
          ElMessage.error(response.message || '重置设置失败');
        }
      } catch (error) {
        console.error('重置设置失败:', error);
        ElMessage.error('重置设置失败，请稍后再试');
      } finally {
        loading.value = false;
      }
    })
    .catch(() => {
      // 用户取消
    });
};

// 导出设置
const exportSettings = async () => {
  try {
    loading.value = true;
    const response = await systemSettingService.exportSettings();
    
    // 创建下载链接
    const blob = new Blob([response.data], { type: 'application/json' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `ai-teacher-settings-${new Date().toISOString().split('T')[0]}.json`;
    document.body.appendChild(a);
    a.click();
    
    // 清理
    setTimeout(() => {
      document.body.removeChild(a);
      URL.revokeObjectURL(url);
    }, 0);
    
    ElMessage.success('设置已导出');
  } catch (error) {
    console.error('导出设置失败:', error);
    ElMessage.error('导出设置失败，请稍后再试');
  } finally {
    loading.value = false;
  }
};

// 导入设置
const importSettings = (event) => {
  const file = event.target.files[0];
  if (!file) return;
  
  // 确认导入
  ElMessageBox.confirm(
    '确定要导入这些设置吗？当前设置将被覆盖。',
    '导入设置',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  )
    .then(async () => {
      try {
        loading.value = true;
        
        // 创建FormData对象
        const formData = new FormData();
        formData.append('file', file);
        
        const response = await systemSettingService.importSettings(formData);
        if (response.code === 200) {
          // 更新本地设置为导入的设置
          Object.assign(settingsForm, response.data);
          
          // 应用主题
          applyTheme(settingsForm.theme);
          
          // 应用字体大小
          applyFontSize(settingsForm.fontSize);
          
          // 更新原始设置
          originalSettings.value = JSON.parse(JSON.stringify(settingsForm));
          
          checkUnsavedChanges();
          ElMessage.success('设置已导入');
        } else {
          ElMessage.error(response.message || '导入设置失败');
        }
      } catch (error) {
        console.error('导入设置失败:', error);
        ElMessage.error('导入设置失败，请稍后再试');
      } finally {
        loading.value = false;
        // 重置文件输入
        event.target.value = '';
      }
    })
    .catch(() => {
      // 用户取消
      event.target.value = '';
    });
};

// 应用主题
const applyTheme = (theme) => {
  // 实际应用中，这里可以修改根元素的class或者CSS变量
  document.documentElement.setAttribute('data-theme', theme);
  
  // 模拟主题切换效果
  if (theme === 'dark') {
    document.documentElement.style.setProperty('--el-bg-color', '#1f1f1f');
    document.documentElement.style.setProperty('--el-text-color-primary', '#ffffff');
  } else {
    document.documentElement.style.setProperty('--el-bg-color', '#ffffff');
    document.documentElement.style.setProperty('--el-text-color-primary', '#303133');
  }
};

// 应用字体大小
const applyFontSize = (size) => {
  // 实际应用中，这里可以修改根元素的font-size
  const sizeMap = {
    small: '14px',
    medium: '16px',
    large: '18px'
  };
  
  document.documentElement.style.fontSize = sizeMap[size] || '16px';
};

// 立即备份
const backupNow = async () => {
  try {
    loading.value = true;
    ElMessage({
      message: '正在进行数据备份...',
      type: 'info'
    });
    
    const response = await systemSettingService.backupNow();
    if (response.code === 200) {
      ElMessage.success('数据备份完成');
    } else {
      ElMessage.error(response.message || '数据备份失败');
    }
  } catch (error) {
    console.error('数据备份失败:', error);
    ElMessage.error('数据备份失败，请稍后再试');
  } finally {
    loading.value = false;
  }
};

// 测试通知
const testNotification = async (type) => {
  try {
    loading.value = true;
    
    // 调用后端API测试通知
    const response = await systemSettingService.testNotification(type);
    
    if (response.code === 200) {
      if (type === 'email') {
        ElMessage.success('测试邮件已发送到您的邮箱');
      } else if (type === 'browser') {
        // 请求浏览器通知权限
        if (Notification && Notification.permission !== 'granted') {
          Notification.requestPermission().then(permission => {
            if (permission === 'granted') {
              new Notification('测试通知', {
                body: '这是一条测试浏览器通知'
              });
            }
          });
        } else if (Notification) {
          new Notification('测试通知', {
            body: '这是一条测试浏览器通知'
          });
        } else {
          ElMessage.info('您的浏览器不支持通知功能');
        }
      } else if (type === 'sound') {
        // 播放提示音
        const audio = new Audio('/notification.mp3');
        audio.play().catch(error => {
          ElMessage.error('无法播放提示音');
        });
      }
    } else {
      ElMessage.error(response.message || `${type}通知测试失败`);
    }
  } catch (error) {
    console.error('通知测试失败:', error);
    ElMessage.error('通知测试失败，请稍后再试');
  } finally {
    loading.value = false;
  }
};

// 组件挂载时
onMounted(() => {
  // 保存原始设置
  originalSettings.value = JSON.parse(JSON.stringify(settingsForm));
  
  // 应用当前主题
  applyTheme(settingsForm.theme);
  
  // 应用当前字体大小
  applyFontSize(settingsForm.fontSize);
});
</script>

<template>
  <div class="settings-container">
    <div class="settings-header">
      <h2 class="settings-title">系统设置</h2>
      <p class="settings-subtitle">自定义您的系统偏好设置</p>
      
      <div v-if="hasUnsavedChanges" class="unsaved-changes-alert">
        <el-alert
          title="您有未保存的更改"
          type="warning"
          :closable="false"
          show-icon
        />
      </div>
    </div>

    <el-card class="settings-card">
      <el-tabs v-model="activeTab" type="border-card" @tab-change="watchSettings">
        <el-tab-pane name="interface" label="界面设置">
          <el-form :model="settingsForm" label-width="100px" @change="watchSettings">
            <el-form-item label="主题">
              <el-radio-group v-model="settingsForm.theme" @change="watchSettings">
                <el-radio v-for="option in themeOptions" :key="option.value" :label="option.value">
                  {{ option.label }}
                </el-radio>
              </el-radio-group>
              <div class="theme-preview">
                <div class="theme-sample" :class="settingsForm.theme">
                  <div class="theme-sample-header"></div>
                  <div class="theme-sample-content"></div>
                </div>
              </div>
            </el-form-item>
            
            <el-form-item label="字体大小">
              <el-select v-model="settingsForm.fontSize" placeholder="请选择字体大小" @change="watchSettings">
                <el-option
                  v-for="option in fontSizeOptions"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
                ></el-option>
              </el-select>
              <div class="font-size-preview">
                <span :class="`font-${settingsForm.fontSize}`">字体预览</span>
              </div>
            </el-form-item>
            
            <el-form-item label="语言">
              <el-select v-model="settingsForm.language" placeholder="请选择语言" @change="watchSettings">
                <el-option
                  v-for="option in languageOptions"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        
        <el-tab-pane name="notification" label="通知设置">
          <el-form :model="settingsForm" label-width="120px" @change="watchSettings">
            <el-form-item label="邮件通知">
              <el-switch v-model="settingsForm.notifications.email" @change="watchSettings" />
              <el-button 
                v-if="settingsForm.notifications.email" 
                size="small" 
                type="primary" 
                style="margin-left: 10px;"
                @click="testNotification('email')"
              >
                测试
              </el-button>
            </el-form-item>
            
            <el-form-item label="浏览器通知">
              <el-switch v-model="settingsForm.notifications.browser" @change="watchSettings" />
              <el-button 
                v-if="settingsForm.notifications.browser" 
                size="small" 
                type="primary" 
                style="margin-left: 10px;"
                @click="testNotification('browser')"
              >
                测试
              </el-button>
            </el-form-item>
            
            <el-form-item label="声音提醒">
              <el-switch v-model="settingsForm.notifications.sound" @change="watchSettings" />
              <el-button 
                v-if="settingsForm.notifications.sound" 
                size="small" 
                type="primary" 
                style="margin-left: 10px;"
                @click="testNotification('sound')"
              >
                测试
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        
        <el-tab-pane name="backup" label="数据备份">
          <el-form :model="settingsForm" label-width="120px" @change="watchSettings">
            <el-form-item label="启用自动备份">
              <el-switch v-model="settingsForm.dataBackup.enabled" @change="watchSettings" />
            </el-form-item>
            
            <template v-if="settingsForm.dataBackup.enabled">
              <el-form-item label="备份频率">
                <el-select v-model="settingsForm.dataBackup.frequency" placeholder="请选择备份频率" @change="watchSettings">
                  <el-option
                    v-for="option in backupFrequencyOptions"
                    :key="option.value"
                    :label="option.label"
                    :value="option.value"
                  ></el-option>
                </el-select>
              </el-form-item>
              
              <el-form-item label="保留备份数量">
                <el-input-number 
                  v-model="settingsForm.dataBackup.keepCount" 
                  :min="1" 
                  :max="10" 
                  @change="watchSettings"
                />
              </el-form-item>
              
              <el-form-item>
                <el-button type="success" @click="backupNow">
                  <el-icon><Download /></el-icon> 立即备份
                </el-button>
              </el-form-item>
            </template>
          </el-form>
        </el-tab-pane>
        
        <el-tab-pane name="privacy" label="隐私设置">
          <el-form :model="settingsForm" label-width="200px" @change="watchSettings">
            <el-form-item label="分享使用数据以改进服务">
              <el-switch v-model="settingsForm.privacy.shareUsageData" @change="watchSettings" />
              <div class="setting-description">
                允许系统收集匿名使用数据，帮助我们改进产品体验
              </div>
            </el-form-item>
            
            <el-form-item label="允许使用Cookie">
              <el-switch v-model="settingsForm.privacy.allowCookies" @change="watchSettings" />
              <div class="setting-description">
                Cookie用于保存您的登录状态和个人偏好设置
              </div>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        
        <el-tab-pane name="ai" label="AI助手设置">
          <el-form :model="settingsForm" label-width="120px" @change="watchSettings">
            <el-form-item label="启用AI助手">
              <el-switch v-model="settingsForm.aiAssistant.enabled" @change="watchSettings" />
            </el-form-item>
            
            <template v-if="settingsForm.aiAssistant.enabled">
              <el-form-item label="自动建议">
                <el-switch v-model="settingsForm.aiAssistant.autoSuggest" @change="watchSettings" />
                <div class="setting-description">
                  AI助手将根据您的操作自动提供建议
                </div>
              </el-form-item>
              
              <el-form-item label="语音控制">
                <el-switch v-model="settingsForm.aiAssistant.voiceControl" @change="watchSettings" />
                <div class="setting-description">
                  启用语音命令控制AI助手（需要麦克风权限）
                </div>
              </el-form-item>
            </template>
          </el-form>
        </el-tab-pane>
        
        <el-tab-pane name="other" label="其他设置">
          <el-form :model="settingsForm" label-width="120px" @change="watchSettings">
            <el-form-item label="自动保存">
              <el-switch v-model="settingsForm.autoSave" @change="watchSettings" />
            </el-form-item>
            
            <el-form-item label="保存间隔" v-if="settingsForm.autoSave">
              <el-input-number 
                v-model="settingsForm.saveInterval" 
                :min="1" 
                :max="30" 
                @change="watchSettings"
              />
              <span class="input-suffix">分钟</span>
            </el-form-item>
            
            <el-divider content-position="left">导入/导出设置</el-divider>
            
            <el-form-item>
              <el-button type="primary" @click="exportSettings">
                <el-icon><Download /></el-icon> 导出设置
              </el-button>
              
              <el-upload
                class="inline-upload"
                action="#"
                :auto-upload="false"
                :show-file-list="false"
                :on-change="importSettings"
                accept=".json"
              >
                <el-button type="primary">
                  <el-icon><Upload /></el-icon> 导入设置
                </el-button>
              </el-upload>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
      
      <div class="settings-actions">
        <el-button type="primary" @click="saveSettings" :disabled="!hasUnsavedChanges" :loading="loading">
          保存设置
        </el-button>
        <el-button @click="resetSettings" :loading="loading">恢复默认</el-button>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.settings-container {
  padding: 20px;
}

.settings-header {
  margin-bottom: 30px;
}

.settings-title {
  font-size: 24px;
  color: #303133;
  margin-bottom: 10px;
}

.settings-subtitle {
  font-size: 14px;
  color: #909399;
}

.settings-card {
  margin-bottom: 20px;
}

.settings-actions {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.input-suffix {
  margin-left: 10px;
  color: #606266;
}

.unsaved-changes-alert {
  margin-top: 15px;
}

.theme-preview {
  margin-top: 10px;
}

.theme-sample {
  width: 150px;
  height: 100px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
}

.theme-sample.light {
  background-color: #ffffff;
}

.theme-sample.dark {
  background-color: #1f1f1f;
}

.theme-sample.system {
  background: linear-gradient(to right, #ffffff 50%, #1f1f1f 50%);
}

.theme-sample-header {
  height: 20px;
  background-color: #409eff;
}

.theme-sample-content {
  height: 80px;
  padding: 10px;
}

.theme-sample.light .theme-sample-content {
  color: #303133;
}

.theme-sample.dark .theme-sample-content {
  color: #ffffff;
}

.font-size-preview {
  margin-top: 10px;
}

.font-small {
  font-size: 14px;
}

.font-medium {
  font-size: 16px;
}

.font-large {
  font-size: 18px;
}

.setting-description {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
}

.inline-upload {
  display: inline-block;
  margin-left: 10px;
}
</style>