<script setup>
import { ref, reactive, onMounted } from 'vue';
import * as echarts from 'echarts';
import { ElMessage, ElLoading } from 'element-plus';
import { lessonService } from '../services';
// 引入echarts，需要先安装：npm install echarts --save

// 备课表单数据
const lessonForm = reactive({
  name: '',
  chapter: '',
  objectives: '',
  content: ''
});

// 备课数据
const lessonPlans = ref([]);

// 加载状态
const loading = ref(false);

// 错误信息
const errorMessage = ref('');

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
const number=ref(0)
// 保存备课
const saveLessonPlan = async () => {
  if (!lessonForm.name || !lessonForm.chapter) {
    ElMessage.warning('请填写课程名称和章节');
    return;
  }
  
  const loadingInstance = ElLoading.service({
    text: '正在保存备课计划...',
    background: 'rgba(0, 0, 0, 0.7)'
  });
  
  try {
    // 准备备课数据
    const lessonData = {
      name: lessonForm.name,
      chapter: lessonForm.chapter,
      objectives: lessonForm.objectives,
      content: lessonForm.content,
      status: '未开始',
      date: new Date().toISOString().split('T')[0]
    };
    
    // 调用API保存备课计划
    await lessonService.saveLessonPlan(lessonData);
    
    // 重置表单
    lessonForm.name = '';
    lessonForm.chapter = '';
    lessonForm.objectives = '';
    lessonForm.content = '';
    
    // 切换到我的备课标签并刷新列表
    activeTab.value = 'myLessons';
    fetchLessonPlans();
    
    ElMessage.success('备课计划保存成功');
  } catch (error) {
    console.error('保存备课计划失败:', error);
    ElMessage.error('保存备课计划失败: ' + (error.message || '未知错误'));
  } finally {
    loadingInstance.close();
  }
};

// AI智能生成备课内容
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
const saveEditedLesson = async () => {
  if (!editForm.name || !editForm.chapter) {
    ElMessage.warning('请填写课程名称和章节');
    return;
  }
  
  const loadingInstance = ElLoading.service({
    text: '正在保存备课计划...',
    background: 'rgba(0, 0, 0, 0.7)'
  });
  
  try {
    // 更新备课数据
    if (currentLesson.value) {
      // 准备更新的数据
      const lessonData = {
        name: editForm.name,
        chapter: editForm.chapter,
        objectives: editForm.objectives,
        content: editForm.content,
        status: editForm.status
      };
      
      // 调用API更新备课计划
      await lessonService.updateLessonPlan(currentLesson.value.id, lessonData);
      
      // 关闭对话框
      editDialogVisible.value = false;
      
      // 刷新备课列表
      fetchLessonPlans();
      
      ElMessage.success('备课计划更新成功');
    }
  } catch (error) {
    console.error('更新备课计划失败:', error);
    ElMessage.error('更新备课计划失败: ' + (error.message || '未知错误'));
  } finally {
    loadingInstance.close();
  }

};

// 打开AI优化对话框
const openAIOptimizeDialog = async (lesson) => {
  currentLesson.value = lesson;
  number.value=0;
  // 复制数据到编辑表单
  editForm.name = lesson.name;
  editForm.chapter = lesson.chapter;
  editForm.objectives = lesson.objectives || '';
  editForm.content = lesson.content || '';
  // 显示AI优化对话框
  aiOptimizeDialogVisible.value = true;

}
// 保存AI优化的备课
const generatedQuestions = ref([]); // 新增：存储生成的题目
const saveAIOptimizedLesson = async () => {
  if (!editForm.name || !editForm.chapter) {
    ElMessage.warning('请填写课程名称和章节');
    return;
  }

  try {
    // 准备备课数据
    const lessonData = `给我出${number.value}道${editForm.name + editForm.chapter}相关的单选题，要带有正确答案，格式为---， 每道题以 ###题目X 开头，答案给这个格式 正确答案：X(直接给答案字母就行)，---直接给答案什么话都不要讲`;

    // 调用API优化备课内容
    const response = await lessonService.optimizeLessonPlan(editForm.name, editForm.chapter, lessonData);
    if (response.code === 200) {
      // 解析返回的题目数据（假设response.data是题目数组）
      generatedQuestions.value = response.data;
      debugger
      ElMessage.success(`AI生成完成，共生成${generatedQuestions.value.lenth}道题目`);
    } else {
      ElMessage.error('AI生成失败，请重试');
    }
  } catch (error) {
    console.error('AI生成失败:', error);
    ElMessage.error('AI生成失败: ' + (error.message || '未知错误'));
    // 模拟数据（测试用）
    generatedQuestions.value = [{
      "title": "在数据结构中，以下哪种方法可以有效地遍历图中的所有顶点？", 
      "optionA": "DFS（深度优先搜索）", 
      "optionB": "BFS（广度优先搜索）", 
      "optionC": "递归遍历", 
      "optionD": "全部都可以", 
      "answer": "D"
    }];
    ElMessage.warning('API调用失败，使用模拟数据展示');
  }

};

// 查看备课详情
const viewLessonDetail = async (lesson) => {
  const loadingInstance = ElLoading.service({
    text: '正在加载备课详情...',
    background: 'rgba(0, 0, 0, 0.7)'
  });
  
  try {
    // 调用API获取备课详情
    const response = await lessonService.getLessonDetail(lesson.id);
    currentLesson.value = response.data;
    detailDialogVisible.value = true;
  } catch (error) {
    console.error('获取备课详情失败:', error);
    ElMessage.error('获取备课详情失败: ' + (error.message || '未知错误'));
    // 如果API调用失败，使用传入的lesson数据作为备选
    currentLesson.value = lesson;
    detailDialogVisible.value = true;
  } finally {
    loadingInstance.close();
  }
};

// 初始化备课状态饼图
const initStatusChart = () => {
  if (!statusChartRef.value || loading.value || lessonPlans.value.length === 0) {
    return;
  }
  
  // 如果已经初始化过图表，先销毁
  if (statusChart) {
    statusChart.dispose();
  }
  
  // 重新初始化图表
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
  
  // 移除旧的事件监听器，避免重复添加
  window.removeEventListener('resize', handleResize);
  window.addEventListener('resize', handleResize);
};

// 处理窗口大小变化
const handleResize = () => {
  if (statusChart) {
    statusChart.resize();
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

// 获取备课列表
const fetchLessonPlans = async () => {
  loading.value = true;
  errorMessage.value = '';
  
  try {
    const response = await lessonService.getLessonPlans();
    lessonPlans.value = response.data;
    
    // 如果在分析页面，需要重新初始化图表
    if (activeTab.value === 'lessonAnalysis') {
      setTimeout(() => {
        initStatusChart();
      }, 100);
    }
  } catch (error) {
    console.error('获取备课列表失败:', error);
    errorMessage.value = '获取备课列表失败: ' + (error.message || '未知错误');
    ElMessage.error(errorMessage.value);
  } finally {
    loading.value = false;
  }
};

// 组件挂载时初始化
onMounted(() => {
  // 获取备课列表
  fetchLessonPlans();
});
</script>

<template>
  <div class="lesson-planner">
    <h2>备课中心</h2>
    <el-alert
      v-if="errorMessage"
      :title="errorMessage"
      type="error"
      :closable="true"
      @close="errorMessage = ''"
      style="margin-bottom: 15px;"
    />
    <el-tabs v-model="activeTab" type="border-card" @tab-change="handleTabChange">
      <el-tab-pane name="myLessons" label="我的备课">
        <div v-if="loading" class="loading-container">
          <el-skeleton :rows="5" animated />
        </div>
        <el-empty v-else-if="lessonPlans.length === 0" description="暂无备课数据" />
        <el-table v-else :data="lessonPlans" style="width: 100%">
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
              <el-button size="small" type="success" @click="openAIOptimizeDialog(scope.row)">AI生成题目</el-button>
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
          </el-form-item>
        </el-form>
      </el-tab-pane>
      
      <el-tab-pane name="lessonAnalysis" label="备课分析">
        <div v-if="loading" class="loading-container">
          <el-skeleton :rows="3" animated />
        </div>
        <div v-else-if="lessonPlans.length === 0" class="empty-container">
          <el-empty description="暂无备课数据，无法生成分析" />
        </div>
        <div v-else class="chart-container">
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
    <el-dialog v-model="aiOptimizeDialogVisible" title="AI生成题目" width="60%">
      <el-form :model="editForm" label-position="top">
        <el-form-item label="课程名称">
          <el-input v-model="editForm.name" disabled />
        </el-form-item>
        
        <el-form-item label="章节">
          <el-input v-model="editForm.chapter" disabled />
        </el-form-item>
        <el-form-item label="生成题目数量">
          <el-input-number v-model="number"  />
        </el-form-item>

      </el-form>

      <!-- 题目显示区域 -->
      <div v-if="generatedQuestions.length > 0" class="questions-container">
        <h3>生成的题目：</h3>
        <el-card v-for="(q, index) in generatedQuestions" :key="index" class="question-card">
          <p class="question-title">{{ q.title }}</p>
          <div class="options">
            <p>选项A: {{ q.optionA }}</p>
            <p>选项B: {{ q.optionB }}</p>
            <p>选项C: {{ q.optionC }}</p>
            <p>选项D: {{ q.optionD }}</p>
          </div>
          <el-tag type="success" class="answer">正确答案：{{ q.answer }}</el-tag>
        </el-card>
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="aiOptimizeDialogVisible = false">关闭</el-button>
          <el-button type="success" @click="saveAIOptimizedLesson">开始生成</el-button>
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
.loading-container {
  padding: 20px;
}

.empty-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 300px;
}
</style>