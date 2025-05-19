import api from './api';

// 用户认证与管理服务
const userService = {
  // 登录
  login(username, password) {
    return api.post('/api/user/login', `username=${encodeURIComponent(username)}&password=${encodeURIComponent(password)}`, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded' // 需要修改Content-Type
      }
    });
  },
  
  // 注册
  register(user) {
    return api.post('/api/user/register', user);
  },
  
  // 退出登录
  logout() {
    return api.post('/api/user/logout');
  },
  
  // 获取用户信息
  getUserInfo() {
    return api.get('/api/user/userInfo');
  },
  
  // 更新用户信息
  updateUserInfo(userData) {
    return api.put('/api/user/update', userData);
  },
  
  // 修改密码
  changePassword(passwordData) {
    return api.post(`/api/user/updatePwd`, passwordData);
  },
  
  // 上传头像
  uploadAvatar(avatarUrl) {
    return api.post('/api/user/updateAvatar',  avatarUrl );
  }
};

export default userService;