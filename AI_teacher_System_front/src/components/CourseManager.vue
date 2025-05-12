<script setup>
import { ref, reactive, onMounted } from 'vue';
import * as echarts from 'echarts';
// 引入echarts，需要先安装：npm install echarts --save

// 课程表单数据
const courseForm = reactive({
  name: '',
  subject: '',
  grade: '',
  semester: '',
  description: ''
});

// 模拟课程数据
const courses = ref([
  {
    id: 1,
    name: '高中数学必修一',
    subject: '数学',
    grade: '高一',
    semester: '上学期',
    students: 45,
    progress: 70,
    status: '进行中'
  },
  {
    id: 2,
    name: '高中物理必修二',
    subject: '物理',
    grade: '高一',
    semester: '下学期',
    students: 42,
    progress: 30,
    status: '进行中'
  },
  {
    id: 3,
    name: '高中语文必修三',
    subject: '语文',
    grade: '高二',
    semester: '上学期',
    students: 48,
    progress: 0,
    status: '未开始'
  }
]);

// 当前激活的标签页
const activeTab = ref('myCourses');

// 图表引用
const progressChartRef = ref(null);
const subjectChartRef = ref(null);
const progressTrendChartRef = ref(null);
let progressChart = null;
let subjectChart = null;
let progressTrendChart = null;

// 对话框可见性
const dialogVisible = ref(false);
const detailDialogVisible = ref(false);
const editDialogVisible = ref(false);
const deleteConfirmVisible = ref(false);

// 当前选中的课程
const currentCourse = ref(null);

// 打开添加课程对话框
const openAddDialog = () => {
  dialogVisible.value = true;
};

// 添加课程
const addCourse = () => {
  if (!courseForm.name || !courseForm.subject || !courseForm.grade) {
    // 这里应该有表单验证
    return;
  }
  
  // 模拟添加操作
  courses.value.push({
    id: courses.value.length + 1,
    name: courseForm.name,
    subject: courseForm.subject,
    grade: courseForm.grade,
    semester: courseForm.semester,
    students: 0,
    progress: 0,
    status: '未开始'
  });
  
  // 重置表单
  courseForm.name = '';
  courseForm.subject = '';
  courseForm.grade = '';
  courseForm.semester = '';
  courseForm.description = '';
  
  // 关闭对话框
  dialogVisible.value = false;
};

// 查看课程详情
const viewCourseDetail = (courseId) => {
  // 查找选中的课程
  currentCourse.value = courses.value.find(course => course.id === courseId);
  if (currentCourse.value) {
    detailDialogVisible.value = true;
  }
};

// 打开编辑课程对话框
const openEditDialog = (courseId) => {
  // 查找选中的课程
  currentCourse.value = courses.value.find(course => course.id === courseId);
  if (currentCourse.value) {
    // 将课程数据填充到表单中
    courseForm.name = currentCourse.value.name;
    courseForm.subject = currentCourse.value.subject;
    courseForm.grade = currentCourse.value.grade;
    courseForm.semester = currentCourse.value.semester;
    courseForm.description = currentCourse.value.description || '';
    
    // 打开编辑对话框
    editDialogVisible.value = true;
  }
};

// 保存编辑的课程
const saveEditedCourse = () => {
  if (!courseForm.name || !courseForm.subject || !courseForm.grade) {
    // 表单验证
    return;
  }
  
  // 更新课程数据
  if (currentCourse.value) {
    const index = courses.value.findIndex(course => course.id === currentCourse.value.id);
    if (index !== -1) {
      // 保留原有的id、学生数、进度和状态
      const updatedCourse = {
        ...courses.value[index],
        name: courseForm.name,
        subject: courseForm.subject,
        grade: courseForm.grade,
        semester: courseForm.semester,
        description: courseForm.description
      };
      
      // 更新课程
      courses.value[index] = updatedCourse;
      
      // 关闭对话框
      editDialogVisible.value = false;
      
      // 重置表单
      courseForm.name = '';
      courseForm.subject = '';
      courseForm.grade = '';
      courseForm.semester = '';
      courseForm.description = '';
      
      // 如果在分析页面，需要重新初始化图表
      if (activeTab.value === 'courseAnalysis') {
        setTimeout(() => {
          initProgressChart();
          initSubjectChart();
          initProgressTrendChart();
        }, 100);
      }
    }
  }
};

// 确认删除课程
const confirmDeleteCourse = (courseId) => {
  // 查找选中的课程
  currentCourse.value = courses.value.find(course => course.id === courseId);
  if (currentCourse.value) {
    deleteConfirmVisible.value = true;
  }
};

// 删除课程
const deleteCourse = () => {
  if (currentCourse.value) {
    // 从数组中删除课程
    const index = courses.value.findIndex(course => course.id === currentCourse.value.id);
    if (index !== -1) {
      courses.value.splice(index, 1);
      
      // 关闭确认对话框
      deleteConfirmVisible.value = false;
      
      // 如果在分析页面，需要重新初始化图表
      if (activeTab.value === 'courseAnalysis') {
        setTimeout(() => {
          initProgressChart();
          initSubjectChart();
          initProgressTrendChart();
        }, 100);
      }
    }
  }
};

// 初始化课程进度图表
const initProgressChart = () => {
  if (progressChartRef.value) {
    progressChart = echarts.init(progressChartRef.value);
    
    const option = {
      title: {
        text: '课程进度概览',
        left: 'center'
      },
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        }
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: {
        type: 'value',
        max: 100,
        name: '完成百分比'
      },
      yAxis: {
        type: 'category',
        data: courses.value.map(course => course.name),
        axisLabel: {
          width: 120,
          overflow: 'truncate'
        }
      },
      series: [
        {
          name: '课程进度',
          type: 'bar',
          data: courses.value.map(course => course.progress),
          itemStyle: {
            color: function(params) {
              // 根据进度设置不同颜色
              const progress = params.value;
              if (progress === 0) return '#909399'; // 未开始
              if (progress < 50) return '#E6A23C'; // 刚开始
              if (progress < 100) return '#409EFF'; // 进行中
              return '#67C23A'; // 已完成
            }
          }
        }
      ]
    };
    
    progressChart.setOption(option);
    window.addEventListener('resize', progressChart.resize);
  }
};

// 初始化学科分布图表
const initSubjectChart = () => {
  if (subjectChartRef.value) {
    subjectChart = echarts.init(subjectChartRef.value);
    
    // 统计各学科课程数量
    const subjectCount = {};
    courses.value.forEach(course => {
      if (subjectCount[course.subject]) {
        subjectCount[course.subject]++;
      } else {
        subjectCount[course.subject] = 1;
      }
    });
    
    const option = {
      title: {
        text: '课程学科分布',
        left: 'center'
      },
      tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b}: {c} ({d}%)'
      },
      legend: {
        orient: 'vertical',
        left: 'left',
        data: Object.keys(subjectCount)
      },
      series: [
        {
          name: '学科分布',
          type: 'pie',
          radius: '50%',
          data: Object.keys(subjectCount).map(key => ({
            name: key,
            value: subjectCount[key]
          })),
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
    
    subjectChart.setOption(option);
    window.addEventListener('resize', subjectChart.resize);
  }
};

// 初始化学习进度趋势图表
const initProgressTrendChart = () => {
  if (progressTrendChartRef.value) {
    progressTrendChart = echarts.init(progressTrendChartRef.value);
    
    // 模拟时间数据（可以根据实际需求调整）
    const dates = ['1月', '2月', '3月', '4月', '5月', '6月'];
    
    // 为每个课程生成模拟的进度趋势数据
    const seriesData = courses.value.map(course => {
      // 生成模拟的进度数据，从0开始逐渐增加到当前进度
      const progressData = [];
      const currentProgress = course.progress;
      for (let i = 0; i < dates.length; i++) {
        // 生成一个从0到当前进度的渐进数列
        const progress = Math.min(Math.round(currentProgress * (i + 1) / dates.length), currentProgress);
        progressData.push(progress);
      }
      
      return {
        name: course.name,
        type: 'line',
        data: progressData,
        smooth: true
      };
    });
    
    const option = {
      title: {
        text: '学习进度趋势',
        left: 'center'
      },
      tooltip: {
        trigger: 'axis'
      },
      legend: {
        data: courses.value.map(course => course.name),
        type: 'scroll',
        orient: 'horizontal',
        bottom: 0
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '15%',
        containLabel: true
      },
      xAxis: {
        type: 'category',
        boundaryGap: false,
        data: dates
      },
      yAxis: {
        type: 'value',
        max: 100,
        name: '完成百分比'
      },
      series: seriesData
    };
    
    progressTrendChart.setOption(option);
    window.addEventListener('resize', progressTrendChart.resize);
  }
};

// 监听标签页变化，初始化图表
const handleTabChange = (tab) => {
  if (tab === 'courseAnalysis') {
    // 使用nextTick确保DOM已更新
    setTimeout(() => {
      initProgressChart();
      initSubjectChart();
      initProgressTrendChart();
    }, 100);
  }
};

// 组件挂载时初始化
onMounted(() => {
  if (activeTab.value === 'courseAnalysis') {
    setTimeout(() => {
      initProgressChart();
      initSubjectChart();
      initProgressTrendChart();
    }, 100);
  }
});
</script>

<template>
  <div class="course-manager">
    <h2>课程管理</h2>
    <el-tabs v-model="activeTab" type="border-card" @tab-change="handleTabChange">
      <el-tab-pane name="myCourses" label="我的课程">
        <div class="action-bar">
          <el-button type="primary" @click="openAddDialog">
            <el-icon><Plus /></el-icon> 添加课程
          </el-button>
        </div>
        
        <el-table :data="courses" style="width: 100%">
          <el-table-column prop="name" label="课程名称" />
          <el-table-column prop="subject" label="学科" width="100" />
          <el-table-column prop="grade" label="年级" width="100" />
          <el-table-column prop="semester" label="学期" width="100" />
          <el-table-column prop="students" label="学生数" width="100" />
          <el-table-column label="进度" width="180">
            <template #default="scope">
              <el-progress :percentage="scope.row.progress" />
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag 
                :type="scope.row.status === '已完成' ? 'success' : scope.row.status === '进行中' ? 'warning' : 'info'"
              >
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200">
            <template #default="scope">
              <el-button size="small" @click="viewCourseDetail(scope.row.id)">查看</el-button>
              <el-button size="small" type="primary" @click="openEditDialog(scope.row.id)">编辑</el-button>
              <el-button size="small" type="danger" @click="confirmDeleteCourse(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      
      <el-tab-pane name="courseAnalysis" label="课程分析">
        <div class="chart-container">
          <el-row :gutter="20">
            <el-col :span="24">
              <el-card class="chart-card">
                <div ref="progressChartRef" class="chart"></div>
              </el-card>
            </el-col>
          </el-row>
          
          <el-row :gutter="20" style="margin-top: 20px;">
            <el-col :span="12">
              <el-card class="chart-card">
                <div ref="subjectChartRef" class="chart"></div>
              </el-card>
            </el-col>
            
            <el-col :span="12">
              <el-card class="chart-card">
                <h3>课程状态统计</h3>
                <el-table :data="[{
                  status: '未开始',
                  count: courses.filter(c => c.progress === 0).length,
                  color: '#909399'
                }, {
                  status: '进行中',
                  count: courses.filter(c => c.progress > 0 && c.progress < 100).length,
                  color: '#409EFF'
                }, {
                  status: '已完成',
                  count: courses.filter(c => c.progress === 100).length,
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
          
          <el-row :gutter="20" style="margin-top: 20px;">
            <el-col :span="24">
              <el-card class="chart-card">
                <div ref="progressTrendChartRef" class="chart"></div>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </el-tab-pane>
    </el-tabs>
    
    <!-- 添加课程对话框 -->
    <el-dialog v-model="dialogVisible" title="添加课程" width="50%">
      <el-form :model="courseForm" label-width="100px">
        <el-form-item label="课程名称" required>
          <el-input v-model="courseForm.name" placeholder="请输入课程名称" />
        </el-form-item>
        
        <el-form-item label="学科" required>
          <el-select v-model="courseForm.subject" placeholder="请选择学科" style="width: 100%">
            <el-option label="语文" value="语文" />
            <el-option label="数学" value="数学" />
            <el-option label="英语" value="英语" />
            <el-option label="物理" value="物理" />
            <el-option label="化学" value="化学" />
            <el-option label="生物" value="生物" />
            <el-option label="历史" value="历史" />
            <el-option label="地理" value="地理" />
            <el-option label="政治" value="政治" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="年级" required>
          <el-select v-model="courseForm.grade" placeholder="请选择年级" style="width: 100%">
            <el-option label="初一" value="初一" />
            <el-option label="初二" value="初二" />
            <el-option label="初三" value="初三" />
            <el-option label="高一" value="高一" />
            <el-option label="高二" value="高二" />
            <el-option label="高三" value="高三" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="学期">
          <el-select v-model="courseForm.semester" placeholder="请选择学期" style="width: 100%">
            <el-option label="上学期" value="上学期" />
            <el-option label="下学期" value="下学期" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="课程描述">
          <el-input 
            v-model="courseForm.description" 
            type="textarea" 
            :rows="3" 
            placeholder="请输入课程描述"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="addCourse">确定</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 课程详情对话框 -->
    <el-dialog v-model="detailDialogVisible" title="课程详情" width="50%">
      <div v-if="currentCourse" class="course-detail">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="课程名称">{{ currentCourse.name }}</el-descriptions-item>
          <el-descriptions-item label="学科">{{ currentCourse.subject }}</el-descriptions-item>
          <el-descriptions-item label="年级">{{ currentCourse.grade }}</el-descriptions-item>
          <el-descriptions-item label="学期">{{ currentCourse.semester }}</el-descriptions-item>
          <el-descriptions-item label="学生数">{{ currentCourse.students }}</el-descriptions-item>
          <el-descriptions-item label="进度">
            <el-progress :percentage="currentCourse.progress" />
          </el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag 
              :type="currentCourse.status === '已完成' ? 'success' : currentCourse.status === '进行中' ? 'warning' : 'info'"
            >
              {{ currentCourse.status }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="描述" v-if="currentCourse.description">
            {{ currentCourse.description }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>
    
    <!-- 编辑课程对话框 -->
    <el-dialog v-model="editDialogVisible" title="编辑课程" width="50%">
      <el-form :model="courseForm" label-width="100px">
        <el-form-item label="课程名称" required>
          <el-input v-model="courseForm.name" placeholder="请输入课程名称" />
        </el-form-item>
        
        <el-form-item label="学科" required>
          <el-select v-model="courseForm.subject" placeholder="请选择学科" style="width: 100%">
            <el-option label="语文" value="语文" />
            <el-option label="数学" value="数学" />
            <el-option label="英语" value="英语" />
            <el-option label="物理" value="物理" />
            <el-option label="化学" value="化学" />
            <el-option label="生物" value="生物" />
            <el-option label="历史" value="历史" />
            <el-option label="地理" value="地理" />
            <el-option label="政治" value="政治" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="年级" required>
          <el-select v-model="courseForm.grade" placeholder="请选择年级" style="width: 100%">
            <el-option label="初一" value="初一" />
            <el-option label="初二" value="初二" />
            <el-option label="初三" value="初三" />
            <el-option label="高一" value="高一" />
            <el-option label="高二" value="高二" />
            <el-option label="高三" value="高三" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="学期">
          <el-select v-model="courseForm.semester" placeholder="请选择学期" style="width: 100%">
            <el-option label="上学期" value="上学期" />
            <el-option label="下学期" value="下学期" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="课程描述">
          <el-input 
            v-model="courseForm.description" 
            type="textarea" 
            :rows="3" 
            placeholder="请输入课程描述"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveEditedCourse">保存</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 删除确认对话框 -->
    <el-dialog v-model="deleteConfirmVisible" title="确认删除" width="30%">
      <span>确定要删除课程 "{{ currentCourse?.name }}" 吗？此操作不可恢复。</span>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="deleteConfirmVisible = false">取消</el-button>
          <el-button type="danger" @click="deleteCourse">确定删除</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
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
.course-manager {
  width: 100%; /* 确保组件宽度占满父容器 */
}

.action-bar {
  margin-bottom: 20px;
  display: flex;
  justify-content: flex-end;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.course-detail {
  padding: 10px;
}
</style>