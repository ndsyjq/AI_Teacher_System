import api from './api';
import {reactive, ref} from "vue";

// AI助手服务
const aiAssistantService = {
  // 发送用户消息
  sendMessage(content) {
    // 确保传递的是普通字符串，而不是响应式对象
    return api.post('/api/ai/ask', { query: `${content}`,
      stream:"false",
      conversation_id: "",
      user: 'neil' });
  },
  
  // 获取历史对话记录
  getHistoryMessages() {
    return api.get('/api/ai-assistant/history');
  }
};

export default aiAssistantService;