<script setup>
import { ref, reactive, computed, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { questionService } from '../services';
import axios from 'axios';
import {Delete, Download, Edit, Plus, RefreshRight, Search, Upload, View} from "@element-plus/icons-vue";
import * as XLSX from 'xlsx';

// 题目类型
const questionTypes = ref([]);

// 题目数据
const questions = ref([]);
const allQuestions = ref([]);

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
  { label: '简单', value: '1' },
  { label: '中等', value: '2' },
  { label: '困难', value: '3' }
]);
const answers = ref([
  { label: '选项A', value: 'A' },
  { label: '选项B', value: 'B' },
  { label: '选项C', value: 'C' },
  { label: '选项D', value: 'D'}
]);
// 表格加载状态
const tableLoading = ref(false);

// 编辑对话框可见性
const editDialogVisible = ref(false);

// 编辑表单数据
const editForm = reactive({
  id: '',
  title: '',
  type: '',
  subject: '',
  chapter: '',
  difficulty: '',
  optionA: '',
  optionB: '',
  optionC: '',
  optionD: '',
  answer: ''
});

// 获取题目列表
const fetchQuestions = async () => {
  try {
    tableLoading.value = true;
    const response = await questionService.getQuestions();
    if (response.code === 200) {
      allQuestions.value = response.data || [];
      pagination.total = allQuestions.value.length || 0;
      applyFiltersAndPagination();
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
  applyFiltersAndPagination();
};
// 处理每页条数变化
const handleSizeChange = (size) => {
  pagination.pageSize = size;
  pagination.currentPage = 1;
  applyFiltersAndPagination();
};

// 处理搜索和筛选
const handleSearch = () => {
  pagination.currentPage = 1;
  applyFiltersAndPagination();
};

// 重置筛选条件
const resetFilters = () => {
  searchKeyword.value = '';
  filters.type = '';
  filters.subject = '';
  filters.grade = '';
  filters.difficulty = '';
  pagination.currentPage = 1;
  applyFiltersAndPagination();
};

// 前端筛选和分页
const applyFiltersAndPagination = () => {
  // 应用筛选条件
  let filteredData = allQuestions.value;
  
  // 关键词搜索
  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase();
    filteredData = filteredData.filter(item => 
      item.title && item.title.toLowerCase().includes(keyword)
    );
  }
  
  // 类型筛选
  if (filters.type) {
    filteredData = filteredData.filter(item => item.type === filters.type);
  }
  
  // 学科筛选
  if (filters.subject) {
    filteredData = filteredData.filter(item => item.subject === filters.subject);
  }
  
  // 年级筛选
  if (filters.grade) {
    filteredData = filteredData.filter(item => item.grade === filters.grade);
  }
  
  // 难度筛选
  if (filters.difficulty) {
    filteredData = filteredData.filter(item => item.difficulty === filters.difficulty);
  }
  
  // 更新总数
  pagination.total = filteredData.length;
  
  // 应用分页
  const startIndex = (pagination.currentPage - 1) * pagination.pageSize;
  const endIndex = startIndex + pagination.pageSize;
  questions.value = filteredData.slice(startIndex, endIndex);
};

// 打开上传对话框
const openUploadDialog = () => {
  uploadDialogVisible.value = true;
};

// 上传题目
const uploadQuestion = async () => {
  if (!uploadForm.title || !uploadForm.chapter || !uploadForm.subject|| !uploadForm.chapter) {
    ElMessage.warning('请填写必填项');
    return;
  }
  try {
    const questionData = {
      title: uploadForm.title,
      chapter: uploadForm.chapter,
      subject: uploadForm.subject,
      difficulty: uploadForm.difficulty,
      optionA: uploadForm.optionA,
      optionB: uploadForm.optionB,
      optionC: uploadForm.optionC,
      optionD: uploadForm.optionD,
      answer: uploadForm.answer,
    };
    
    const response = await questionService.addQuestion(questionData);
    
    if (response.code === 200) {
      ElMessage.success('添加题目成功');
      // 重置表单
      uploadForm.title = '';
      uploadForm.chapter = '';
      uploadForm.subject = '';
      uploadForm.optionA = '';
      uploadForm.difficulty = '';
      uploadForm.optionB = '';
      uploadForm.optionC = '';
      uploadForm.answer = '';
      uploadForm.optionD = '';
      
      // 关闭对话框
      uploadDialogVisible.value = false;
      
      // 刷新列表
      await fetchQuestions();
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
  // 题目内容
  previewHtml += `<div class="question-content">
                    <p>${question.title || '无标题'}</p>
                    <p>${question.content || ''}</p>
                  </div>`;
  
  // 选项展示
  if (question.optionA || question.optionB || question.optionC || question.optionD) {
    previewHtml += `<div class="question-options">`;

    if (question.optionA) {
      previewHtml += `<div class="option"><span>A.</span> ${question.optionA}</div>`;
    }

    if (question.optionB) {
      previewHtml += `<div class="option"><span>B.</span> ${question.optionB}</div>`;
    }

    if (question.optionC) {
      previewHtml += `<div class="option"><span>C.</span> ${question.optionC}</div>`;
    }

    if (question.optionD) {
      previewHtml += `<div class="option"><span>D.</span> ${question.optionD}</div>`;
    }

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

  // 章节和难度信息

  
  // 标准答案和解析
  previewHtml += `<div class="question-answer">
                    <div class="answer-header">参考答案：</div>
                    <div class="answer-content">${question.answer || '暂无答案'}</div>
                  </div>
                  `;
  previewHtml += `<div class="question-meta">
                    <div><strong>创建时间：</strong>${formatDate(question.createdAt)}</div>
                  </div>`;
  previewHtml += `</div>`;
  
  return previewHtml;
};

// 格式化难度显示
const getDifficultyText = (difficulty) => {
  if (!difficulty) return '未知';
  
  // 如果difficulty是数字
  if (!isNaN(difficulty)) {
    const diffLevel = parseInt(difficulty);
    switch (diffLevel) {
      case 1: return '简单';
      case 2: return '中等';
      case 3: return '困难';
      default: return `${difficulty}`;
    }
  }
  
  return difficulty;
};

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return '未知';
  
  try {
    const date = new Date(dateString);
    return date.toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
  } catch (e) {
    return dateString;
  }
};
// 获取难度标签类型
const getDifficultyTagType = (difficulty) => {
  if (!difficulty) return '';
  
  // 如果difficulty是数字
  if (!isNaN(difficulty)) {
    const diffLevel = parseInt(difficulty);
    switch (diffLevel) {
      case 1: return 'success'; // 简单
      case 2: return 'warning'; // 中等
      case 3: return 'danger';  // 困难
      default: return '';       // 默认
    }
  }
// 如果difficulty是字符串
  if (typeof difficulty === 'string') {
    // 尝试转换为数字（处理"1"这样的字符串数字）
    const num = parseInt(difficulty, 10);
    if (!isNaN(num)) {
      const diffLevel = num;
      switch (diffLevel) {
        case 1: return 'success'; // 简单
        case 2: return 'warning'; // 中等
        case 3: return 'danger';  // 困难
        default: return '';
      }
    }
    // 转换失败则检查关键词
    if (difficulty.includes('简单')) return 'success';
    if (difficulty.includes('中等')) return 'warning';
    if (difficulty.includes('困难')) return 'danger';
  }
  return '';
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
 const fileList = ref([]);
const importLoading = ref(false);

// 打开导入对话框
const openImportDialog = () => {
  importDialogVisible.value = true;
};

// 导入题目
const importQuestions = async () => {
  console.log('开始导入流程');
  if (!importFile.value) {
    ElMessage.warning('请选择要导入的Excel文件');
    return;
  }
  
  console.log('文件对象详情:', importFile.value);
  console.log('文件类型:', importFile.value.type);
  console.log('文件名:', importFile.value.name);
  console.log('文件大小:', importFile.value.size, '字节');
  
  // 验证文件类型
  const allowedTypes = ['application/vnd.ms-excel', 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'];
  const fileExt = importFile.value.name.split('.').pop().toLowerCase();
  console.log('文件扩展名:', fileExt);
  
  if (!allowedTypes.includes(importFile.value.type) && 
      fileExt !== 'xlsx' && fileExt !== 'xls') {
    ElMessage.error(`请选择Excel文件（.xls或.xlsx格式），当前文件类型: ${importFile.value.type}`);
    return;
  }
  
  // 验证文件大小（限制10MB）
  const maxSize = 10 * 1024 * 1024;
  if (importFile.value.size > maxSize) {
    ElMessage.error(`文件大小不能超过10MB，当前大小: ${(importFile.value.size / 1024 / 1024).toFixed(2)}MB`);
    return;
  }
  
  try {
    importLoading.value = true;
    ElMessage.info('正在处理文件，请稍候...');
    
    // 创建新的FormData对象
    const formData = new FormData();
    
    // 确保使用正确的字段名'file'，这必须与后端期望的字段名一致
    formData.append('file', importFile.value, importFile.value.name);
    
    // 验证FormData内容
    console.log('FormData内容:');
    for (let pair of formData.entries()) {
      console.log(pair[0] + ':', pair[1]);
      if (pair[0] === 'file') {
        console.log('file字段类型:', Object.prototype.toString.call(pair[1]));
        console.log('file字段名称:', pair[1].name);
      }
    }
    
    console.log('开始发送请求...');
    
    // 使用questionService发送请求
    const response = await questionService.importQuestions(formData);
    
    console.log('请求响应:', response);
    
    if (response.code === 200) {
      ElMessage.success(`成功导入题目`);
      importDialogVisible.value = false;
      importFile.value = null;
      fileList.value = [];
      fetchQuestions();
    } else {
      console.error('导入失败，服务器返回:', response);
      ElMessage.error(response.message || '导入失败');
    }
  } catch (error) {
    console.error('导入题目失败:', error);
    // 详细记录错误信息
    if (error.response) {
      console.error('错误响应状态:', error.response.status);
      console.error('错误响应头:', error.response.headers);
      console.error('错误响应数据:', error.response.data);
      ElMessage.error(`导入失败: ${error.response.data?.message || error.message || '请检查文件格式'}`);
    } else if (error.request) {
      console.error('请求已发送但没有收到响应');
      console.error('请求对象:', error.request);
      ElMessage.error('服务器无响应，请检查网络连接');
    } else {
      console.error('请求配置错误:', error.message);
      ElMessage.error(`请求错误: ${error.message}`);
    }
  } finally {
    importLoading.value = false;
  }
};

// 处理文件上传
const handleFileChange = (file) => {
  console.log('文件选择事件:', file);
  console.log('文件对象结构:', JSON.stringify(file, (key, value) => {
    if (key === 'raw') return '[File对象]';
    return value;
  }));
  
  // 当auto-upload为false时，file.raw包含实际的File对象
  if (file && file.raw) {
    console.log('文件raw对象类型:', Object.prototype.toString.call(file.raw));
    console.log('文件raw是否为File:', file.raw instanceof File);
    console.log('文件raw是否为Blob:', file.raw instanceof Blob);
    console.log('文件raw名称:', file.raw.name);
    console.log('文件raw类型:', file.raw.type);
    console.log('文件raw大小:', file.raw.size);
    
    importFile.value = file.raw;
    fileList.value = [{ name: file.raw.name, size: file.raw.size }];
    console.log('文件已设置:', importFile.value.name);
  } else if (file && file.name) {
    // 直接使用file对象
    console.log('直接使用file对象');
    importFile.value = file;
    fileList.value = [{ name: file.name, size: file.size }];
    console.log('文件已设置(直接):', importFile.value.name);
  } else {
    console.error('文件对象无效:', file);
    importFile.value = null;
    fileList.value = [];
  }
};

// 自定义上传方法
const customUpload = (options) => {
  console.log('自定义上传方法被调用:', options);
  console.log('上传文件对象类型:', Object.prototype.toString.call(options.file));
  console.log('上传文件是否为File:', options.file instanceof File);
  console.log('上传文件是否为Blob:', options.file instanceof Blob);
  
  // 这里不执行实际上传，只保存文件对象
  if (options.file) {
    importFile.value = options.file;
    fileList.value = [{ name: options.file.name, size: options.file.size }];
    console.log('通过customUpload设置文件:', options.file.name);
    
    // 模拟成功回调，通知el-upload组件上传成功
    if (options.onSuccess) {
      options.onSuccess('上传成功');
    }
  } else if (options.onError) {
    options.onError('文件对象无效');
  }
};
// 批量下载选中题目
const multipleSelection = ref([]);
const handleSelectionChange = (selection) => {
  multipleSelection.value = selection;
};

// 下载Excel导入模板
const downloadTemplate = () => {
  try {
    // 创建一个工作簿对象
    const workbook = XLSX.utils.book_new();
    
    // 创建表头数据（符合后端期望的字段）
    const headers = ['title', 'optionA', 'optionB', 'optionC', 'optionD', 'answer', 'subject', 'chapter', 'difficulty'];
    
    // 创建示例数据
    const exampleData = [
      {
        title: '1+1=?',
        optionA: '1',
        optionB: '2',
        optionC: '3',
        optionD: '4',
        answer: 'B',
        subject: '数学',
        chapter: '加法',
        difficulty: '简单'
      }
    ];
    
    // 将数据转换为工作表
    const worksheet = XLSX.utils.json_to_sheet(exampleData, { header: headers });
    
    // 将工作表添加到工作簿
    XLSX.utils.book_append_sheet(workbook, worksheet, '题目导入模板');
    
    // 将工作簿转换为二进制数据
    const excelData = XLSX.write(workbook, { type: 'array', bookType: 'xlsx' });
    
    // 创建Blob对象
    const blob = new Blob([excelData], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
    
    // 创建下载链接
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = '题目导入模板.xlsx';
    document.body.appendChild(a);
    a.click();

    // 清理
    setTimeout(() => {
      document.body.removeChild(a);
      URL.revokeObjectURL(url);
    }, 0);
    
    ElMessage.success('模板下载成功');
  } catch (error) {
    console.error('下载模板失败:', error);
    ElMessage.error('下载模板失败，请稍后再试');
  }
};

// 编辑题目方法
const editQuestion = (question) => {
  // 填充表单数据
  editForm.id = question.id;
  editForm.title = question.title;
  editForm.type = question.type;
  editForm.subject = question.subject;
  editForm.chapter = question.chapter;
  editForm.difficulty = question.difficulty;
  editForm.optionA = question.optionA;
  editForm.optionB = question.optionB;
  editForm.optionC = question.optionC;
  editForm.optionD = question.optionD;
  editForm.answer = question.answer;
  
  // 显示编辑对话框
  editDialogVisible.value = true;
};

// 提交编辑
const submitEdit = async () => {
  if (!editForm.title || !editForm.chapter || !editForm.subject) {
    ElMessage.warning('请填写必填项');
    return;
  }
  
  try {
    const response = await questionService.updateQuestion({
      id: editForm.id,
      title: editForm.title,
      chapter: editForm.chapter,
      subject: editForm.subject,
      difficulty: editForm.difficulty,
      optionA: editForm.optionA,
      optionB: editForm.optionB,
      optionC: editForm.optionC,
      optionD: editForm.optionD,
      answer: editForm.answer
    });
    
    if (response.code === 200) {
      ElMessage.success('修改题目成功');
      editDialogVisible.value = false;
      fetchQuestions(); // 刷新题目列表
    } else {
      ElMessage.error(response.message || '修改题目失败');
    }
  } catch (error) {
    console.error('修改题目失败:', error);
    ElMessage.error('修改题目失败，请稍后再试');
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
          <el-select v-model="filters.subject" placeholder="课程" clearable>
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
        <el-table-column prop="chapter" label="章节" width="100" show-overflow-tooltip />
        <el-table-column prop="difficulty" label="难度" width="80">
          <template #default="scope">
            <el-tag 
              :type="getDifficultyTagType(scope.row.difficulty)"
              size="small"
            >
              {{ getDifficultyText(scope.row.difficulty) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="150">
          <template #default="scope">
            {{ formatDate(scope.row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220">
          <template #default="scope">
            <el-button size="small" type="primary" @click="editQuestion(scope.row)">
              <el-icon><Edit /></el-icon> 修改
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
            <el-form-item label="学科">
              <el-input v-model="uploadForm.subject" placeholder="请输入课程" style="width: 100%">
              </el-input>
            </el-form-item>
        <el-form-item label="课程" required>
          <el-input v-model="uploadForm.chapter" placeholder="请输入章节" style="width: 100%">
          </el-input>
        </el-form-item>
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
        <el-form-item label="题目内容" required>
          <el-input 
            v-model="uploadForm.title"
            type="textarea" 
            :rows="4" 
            placeholder="请输入题目内容"
          />
        </el-form-item>
        <el-form-item label="选项A">
              <el-input v-model="uploadForm.optionA" placeholder="请输入答案A" style="width: 100%">
              </el-input>
            </el-form-item>
        <el-form-item label="选项B">
              <el-input v-model="uploadForm.optionB" placeholder="请输入答案B" style="width: 100%">
              </el-input>
            </el-form-item>
        <el-form-item label="选项C">
              <el-input v-model="uploadForm.optionC" placeholder="请输入答案C" style="width: 100%">
              </el-input>
            </el-form-item>
        <el-form-item label="选项D">
              <el-input v-model="uploadForm.optionD" placeholder="请输入答案D" style="width: 100%">
              </el-input>
        </el-form-item>
        <el-form-item label="标准答案">
              <el-select v-model="uploadForm.answer" placeholder="请选择正确答案" style="width: 100%">
                <el-option
                    v-for="level in answers"
                    :key="level.value"
                    :label="level.label"
                    :value="level.value"
                />
              </el-select>
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
        :http-request="customUpload"
        :show-file-list="true"
        :file-list="fileList"
        :multiple="false"
        drag
      >
        <template #trigger>
          <el-button type="primary">选择Excel文件</el-button>
        </template>
        <template #tip>
          <div class="el-upload__tip">
            请上传符合模板格式的Excel文件，<a href="#" @click.prevent="downloadTemplate">点击下载模板</a>
          </div>
        </template>
      </el-upload>
      
      <div v-if="importFile" class="selected-file-info" style="margin-top: 15px; padding: 10px; background-color: #f5f7fa; border-radius: 4px;">
        <p><strong>已选择文件:</strong> {{ importFile.name }}</p>
        <p><strong>文件大小:</strong> {{ (importFile.size / 1024).toFixed(2) }} KB</p>
        <p><strong>文件类型:</strong> {{ importFile.type || '未知' }}</p>
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="importDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="importQuestions" :loading="importLoading">导入</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 预览题目对话框 -->
    <el-dialog v-model="previewDialogVisible" title="题目预览" width="70%" destroy-on-close>
      <div v-if="currentQuestion" class="question-preview-container">
        <div class="preview-header">
          <h3>{{ currentQuestion.title }}</h3>
          <div class="preview-info">
            <span>学科：{{ currentQuestion.subject }}</span>
            <span>章节：{{ currentQuestion.chapter }}</span>
            <span>难度：{{ getDifficultyText(currentQuestion.difficulty) }}</span>
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

    <!-- 编辑题目对话框 -->
    <el-dialog v-model="editDialogVisible" title="编辑题目" width="60%">
      <el-form :model="editForm" label-width="100px">
        <el-form-item label="学科">
          <el-input v-model="editForm.subject" placeholder="请输入课程" style="width: 100%">
          </el-input>
        </el-form-item>
        <el-form-item label="课程" required>
          <el-input v-model="editForm.chapter" placeholder="请输入章节" style="width: 100%">
          </el-input>
        </el-form-item>
        <el-form-item label="难度">
          <el-select v-model="editForm.difficulty" placeholder="请选择难度" style="width: 100%">
            <el-option
              v-for="level in difficultyLevels"
              :key="level.value"
              :label="level.label"
              :value="level.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="题目内容" required>
          <el-input 
            v-model="editForm.title"
            type="textarea" 
            :rows="4" 
            placeholder="请输入题目内容"
          />
        </el-form-item>
        <el-form-item label="选项A">
          <el-input v-model="editForm.optionA" placeholder="请输入答案A" style="width: 100%">
          </el-input>
        </el-form-item>
        <el-form-item label="选项B">
          <el-input v-model="editForm.optionB" placeholder="请输入答案B" style="width: 100%">
          </el-input>
        </el-form-item>
        <el-form-item label="选项C">
          <el-input v-model="editForm.optionC" placeholder="请输入答案C" style="width: 100%">
          </el-input>
        </el-form-item>
        <el-form-item label="选项D">
          <el-input v-model="editForm.optionD" placeholder="请输入答案D" style="width: 100%">
          </el-input>
        </el-form-item>
        <el-form-item label="标准答案">
          <el-select v-model="editForm.answer" placeholder="请选择正确答案" style="width: 100%">
            <el-option
              v-for="level in answers"
              :key="level.value"
              :label="level.label"
              :value="level.value"
            />
          </el-select>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitEdit">保存</el-button>
        </span>
      </template>
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