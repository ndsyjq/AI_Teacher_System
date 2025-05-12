import api from './api';

// 资源库服务
const resourceService = {
  // 上传资源
  uploadResource(formData) {
    return api.post('/api/resources/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
  },
  
  // 下载资源
  downloadResource(resourceId) {
    return api.get(`/api/resources/download/${resourceId}`, {
      responseType: 'blob'
    });
  },
  
  // 预览资源
  previewResource(resourceId) {
    return api.get(`/api/resources/preview/${resourceId}`);
  },
  
  // 获取资源列表
  getResources(params) {
    return api.get('/api/resources', { params });
  },
  
  // 收藏资源
  favoriteResource(resourceId) {
    return api.post(`/api/resources/favorite/${resourceId}`);
  },
  
  // 取消收藏
  unfavoriteResource(resourceId) {
    return api.delete(`/api/resources/favorite/${resourceId}`);
  },
  
  // 评论资源
  commentResource(resourceId, commentData) {
    return api.post(`/api/resources/comment/${resourceId}`, commentData);
  },
  
  // 获取资源评论
  getResourceComments(resourceId) {
    return api.get(`/api/resources/comments/${resourceId}`);
  }
};

export default resourceService;