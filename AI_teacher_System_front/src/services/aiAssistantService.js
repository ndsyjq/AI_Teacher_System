import api from './api';

// AI助手服务
const aiAssistantService = {
  // 发送用户消息
  sendMessage(content) {
    // 确保传递的是普通字符串，而不是响应式对象
    const messageContent = typeof content === 'object' && content.value !== undefined ? content.value : content;
    return api.post('/api/ai-assistant/send-message', { content: messageContent }, { timeout: 20000 });
  },
  
  // 获取历史对话记录
  getHistoryMessages() {
    return api.get('/api/ai-assistant/history');
  }
};

export default aiAssistantService;