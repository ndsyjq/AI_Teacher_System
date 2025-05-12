<template>
  <div class="main-container">
    <div class="left-space"></div>
    <div class="ai-assistant">
      <h2>AI助手</h2>
      <el-card>
        <div class="ai-chat-container">
          <div class="ai-chat-messages">
            <div 
              v-for="(message, index) in messages" 
              :key="index"
              :class="['ai-message', message.type === 'user' ? 'user-message' : '']">
              <el-avatar 
                :icon="message.type === 'user' ? 'UserFilled' : 'Service'"
                :class="message.type === 'user' ? 'user-avatar' : 'ai-avatar'"
              />
              <div class="message-content" :class="message.type === 'user' ? 'user-content' : 'ai-content'">
                {{ message.content }}
              </div>
            </div>
          </div>
          <div class="ai-chat-input">
            <el-input
              v-model="userInput"
              type="textarea"
              :rows="3"
              placeholder="请输入您的问题..."
              @keyup.enter.ctrl="sendMessage"
            />
            <div class="input-actions">
              <span class="input-tip">按Ctrl+Enter发送</span>
              <el-button type="primary" @click="sendMessage">发送</el-button>
            </div>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import {aiAssistantService} from '@/services/';

// 清理AI回复内容，移除</think>标签前的内容
const cleanAIResponse = (content) => {
  if (typeof content !== 'string') return content;
  
  const thinkTagIndex = content.indexOf('</think>');
  if (thinkTagIndex !== -1) {
    return content.substring(thinkTagIndex + 8).trim(); // 8是</think>的长度
  }
  return content;
};

// 聊天消息记录
const messages = ref([
  {
    type: 'ai',
    content: '您好，我是您的AI助教，有什么可以帮助您的吗？'
  }
]);

// 用户输入的消息
const userInput = ref('');

// 发送消息
  const sendMessage = async () => {
    if (!userInput.value.trim()) return;
    
    const userMessage = userInput.value;
    
    // 添加用户消息
    messages.value.push({
      type: 'user',
      content: userMessage
    });

    // 清空输入框
    userInput.value = '';
    
    // 添加一个临时的AI消息，表示正在思考
    messages.value.push({
      type: 'ai',
      content: '思考中...',
      isLoading: true
    });
    
    try {
      // 发送消息到后端并获取回复
      const response = await aiAssistantService.sendMessage(userMessage);
      
      // 更新最后一条AI消息
      const lastIndex = messages.value.length - 1;
      messages.value[lastIndex] = {
        type: 'ai',
        content: cleanAIResponse(response.data) || '抱歉，我暂时无法回答这个问题。',
        isLoading: false
      };
    } catch (error) {
      // 处理错误情况
      const lastIndex = messages.value.length - 1;
      messages.value[lastIndex] = {
        type: 'ai',
        content: '抱歉，发生了一些错误，请稍后再试。',
        isLoading: false
      };
      console.error('AI助手请求错误:', error);
    }
  
  // 已在上面清空输入框
};
</script>

<style scoped>
.main-container {
  display: flex;
  width: 100%;
}

.left-space {
  width: 20%; /* 左侧留出20%的空间 */
}

.ai-assistant {
  width: 80%; /* 聊天界面占据右侧80%的空间 */
}

.ai-chat-container {
  display: flex;
  flex-direction: column;
  height: 500px;
}

.ai-chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 10px;
  background-color: #f9f9f9;
  border-radius: 4px;
  margin-bottom: 10px;
}

.ai-message {
  display: flex;
  margin-bottom: 15px;
  align-items: flex-start;
}

.user-message {
  flex-direction: row-reverse;
}

.message-content {
  padding: 10px;
  border-radius: 4px;
  max-width: 70%;
  word-break: break-word;
}

.ai-content {
  background-color: #e6f7ff;
  margin-left: 10px;
}

.user-content {
  background-color: #95de64;
  margin-right: 10px;
  color: #fff;
}

.ai-chat-input {
  margin-top: auto;
}

.input-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
}

.input-tip {
  color: #999;
  font-size: 12px;
}
</style>