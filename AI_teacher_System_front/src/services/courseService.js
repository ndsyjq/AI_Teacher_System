import api from './api';

// 课程管理服务
const courseService = {
  // 添加课程
  addCourse(courseData) {
    return api.post('/api/courses', courseData);
  },
  
  // 获取课程列表
  getCourses() {
    return api.get('/api/courses');
  },
  
  // 获取课程详情
  getCourseDetail(courseId) {
    return api.get(`/api/courses/${courseId}`);
  },
  
  // 编辑课程
  updateCourse(courseId, courseData) {
    return api.put(`/api/courses/${courseId}`, courseData);
  },
  
  // 删除课程
  deleteCourse(courseId) {
    return api.delete(`/api/courses/${courseId}`);
  },
  
  // 获取课程进度概览数据
  getProgressOverview() {
    return api.get('/api/courses/progress-overview');
  },
  
  // 获取课程学科分布数据
  getSubjectDistribution() {
    return api.get('/api/courses/subject-distribution');
  },
  
  // 获取学习进度趋势数据
  getProgressTrend() {
    return api.get('/api/courses/progress-trend');
  }
};

export default courseService;