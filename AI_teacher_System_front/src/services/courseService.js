import api from './api';

// 课程管理服务
export const courseService = {
  // 添加课程
  addCourse(courseData,userName) {
    return api.post(`/api/courses/add?username=${userName}`, courseData);
  },
  // 获取课程列表
  getCourses() {
    return api.get('/api/courses/selectAll');
  },
  
  // 获取课程详情
  getCourseDetail(courseId) {
    return api.get(`/api/courses/select?id=${courseId}`);
  },
  
  // 编辑课程
  updateCourse( courseData) {
    return api.put(`/api/courses/update`, courseData);
  },
  
  // 删除课程
  deleteCourse(courseId) {
    return api.delete(`/api/courses/delete?id=${courseId}`);
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

