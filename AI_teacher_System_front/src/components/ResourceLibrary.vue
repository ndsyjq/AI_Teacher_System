<script setup>
import { ref, reactive, computed, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { questionService } from '../services';

// 题目类型
const questionTypes = ref([]);

// 题目数据
const questions = ref([]);

// 搜索关键词
const searchKeyword = ref('');

// 筛选条件
const filters = reactive({
  type: '',
  subject: '',
  grade: '',
  difficulty: ''
});

// 分页
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
});

// 上传对话框可见性
const uploadDialogVisible = ref(false);
// 预览对话框可见性
const previewDialogVisible = ref(false);
// 下载进度对话框可见性
const downloadProgressVisible = ref(false);

// 当前选中的题目
const currentQuestion = ref(null);
// 下载进度
const downloadProgress = ref(0);
// 下载状态
const downloadStatus = ref('');

// 上传表单
const uploadForm = reactive({
  title: '',
  type: '',
  subject: '',
  grade: '',
  difficulty: '',
  score: '',
  content: '',
  answer: '',
  analysis: ''
});

// 学科列表
const subjects = ref([
  { label: '语文', value: '语文' },
  { label: '数学', value: '数学' },
  { label: '英语', value: '英语' },
  { label: '物理', value: '物理' },
  { label: '化学', value: '化学' },
  { label: '生物', value: '生物' },
  { label: '历史', value: '历史' },
  { label: '地理', value: '地理' },
  { label: '政治', value: '政治' }
]);

// 年级列表
const grades = ref([
  { label: '初一', value: '初一' },
  { label: '初二', value: '初二' },
  { label: '初三', value: '初三' },
  { label: '高一', value: '高一' },
  { label: '高二', value: '高二' },
  { label: '高三', value: '高三' }
]);

// 难度列表
const difficultyLevels = ref([
  { label: '简单', value: '简单' },
  { label: '中等', value: '中等' },
  { label: '困难', value: '困难' }
]);

// 表格加载状态
const tableLoading = ref(false);

// 获取题目列表
const fetchQuestions = async () => {
  try {
    tableLoading.value = true;
    const params = {
      page: pagination.currentPage,
      size: pagination.pageSize,
      keyword: searchKeyword.value,
      type: filters.type,
      subject: filters.subject,
      grade: filters.grade,
      difficulty: filters.difficulty
    };
    
    const response = await questionService.getQuestions(params);
    if (response.code === 200) {
      questions.value = response.data.records || [];
      pagination.total = response.data.total || 0;
    } else {
      ElMessage.error(response.message || '获取题目列表失败');
    }
  } catch (error) {
    console.error('获取题目列表失败:', error);
    ElMessage.error('获取题目列表失败，请稍后再试');
  } finally {
    tableLoading.value = false;
  }
};

// 获取题目类型
const fetchQuestionTypes = async () => {
  try {
    const response = await questionService.getQuestionTypes();
    if (response.code === 200) {
      questionTypes.value = response.data.map(item => ({
        id: item.id,
        name: item.name
      }));
    } else {
      // 使用默认值
      questionTypes.value = [
        { id: 1, name: '单选题' },
        { id: 2, name: '多选题' },
        { id: 3, name: '判断题' },
        { id: 4, name: '填空题' },
        { id: 5, name: '解答题' }
      ];
    }
  } catch (error) {
    console.error('获取题目类型失败:', error);
    // 使用默认值
    questionTypes.value = [
      { id: 1, name: '单选题' },
      { id: 2, name: '多选题' },
      { id: 3, name: '判断题' },
      { id: 4, name: '填空题' },
      { id: 5, name: '解答题' }
    ];
  }
};

// 组件加载时获取数据
onMounted(() => {
  fetchQuestionTypes();
  fetchQuestions();
});

// 处理页码变化
const handleCurrentChange = (page) => {
  pagination.currentPage = page;
  fetchQuestions();
};

// 处理每页条数变化
const handleSizeChange = (size) => {
  pagination.pageSize = size;
  pagination.currentPage = 1;
  fetchQuestions();
};

// 处理搜索和筛选
const handleSearch = () => {
  pagination.currentPage = 1;
  fetchQuestions();
};

// 重置筛选条件
const resetFilters = () => {
  searchKeyword.value = '';
  filters.type = '';
  filters.subject = '';
  filters.grade = '';
  filters.difficulty = '';
  pagination.currentPage = 1;
  fetchQuestions();
};

// 打开上传对话框
const openUploadDialog = () => {
  uploadDialogVisible.value = true;
};

// 上传题目
const uploadQuestion = async () => {
  if (!uploadForm.title || !uploadForm.type) {
    ElMessage.warning('请填写必填项');
    return;
  }
  
  try {
    const questionData = {
      title: uploadForm.title,
      type: uploadForm.type,
      subject: uploadForm.subject,
      grade: uploadForm.grade,
      difficulty: uploadForm.difficulty,
      score: parseInt(uploadForm.score) || 5,
      content: uploadForm.content,
      answer: uploadForm.answer,
      analysis: uploadForm.analysis
    };
    
    const response = await questionService.addQuestion(questionData);
    
    if (response.code === 200) {
      ElMessage.success('添加题目成功');
      // 重置表单
      uploadForm.title = '';
      uploadForm.type = '';
      uploadForm.subject = '';
      uploadForm.grade = '';
      uploadForm.difficulty = '';
      uploadForm.score = '';
      uploadForm.content = '';
      uploadForm.answer = '';
      uploadForm.analysis = '';
      
      // 关闭对话框
      uploadDialogVisible.value = false;
      
      // 刷新列表
      fetchQuestions();
    } else {
      ElMessage.error(response.message || '添加题目失败');
    }
  } catch (error) {
    console.error('添加题目失败:', error);
    ElMessage.error('添加题目失败，请稍后再试');
  }
};

// 下载题目
const downloadQuestion = async (question) => {
  try {
    // 设置当前题目
    currentQuestion.value = question;
    
    // 显示下载进度对话框
    downloadProgressVisible.value = true;
    downloadProgress.value = 0;
    downloadStatus.value = '准备下载...';
    
    // 模拟下载进度
    const interval = setInterval(() => {
      downloadProgress.value += 10;
      
      if (downloadProgress.value < 50) {
        downloadStatus.value = '正在下载...';
      } else if (downloadProgress.value < 90) {
        downloadStatus.value = '即将完成...';
      } else {
        downloadStatus.value = '下载完成';
      }
      
      if (downloadProgress.value >= 100) {
        clearInterval(interval);
        
        // 延迟关闭对话框
        setTimeout(async () => {
          downloadProgressVisible.value = false;
          
          try {
            // 实际下载
            const response = await questionService.downloadQuestion(question.id);
            
            // 创建Blob对象
            const blob = new Blob([response], { type: 'application/octet-stream' });
            
            // 创建下载链接
            const link = document.createElement('a');
            link.href = window.URL.createObjectURL(blob);
            link.download = `${question.title}.docx`; // 默认文件名
            link.click();
            
            // 释放URL对象
            window.URL.revokeObjectURL(link.href);
            
            ElMessage.success('下载成功');
          } catch (error) {
            console.error('下载题目失败:', error);
            ElMessage.error('下载题目失败，请稍后再试');
          }
        }, 500);
      }
    }, 300);
  } catch (error) {
    console.error('下载题目失败:', error);
    ElMessage.error('下载题目失败，请稍后再试');
    downloadProgressVisible.value = false;
  }
};

// 预览题目
const previewQuestion = async (question) => {
  try {
    currentQuestion.value = question;
    
    // 获取题目详情
    const response = await questionService.getQuestionDetail(question.id);
    
    if (response.code === 200) {
      // 更新当前题目详情
      currentQuestion.value = response.data;
      
      // 显示预览对话框
      previewDialogVisible.value = true;
    } else {
      ElMessage.error(response.message || '获取题目详情失败');
    }
  } catch (error) {
    console.error('获取题目详情失败:', error);
    ElMessage.error('获取题目详情失败，请稍后再试');
  }
};

// 删除题目
const deleteQuestion = (question) => {
  ElMessageBox.confirm(
    `确定要删除题目 "${question.title}" 吗？`,
    '删除确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(async () => {
      try {
        const response = await questionService.deleteQuestion(question.id);
        
        if (response.code === 200) {
          ElMessage.success('删除成功');
          fetchQuestions();
        } else {
          ElMessage.error(response.message || '删除失败');
        }
      } catch (error) {
        console.error('删除题目失败:', error);
        ElMessage.error('删除题目失败，请稍后再试');
      }
    })
    .catch(() => {
      // 取消删除，不做处理
    });
};

// 获取题目预览内容
const getPreviewContent = (question) => {
  if (!question) return '';
  
  // 根据题目类型返回不同的预览内容
  let previewHtml = `<div class="question-preview">`;
  
  // 题目类型和分值
  previewHtml += `<div class="question-type-score">
                    <span class="question-type">[${question.type}]</span>
                    <span class="question-score">(${question.score}分)</span>
                  </div>`;
  
  // 题目内容
  previewHtml += `<div class="question-content">
                    <p>${question.title}</p>
                    <p>${question.content || '暂无题目内容'}</p>
                  </div>`;
  
  // 根据题型展示不同的选项或答题区域
  if (question.type === '单选题' || question.type === '多选题') {
    // 假设选项数据在options中
    const options = question.options || [
      { label: 'A', content: '选项A的内容' },
      { label: 'B', content: '选项B的内容' },
      { label: 'C', content: '选项C的内容' },
      { label: 'D', content: '选项D的内容' }
    ];
    
    previewHtml += `<div class="question-options">`;
    options.forEach(option => {
      previewHtml += `<div class="option"><span>${option.label}.</span> ${option.content}</div>`;
    });
    previewHtml += `</div>`;
  } else if (question.type === '判断题') {
    previewHtml += `<div class="question-options">
                      <div class="option"><span>√</span> 正确</div>
                      <div class="option"><span>×</span> 错误</div>
                    </div>`;
  } else if (question.type === '填空题') {
    previewHtml += `<div class="question-options">
                      <div class="blank-line">填空处：_________________</div>
                    </div>`;
  } else if (question.type === '解答题') {
    previewHtml += `<div class="question-options">
                      <div class="answer-area">答题区域：</div>
                      <div class="answer-lines"></div>
                    </div>`;
  }
  
  // 标准答案和解析
  previewHtml += `<div class="question-answer">
                    <div class="answer-header">参考答案：</div>
                    <div class="answer-content">${question.answer || '暂无答案'}</div>
                  </div>
                  <div class="question-analysis">
                    <div class="analysis-header">解析：</div>
                    <div class="analysis-content">${question.analysis || '暂无解析'}</div>
                  </div>`;
  
  previewHtml += `</div>`;
  
  return previewHtml;
};

// 导出题目
const exportQuestions = async () => {
  try {
    ElMessage.info('正在准备导出...');
    
    const params = {
      keyword: searchKeyword.value,
      type: filters.type,
      subject: filters.subject,
      grade: filters.grade,
      difficulty: filters.difficulty
    };
    
    const response = await questionService.exportQuestions(params);
    
    // 创建Blob对象
    const blob = new Blob([response], { type: 'application/vnd.ms-excel' });
    
    // 创建下载链接
    const link = document.createElement('a');
    link.href = window.URL.createObjectURL(blob);
    link.download = `题库导出_${new Date().toISOString().split('T')[0]}.xlsx`;
    link.click();
    
    // 释放URL对象
    window.URL.revokeObjectURL(link.href);
    
    ElMessage.success('导出成功');
  } catch (error) {
    console.error('导出题目失败:', error);
    ElMessage.error('导出题目失败，请稍后再试');
  }
};

// 批量导入题目对话框
const importDialogVisible = ref(false);
const importFile = ref(null);

// 打开导入对话框
const openImportDialog = () => {
  importDialogVisible.value = true;
};

// 导入题目
const importQuestions = async () => {
  if (!importFile.value) {
    ElMessage.warning('请选择要导入的Excel文件');
    return;
  }
  
  try {
    const formData = new FormData();
    formData.append('file', importFile.value);
    
    const response = await questionService.importQuestions(formData);
    
    if (response.code === 200) {
      ElMessage.success(`成功导入${response.data || 0}道题目`);
      importDialogVisible.value = false;
      importFile.value = null;
      fetchQuestions();
    } else {
      ElMessage.error(response.message || '导入失败');
    }
  } catch (error) {
    console.error('导入题目失败:', error);
    ElMessage.error('导入题目失败，请稍后再试');
  }
};

// 处理文件上传
const handleFileChange = (file) => {
  importFile.value = file.raw;
};

// 批量下载选中题目
const multipleSelection = ref([]);
const handleSelectionChange = (selection) => {
  multipleSelection.value = selection;
};

// 批量下载
const batchDownload = async () => {
  if (multipleSelection.value.length === 0) {
    ElMessage.warning('请选择要下载的题目');
    return;
  }
  
  try {
    ElMessage.info('正在准备下载...');
    
    const questionIds = multipleSelection.value.map(item => item.id);
    const response = await questionService.batchDownloadQuestions(questionIds);
    
    // 创建Blob对象
    const blob = new Blob([response], { type: 'application/octet-stream' });
    
    // 创建下载链接
    const link = document.createElement('a');
    link.href = window.URL.createObjectURL(blob);
    link.download = `批量题目_${new Date().toISOString().split('T')[0]}.zip`;
    link.click();
    
    // 释放URL对象
    window.URL.revokeObjectURL(link.href);
    
    ElMessage.success('批量下载成功');
  } catch (error) {
    console.error('批量下载题目失败:', error);
    ElMessage.error('批量下载题目失败，请稍后再试');
  }
};
</script>

<template>
  <div class="question-bank">
    <h2>题库</h2>
    
    <el-card>
      <div class="toolbar">
        <div class="search-bar">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索题目..."
            class="search-input"
            clearable
            @keyup.enter="handleSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
          
          <el-select v-model="filters.type" placeholder="题目类型" clearable>
            <el-option
              v-for="type in questionTypes"
              :key="type.id"
              :label="type.name"
              :value="type.name"
            />
          </el-select>
          
          <el-select v-model="filters.subject" placeholder="学科" clearable>
            <el-option
              v-for="subject in subjects"
              :key="subject.value"
              :label="subject.label"
              :value="subject.value"
            />
          </el-select>
          
          <el-select v-model="filters.grade" placeholder="年级" clearable>
            <el-option
              v-for="grade in grades"
              :key="grade.value"
              :label="grade.label"
              :value="grade.value"
            />
          </el-select>
          
          <el-select v-model="filters.difficulty" placeholder="难度" clearable>
            <el-option
              v-for="level in difficultyLevels"
              :key="level.value"
              :label="level.label"
              :value="level.value"
            />
          </el-select>
          
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon> 搜索
          </el-button>
          
          <el-button @click="resetFilters">
            <el-icon><RefreshRight /></el-icon> 重置
          </el-button>
        </div>
        
        <div class="action-bar">
          <el-button type="primary" @click="openUploadDialog">
            <el-icon><Plus /></el-icon> 添加题目
          </el-button>
          
          <el-button type="success" @click="exportQuestions">
            <el-icon><Download /></el-icon> 导出题目
          </el-button>
          
          <el-button type="warning" @click="openImportDialog">
            <el-icon><Upload /></el-icon> 批量导入
          </el-button>
          
          <el-button type="info" @click="batchDownload" :disabled="multipleSelection.length === 0">
            <el-icon><Download /></el-icon> 批量下载
          </el-button>
        </div>
      </div>
      
      <el-table 
        :data="questions" 
        style="width: 100%" 
        v-loading="tableLoading"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="title" label="题目标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="type" label="题型" width="100" />
        <el-table-column prop="subject" label="学科" width="80" />
        <el-table-column prop="grade" label="年级" width="80" />
        <el-table-column prop="difficulty" label="难度" width="80">
          <template #default="scope">
            <el-tag 
              :type="scope.row.difficulty === '简单' ? 'success' : 
                    scope.row.difficulty === '中等' ? 'warning' : 'danger'"
              size="small"
            >
              {{ scope.row.difficulty }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="score" label="分值" width="80" />
        <el-table-column prop="uploadTime" label="上传时间" width="120" />
        <el-table-column prop="usageCount" label="使用次数" width="100" />
        <el-table-column label="操作" width="220">
          <template #default="scope">
            <el-button size="small" type="primary" @click="downloadQuestion(scope.row)">
              <el-icon><Download /></el-icon> 下载
            </el-button>
            <el-button size="small" @click="previewQuestion(scope.row)">
              <el-icon><View /></el-icon> 预览
            </el-button>
            <el-button size="small" type="danger" @click="deleteQuestion(scope.row)">
              <el-icon><Delete /></el-icon> 删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.currentPage"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
    
    <!-- 添加题目对话框 -->
    <el-dialog v-model="uploadDialogVisible" title="添加题目" width="60%">
      <el-form :model="uploadForm" label-width="100px">
        <el-form-item label="题目标题" required>
          <el-input v-model="uploadForm.title" placeholder="请输入题目标题" />
        </el-form-item>
        
        <el-form-item label="题目类型" required>
          <el-select v-model="uploadForm.type" placeholder="请选择题目类型" style="width: 100%">
            <el-option
              v-for="type in questionTypes"
              :key="type.id"
              :label="type.name"
              :value="type.name"
            />
          </el-select>
        </el-form-item>
        
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="学科">
              <el-select v-model="uploadForm.subject" placeholder="请选择学科" style="width: 100%">
                <el-option
                  v-for="subject in subjects"
                  :key="subject.value"
                  :label="subject.label"
                  :value="subject.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          
          <el-col :span="8">
            <el-form-item label="适用年级">
              <el-select v-model="uploadForm.grade" placeholder="请选择年级" style="width: 100%">
                <el-option
                  v-for="grade in grades"
                  :key="grade.value"
                  :label="grade.label"
                  :value="grade.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          
          <el-col :span="8">
            <el-form-item label="难度">
              <el-select v-model="uploadForm.difficulty" placeholder="请选择难度" style="width: 100%">
                <el-option
                  v-for="level in difficultyLevels"
                  :key="level.value"
                  :label="level.label"
                  :value="level.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="分值">
          <el-input-number v-model="uploadForm.score" :min="1" :max="100" />
        </el-form-item>
        
        <el-form-item label="题目内容" required>
          <el-input 
            v-model="uploadForm.content" 
            type="textarea" 
            :rows="4" 
            placeholder="请输入题目内容"
          />
        </el-form-item>
        
        <el-form-item label="标准答案">
          <el-input 
            v-model="uploadForm.answer" 
            type="textarea" 
            :rows="3" 
            placeholder="请输入标准答案"
          />
        </el-form-item>
        
        <el-form-item label="解析说明">
          <el-input 
            v-model="uploadForm.analysis" 
            type="textarea" 
            :rows="3" 
            placeholder="请输入解析说明"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="uploadDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="uploadQuestion">添加</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 批量导入对话框 -->
    <el-dialog v-model="importDialogVisible" title="批量导入题目" width="40%">
      <el-upload
        class="upload-demo"
        action="#"
        :auto-upload="false"
        :limit="1"
        accept=".xlsx,.xls"
        :on-change="handleFileChange"
      >
        <template #trigger>
          <el-button type="primary">选择Excel文件</el-button>
        </template>
        <template #tip>
          <div class="el-upload__tip">
            请上传符合模板格式的Excel文件，<a href="#">点击下载模板</a>
          </div>
        </template>
      </el-upload>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="importDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="importQuestions">导入</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 预览题目对话框 -->
    <el-dialog v-model="previewDialogVisible" title="题目预览" width="70%" destroy-on-close>
      <div v-if="currentQuestion" class="question-preview-container">
        <div class="preview-header">
          <h3>{{ currentQuestion.title }}</h3>
          <div class="preview-info">
            <span>题型：{{ currentQuestion.type }}</span>
            <span>学科：{{ currentQuestion.subject }}</span>
            <span>难度：{{ currentQuestion.difficulty }}</span>
            <span>分值：{{ currentQuestion.score }}分</span>
          </div>
        </div>
        
        <div class="preview-content" v-html="getPreviewContent(currentQuestion)"></div>
        
        <div class="preview-footer">
          <el-button type="primary" @click="downloadQuestion(currentQuestion)">
            <el-icon><Download /></el-icon> 下载此题目
          </el-button>
        </div>
      </div>
    </el-dialog>
    
    <!-- 下载进度对话框 -->
    <el-dialog v-model="downloadProgressVisible" title="下载进度" width="30%" :close-on-click-modal="false" :show-close="false">
      <div v-if="currentQuestion" class="download-progress">
        <p>正在下载：{{ currentQuestion.title }}</p>
        <el-progress :percentage="downloadProgress" :status="downloadProgress === 100 ? 'success' : ''"></el-progress>
        <p class="download-status">{{ downloadStatus }}</p>
      </div>
    </el-dialog>
  </div>
</template>

<style scoped>
.question-bank {
  width: 100%; /* 确保组件宽度占满父容器 */
}

.toolbar {
  margin-bottom: 20px;
}

.search-bar {
  display: flex;
  gap: 15px;
  margin-bottom: 15px;
  flex-wrap: wrap;
}

.action-bar {
  display: flex;
  gap: 15px;
  justify-content: flex-start;
  margin-bottom: 15px;
}

.search-input {
  width: 300px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.question-preview-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.preview-header {
  border-bottom: 1px solid #eee;
  padding-bottom: 10px;
}

.preview-info {
  display: flex;
  gap: 20px;
  color: #666;
  font-size: 14px;
}

.preview-content {
  min-height: 300px;
  display: flex;
  flex-direction: column;
  background-color: #f5f7fa;
  border-radius: 4px;
  padding: 20px;
}

.question-preview {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.question-type-score {
  display: flex;
  gap: 10px;
  font-weight: bold;
}

.question-content {
  margin: 10px 0;
}

.question-options {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin: 10px 0;
}

.option {
  display: flex;
  gap: 10px;
}

.blank-line, .answer-area {
  margin: 10px 0;
}

.answer-lines {
  height: 100px;
  border: 1px dashed #ddd;
  border-radius: 4px;
}

.question-answer, .question-analysis {
  margin-top: 20px;
  padding-top: 10px;
  border-top: 1px dashed #ddd;
}

.answer-header, .analysis-header {
  font-weight: bold;
  margin-bottom: 5px;
}

.preview-footer {
  display: flex;
  justify-content: center;
  margin-top: 10px;
}

.download-progress {
  padding: 10px;
}

.download-status {
  text-align: center;
  margin-top: 10px;
  color: #409EFF;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>