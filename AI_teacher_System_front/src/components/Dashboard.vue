<script setup>
import { ref, reactive, inject } from 'vue';
import { useRouter } from 'vue-router';

// 获取路由实例
const router = useRouter();

// 进度条格式化函数
const format = (percentage) => {
  return `${percentage}%`;
};

// 模拟最近活动数据
const recentActivities = ref([
  { date: '2023-06-01', content: '完成了《高中数学必修一》第1章的备课' },
  { date: '2023-06-02', content: '完成了《高中数学必修一》第2章的备课' },
  { date: '2023-06-03', content: '完成了《高中数学必修一》第3章的备课' },
  { date: '2023-06-04', content: '完成了《高中数学必修一》第4章的备课' },
]);

// 获取父组件的activeMenu变量
const emitter = inject('emitter', null);

// 跳转到课程管理页面
const navigateToCourses = () => {
  if (emitter) {
    emitter.emit('change-menu', 'courses');
  } else {
    // 如果没有注入emitter，可以使用事件总线或其他方式通知父组件
    console.log('跳转到课程管理页面');
  }
};

// 跳转到备课中心页面
const navigateToLessonPlanner = () => {
  if (emitter) {
    emitter.emit('change-menu', 'lesson-plan');
  } else {
    console.log('跳转到备课中心页面');
  }
};

// 跳转到AI助手页面
const navigateToAIAssistant = () => {
  if (emitter) {
    emitter.emit('change-menu', 'ai-assistant');
  } else {
    console.log('跳转到AI助手页面');
  }
};
</script>

<template>
  <div class="dashboard">
    <h2 class="dashboard-title">欢迎使用AI教师智慧备课系统</h2>

    <!-- 进度卡片 -->
    <el-row :gutter="20">
      <el-col :xs="24" :sm="12" :md="8" :lg="8" :xl="8">
        <el-card class="dashboard-card">
          <template #header>
            <div class="card-header">
              <span>我的课程</span>
              <el-button class="button" text @click="navigateToCourses">查看全部</el-button>
            </div>
          </template>
          <div class="dashboard-card-content">
            <p>当前共有 5 门课程</p>
            <el-progress :percentage="70" :format="format" />
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :md="8" :lg="8" :xl="8">
        <el-card class="dashboard-card">
          <template #header>
            <div class="card-header">
              <span>备课进度</span>
              <el-button class="button" text @click="navigateToLessonPlanner">详情</el-button>
            </div>
          </template>
          <div class="dashboard-card-content">
            <p>本周已完成 3/5 课时</p>
            <el-progress :percentage="60" :format="format" />
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :md="8" :lg="8" :xl="8">
        <el-card class="dashboard-card">
          <template #header>
            <div class="card-header">
              <span>AI助手</span>
              <el-button class="button" text @click="navigateToAIAssistant">立即使用</el-button>
            </div>
          </template>
          <div class="dashboard-card-content">
            <p>本周已使用 12 次</p>
            <el-progress :percentage="40" :format="format" />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 最近活动 -->
    <el-divider content-position="center">最近活动</el-divider>
    <el-timeline>
      <el-timeline-item
        v-for="(activity, index) in recentActivities"
        :key="index"
        :timestamp="activity.date"
      >
        {{ activity.content }}
      </el-timeline-item>
    </el-timeline>
  </div>
</template>

<style scoped>
.dashboard {
  width: 100%;
  padding: 20px;
  box-sizing: border-box;
  background-color: #f5f7fa; /* Light background to fill space */
}

.dashboard-title {
  color:black;
  text-align: center;
  font-size: 24px;
  margin-bottom: 30px;
}

.dashboard-card {
  background-color: #ffffff;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  padding: 20px;
  transition: transform 0.3s ease-in-out;
}

.dashboard-card:hover {
  transform: translateY(-5px);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
}

.dashboard-card-content {
  padding: 10px 0;
}

.el-row {
  margin-bottom: 20px;
}

.el-divider {
  margin-top: 40px;
  margin-bottom: 20px;
  font-weight: bold;
}

.el-timeline-item {
  font-size: 14px;
}

.el-button {
  font-size: 12px;
}
</style>
