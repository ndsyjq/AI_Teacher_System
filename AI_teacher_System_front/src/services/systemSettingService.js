import api from './api';

// 系统设置服务
const systemSettingService = {
  // 保存系统设置
  saveSettings(settingsData) {
    return api.post('/api/system-setting', settingsData);
  },
  
  // 重置系统设置
  resetSettings() {
    return api.post('/api/system-setting/reset');
  },
  
  // 导出系统设置
  exportSettings() {
    return api.get('/api/system-setting/export', {
      responseType: 'blob'
    });
  },
  
  // 导入系统设置
  importSettings(formData) {
    return api.post('/api/system-setting/import', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
  },
  
  // 立即备份
  backupNow() {
    return api.post('/api/system-setting/backup');
  },
  
  // 测试通知
  testNotification(type) {
    return api.post('/api/system-setting/test-notification', { type });
  },
  
  // 获取系统设置
  getSettings() {
    return api.get('/api/system-setting');
  }
};

export default systemSettingService;