import api from './api';

// 备课计划服务
const lessonService = {
  // 保存备课计划
  saveLessonPlan(lessonData,userName) {
    return api.post(`/api/lesson/add`, lessonData);
  },
  
  // AI智能生成备课内容
  generateAIContent(lessonData) {
    return api.post('/api/lesson-plans/generate', lessonData);
  },
  
  // 编辑备课计划
  updateLessonPlan(lessonData) {
    return api.put(`/api/lesson/update`, lessonData);
  },
  
  // AI优化备课内容
  optimizeLessonPlan(lessonId, lessonData) {
    return api.post(`/api/lesson-plans/${lessonId}/optimize`, lessonData);
  },
  deleteLessonPlan(lessonId) {
    return api.delete(`/api/lesson/delete?id=${lessonId}`);
  },
  // 获取备课详情
  getLessonDetail(lessonId) {
    return api.get(`/api/lesson/selectById?id=${lessonId}`);
  },
  // 获取备课列表
  getLessonPlans() {
    return api.get('/api/lesson/selectAll');
  }
};

export default lessonService;