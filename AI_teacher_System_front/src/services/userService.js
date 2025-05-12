import api from './api';

// 用户认证与管理服务
const userService = {
  // 登录
  login(username, password) {
    return api.post('/api/auth/login', { username, password });
  },
  
  // 注册
  register(userData) {
    return api.post('/api/auth/register', userData);
  },
  
  // 退出登录
  logout() {
    return api.post('/api/auth/logout');
  },
  
  // 获取用户信息
  getUserInfo() {
    return api.get('/api/users/current');
  },
  
  // 更新用户信息
  updateUserInfo(userData) {
    return api.put('/api/users', userData);
  },
  
  // 修改密码
  changePassword(passwordData) {
    return api.post(`/api/users/change-password?oldPassword=${passwordData.currentPassword}&newPassword=${passwordData.newPassword}`);
  },
  
  // 上传头像
  uploadAvatar(formData) {
    return api.post('/api/users/update-avatar', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
  }
};

export default userService;