import api from './api';

// 备课计划服务
const lessonService = {
  // 保存备课计划
  saveLessonPlan(lessonData) {
    return api.post('/api/lesson-plans', lessonData);
  },
  
  // AI智能生成备课内容
  generateAIContent(lessonData) {
    return api.post('/api/lesson-plans/generate', lessonData);
  },
  
  // 编辑备课计划
  updateLessonPlan(lessonId, lessonData) {
    return api.put(`/api/lesson-plans/${lessonId}`, lessonData);
  },
  
  // AI优化备课内容
  optimizeLessonPlan(lessonId, lessonData) {
    return api.post(`/api/lesson-plans/${lessonId}/optimize`, lessonData);
  },
  
  // 获取备课详情
  getLessonDetail(lessonId) {
    return api.get(`/api/lesson-plans/${lessonId}`);
  },
  
  // 获取备课列表
  getLessonPlans() {
    return api.get('/api/lesson-plans');
  }
};

export default lessonService;