<script setup>
import { ref, reactive, onMounted } from 'vue';
import * as echarts from 'echarts';
// 引入echarts，需要先安装：npm install echarts --save

// 备课表单数据
const lessonForm = reactive({
  name: '',
  chapter: '',
  objectives: '',
  content: ''
});

// 模拟备课数据
const lessonPlans = ref([
  {
    id: 1,
    name: '高中数学必修一',
    chapter: '第一章 集合与函数概念',
    status: '已完成',
    date: '2023-06-01',
    objectives: '1. 理解集合的基本概念\n2. 掌握集合的运算\n3. 理解函数的概念',
    content: '本节课主要讲解集合的基本概念和函数的定义。\n\n一、教学重点：\n1. 集合的表示方法\n2. 函数的定义域和值域\n\n二、教学难点：\n1. 集合之间的关系\n2. 函数的性质'
  },
  {
    id: 2,
    name: '高中物理必修二',
    chapter: '第三章 牛顿运动定律',
    status: '进行中',
    date: '2023-06-05',
    objectives: '1. 理解牛顿三大定律\n2. 掌握力学问题的分析方法',
    content: '本节课主要讲解牛顿三大定律及其应用。\n\n一、教学重点：\n1. 牛顿第二定律的应用\n2. 摩擦力的计算\n\n二、教学难点：\n1. 复杂力学系统的分析\n2. 多物体问题的处理'
  },
  {
    id: 3,
    name: '高中语文必修三',
    chapter: '第二单元 小说阅读',
    status: '未开始',
    date: '2023-06-10',
    objectives: '',
    content: ''
  }
]);

// 当前激活的标签页
const activeTab = ref('myLessons');

// 图表引用
const statusChartRef = ref(null);
let statusChart = null;

// 对话框可见性
const editDialogVisible = ref(false);
const aiOptimizeDialogVisible = ref(false);
const detailDialogVisible = ref(false);

// 当前选中的备课
const currentLesson = ref(null);

// 临时编辑表单
const editForm = reactive({
  name: '',
  chapter: '',
  objectives: '',
  content: '',
  status: ''
});

// 保存备课
const saveLessonPlan = () => {
  if (!lessonForm.name || !lessonForm.chapter) {
    // 这里应该有表单验证
    return;
  }
  
  // 模拟保存操作
  lessonPlans.value.push({
    id: lessonPlans.value.length + 1,
    name: lessonForm.name,
    chapter: lessonForm.chapter,
    objectives: lessonForm.objectives,
    content: lessonForm.content,
    status: '未开始',
    date: new Date().toISOString().split('T')[0]
  });
  
  // 重置表单
  lessonForm.name = '';
  lessonForm.chapter = '';
  lessonForm.objectives = '';
  lessonForm.content = '';
  
  // 切换到我的备课标签
  activeTab.value = 'myLessons';
};

// AI智能生成备课内容
const generateAIContent = () => {
  if (!lessonForm.name || !lessonForm.chapter) {
    // 这里应该有表单验证
    return;
  }
  
  // 模拟AI生成内容
  lessonForm.objectives = '1. 理解' + lessonForm.name + '中' + lessonForm.chapter + '的基本概念\n2. 掌握相关知识点的应用\n3. 能够解决相关的基础问题';
  lessonForm.content = '本节课主要讲解' + lessonForm.name + '中' + lessonForm.chapter + '的核心内容。\n\n一、教学重点：\n1. 基本概念的理解\n2. 典型例题分析\n\n二、教学难点：\n1. 知识点的融会贯通\n2. 解题思路的形成';
};

// 打开编辑对话框
const openEditDialog = (lesson) => {
  currentLesson.value = lesson;
  // 复制数据到编辑表单
  editForm.name = lesson.name;
  editForm.chapter = lesson.chapter;
  editForm.objectives = lesson.objectives || '';
  editForm.content = lesson.content || '';
  editForm.status = lesson.status;
  
  // 显示编辑对话框
  editDialogVisible.value = true;
};

// 保存编辑的备课
const saveEditedLesson = () => {
  if (!editForm.name || !editForm.chapter) {
    // 表单验证
    return;
  }
  
  // 更新备课数据
  if (currentLesson.value) {
    const index = lessonPlans.value.findIndex(plan => plan.id === currentLesson.value.id);
    if (index !== -1) {
      // 更新备课
      lessonPlans.value[index] = {
        ...lessonPlans.value[index],
        name: editForm.name,
        chapter: editForm.chapter,
        objectives: editForm.objectives,
        content: editForm.content,
        status: editForm.status
      };
      
      // 关闭对话框
      editDialogVisible.value = false;
      
      // 如果在分析页面，需要重新初始化图表
      if (activeTab.value === 'lessonAnalysis') {
        setTimeout(() => {
          initStatusChart();
        }, 100);
      }
    }
  }
};

// 打开AI优化对话框
const openAIOptimizeDialog = (lesson) => {
  currentLesson.value = lesson;
  
  // 复制数据到编辑表单
  editForm.name = lesson.name;
  editForm.chapter = lesson.chapter;
  editForm.objectives = lesson.objectives || '';
  editForm.content = lesson.content || '';
  
  // 显示AI优化对话框
  aiOptimizeDialogVisible.value = true;
  
  // 模拟AI优化过程
  setTimeout(() => {
    // 模拟AI优化结果
    if (editForm.objectives) {
      editForm.objectives += '\n4. 培养学生的创新思维和解决问题的能力';
    } else {
      editForm.objectives = '1. 理解' + lesson.name + '中' + lesson.chapter + '的基本概念\n2. 掌握相关知识点的应用\n3. 能够解决相关的基础问题\n4. 培养学生的创新思维和解决问题的能力';
    }
    
    if (editForm.content) {
      editForm.content += '\n\n三、教学方法：\n1. 启发式教学\n2. 小组讨论\n3. 案例分析';
    } else {
      editForm.content = '本节课主要讲解' + lesson.name + '中' + lesson.chapter + '的核心内容。\n\n一、教学重点：\n1. 基本概念的理解\n2. 典型例题分析\n\n二、教学难点：\n1. 知识点的融会贯通\n2. 解题思路的形成\n\n三、教学方法：\n1. 启发式教学\n2. 小组讨论\n3. 案例分析';
    }
  }, 1000);
};

// 保存AI优化的备课
const saveAIOptimizedLesson = () => {
  if (!editForm.name || !editForm.chapter) {
    // 表单验证
    return;
  }
  
  // 更新备课数据
  if (currentLesson.value) {
    const index = lessonPlans.value.findIndex(plan => plan.id === currentLesson.value.id);
    if (index !== -1) {
      // 更新备课
      lessonPlans.value[index] = {
        ...lessonPlans.value[index],
        objectives: editForm.objectives,
        content: editForm.content,
        status: '进行中' // AI优化后自动设为进行中
      };
      
      // 关闭对话框
      aiOptimizeDialogVisible.value = false;
      
      // 如果在分析页面，需要重新初始化图表
      if (activeTab.value === 'lessonAnalysis') {
        setTimeout(() => {
          initStatusChart();
        }, 100);
      }
    }
  }
};

// 查看备课详情
const viewLessonDetail = (lesson) => {
  currentLesson.value = lesson;
  detailDialogVisible.value = true;
};

// 初始化备课状态饼图
const initStatusChart = () => {
  if (statusChartRef.value) {
    statusChart = echarts.init(statusChartRef.value);
    
    // 统计各状态备课数量
    const statusCount = {
      '已完成': lessonPlans.value.filter(plan => plan.status === '已完成').length,
      '进行中': lessonPlans.value.filter(plan => plan.status === '进行中').length,
      '未开始': lessonPlans.value.filter(plan => plan.status === '未开始').length
    };
    
    const option = {
      title: {
        text: '备课状态分布',
        left: 'center'
      },
      tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b}: {c} ({d}%)'
      },
      legend: {
        orient: 'vertical',
        left: 'left',
        data: Object.keys(statusCount)
      },
      series: [
        {
          name: '备课状态',
          type: 'pie',
          radius: '50%',
          data: [
            { 
              value: statusCount['已完成'], 
              name: '已完成',
              itemStyle: { color: '#67C23A' }
            },
            { 
              value: statusCount['进行中'], 
              name: '进行中',
              itemStyle: { color: '#E6A23C' }
            },
            { 
              value: statusCount['未开始'], 
              name: '未开始',
              itemStyle: { color: '#909399' }
            }
          ],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    };
    
    statusChart.setOption(option);
    window.addEventListener('resize', statusChart.resize);
  }
};

// 监听标签页变化，初始化图表
const handleTabChange = (tab) => {
  if (tab === 'lessonAnalysis') {
    // 使用nextTick确保DOM已更新
    setTimeout(() => {
      initStatusChart();
    }, 100);
  }
};

// 组件挂载时初始化
onMounted(() => {
  if (activeTab.value === 'lessonAnalysis') {
    setTimeout(() => {
      initStatusChart();
    }, 100);
  }
});
</script>

<template>
  <div class="lesson-planner">
    <h2>备课中心</h2>
    <el-tabs v-model="activeTab" type="border-card" @tab-change="handleTabChange">
      <el-tab-pane name="myLessons" label="我的备课">
        <el-table :data="lessonPlans" style="width: 100%">
          <el-table-column prop="name" label="课程名称" />
          <el-table-column prop="chapter" label="章节" />
          <el-table-column prop="status" label="状态">
            <template #default="scope">
              <el-tag 
                :type="scope.row.status === '已完成' ? 'success' : scope.row.status === '进行中' ? 'warning' : 'info'"
              >
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="date" label="创建日期" />
          <el-table-column label="操作" width="280">
            <template #default="scope">
              <el-button size="small" @click="viewLessonDetail(scope.row)">查看</el-button>
              <el-button size="small" type="primary" @click="openEditDialog(scope.row)">编辑</el-button>
              <el-button size="small" type="success" @click="openAIOptimizeDialog(scope.row)">AI优化</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      
      <el-tab-pane name="createLesson" label="创建备课">
        <el-form :model="lessonForm" label-position="top">
          <el-form-item label="课程名称" required>
            <el-input v-model="lessonForm.name" placeholder="请输入课程名称" />
          </el-form-item>
          
          <el-form-item label="章节" required>
            <el-input v-model="lessonForm.chapter" placeholder="请输入章节" />
          </el-form-item>
          
          <el-form-item label="教学目标">
            <el-input 
              v-model="lessonForm.objectives" 
              type="textarea" 
              rows="3" 
              placeholder="请输入教学目标" 
            />
          </el-form-item>
          
          <el-form-item label="教学内容">
            <el-input 
              v-model="lessonForm.content" 
              type="textarea" 
              rows="5" 
              placeholder="请输入教学内容" 
            />
          </el-form-item>
          
          <el-form-item>
            <el-button type="primary" @click="saveLessonPlan">保存</el-button>
            <el-button type="success" @click="generateAIContent">AI智能生成</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      
      <el-tab-pane name="lessonAnalysis" label="备课分析">
        <div class="chart-container">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-card class="chart-card">
                <div ref="statusChartRef" class="chart"></div>
              </el-card>
            </el-col>
            
            <el-col :span="12">
              <el-card class="chart-card">
                <h3>备课状态统计</h3>
                <el-table :data="[{
                  status: '未开始',
                  count: lessonPlans.filter(p => p.status === '未开始').length,
                  color: '#909399'
                }, {
                  status: '进行中',
                  count: lessonPlans.filter(p => p.status === '进行中').length,
                  color: '#E6A23C'
                }, {
                  status: '已完成',
                  count: lessonPlans.filter(p => p.status === '已完成').length,
                  color: '#67C23A'
                }]">
                  <el-table-column prop="status" label="状态">
                    <template #default="scope">
                      <el-tag :color="scope.row.color" effect="plain">{{ scope.row.status }}</el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column prop="count" label="数量" />
                </el-table>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </el-tab-pane>
    </el-tabs>
    
    <!-- 编辑备课对话框 -->
    <el-dialog v-model="editDialogVisible" title="编辑备课" width="60%">
      <el-form :model="editForm" label-position="top">
        <el-form-item label="课程名称" required>
          <el-input v-model="editForm.name" placeholder="请输入课程名称" />
        </el-form-item>
        
        <el-form-item label="章节" required>
          <el-input v-model="editForm.chapter" placeholder="请输入章节" />
        </el-form-item>
        
        <el-form-item label="教学目标">
          <el-input 
            v-model="editForm.objectives" 
            type="textarea" 
            rows="3" 
            placeholder="请输入教学目标" 
          />
        </el-form-item>
        
        <el-form-item label="教学内容">
          <el-input 
            v-model="editForm.content" 
            type="textarea" 
            rows="5" 
            placeholder="请输入教学内容" 
          />
        </el-form-item>
        
        <el-form-item label="状态">
          <el-select v-model="editForm.status" placeholder="请选择状态">
            <el-option label="未开始" value="未开始" />
            <el-option label="进行中" value="进行中" />
            <el-option label="已完成" value="已完成" />
          </el-select>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveEditedLesson">保存</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- AI优化对话框 -->
    <el-dialog v-model="aiOptimizeDialogVisible" title="AI优化备课" width="60%">
      <div class="ai-optimize-header">
        <el-alert
          title="AI正在优化您的备课内容，优化后的内容将显示在下方表单中"
          type="info"
          :closable="false"
        />
      </div>
      
      <el-form :model="editForm" label-position="top">
        <el-form-item label="课程名称">
          <el-input v-model="editForm.name" disabled />
        </el-form-item>
        
        <el-form-item label="章节">
          <el-input v-model="editForm.chapter" disabled />
        </el-form-item>
        
        <el-form-item label="教学目标 (AI优化)">
          <el-input 
            v-model="editForm.objectives" 
            type="textarea" 
            rows="4" 
            placeholder="AI正在生成优化内容..." 
          />
        </el-form-item>
        
        <el-form-item label="教学内容 (AI优化)">
          <el-input 
            v-model="editForm.content" 
            type="textarea" 
            rows="6" 
            placeholder="AI正在生成优化内容..." 
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="aiOptimizeDialogVisible = false">取消</el-button>
          <el-button type="success" @click="saveAIOptimizedLesson">应用AI优化</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 备课详情对话框 -->
    <el-dialog v-model="detailDialogVisible" title="备课详情" width="60%">
      <div v-if="currentLesson" class="lesson-detail">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="课程名称">{{ currentLesson.name }}</el-descriptions-item>
          <el-descriptions-item label="章节">{{ currentLesson.chapter }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag 
              :type="currentLesson.status === '已完成' ? 'success' : currentLesson.status === '进行中' ? 'warning' : 'info'"
            >
              {{ currentLesson.status }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="创建日期">{{ currentLesson.date }}</el-descriptions-item>
          <el-descriptions-item label="教学目标" v-if="currentLesson.objectives">
            <pre>{{ currentLesson.objectives }}</pre>
          </el-descriptions-item>
          <el-descriptions-item label="教学内容" v-if="currentLesson.content">
            <pre>{{ currentLesson.content }}</pre>
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>
  </div>
</template>

<style scoped>
.lesson-planner {
  width: 100%; /* 确保组件宽度占满父容器 */
}

.el-tag {
  margin-right: 5px;
}

.chart-container {
  padding: 20px;
}

.chart-card {
  margin-bottom: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.chart {
  height: 400px;
  width: 100%;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.ai-optimize-header {
  margin-bottom: 20px;
}

pre {
  white-space: pre-wrap;
  word-wrap: break-word;
  background-color: #f5f7fa;
  padding: 10px;
  border-radius: 4px;
  margin: 0;
}

.lesson-detail {
  padding: 10px;
}
</style>