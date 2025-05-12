<script setup>
import { ref, reactive, computed } from 'vue';

// 资源分类
const resourceCategories = ref([
  { id: 1, name: '教案' },
  { id: 2, name: '课件' },
  { id: 3, name: '试题' },
  { id: 4, name: '教学视频' },
  { id: 5, name: '教学素材' }
]);

// 模拟资源数据
const resources = ref([
  {
    id: 1,
    name: '高中数学必修一第一章教案',
    category: '教案',
    subject: '数学',
    grade: '高一',
    format: 'DOCX',
    size: '2.5MB',
    uploadTime: '2023-06-01',
    downloads: 56
  },
  {
    id: 2,
    name: '牛顿运动定律PPT',
    category: '课件',
    subject: '物理',
    grade: '高一',
    format: 'PPTX',
    size: '5.8MB',
    uploadTime: '2023-06-02',
    downloads: 42
  },
  {
    id: 3,
    name: '语文阅读理解练习题',
    category: '试题',
    subject: '语文',
    grade: '高二',
    format: 'PDF',
    size: '1.2MB',
    uploadTime: '2023-06-03',
    downloads: 78
  },
  {
    id: 4,
    name: '化学实验演示视频',
    category: '教学视频',
    subject: '化学',
    grade: '高二',
    format: 'MP4',
    size: '45.6MB',
    uploadTime: '2023-06-04',
    downloads: 35
  },
  {
    id: 5,
    name: '地理地形图素材集',
    category: '教学素材',
    subject: '地理',
    grade: '高一',
    format: 'ZIP',
    size: '32.1MB',
    uploadTime: '2023-06-05',
    downloads: 28
  }
]);

// 搜索关键词
const searchKeyword = ref('');

// 筛选条件
const filters = reactive({
  category: '',
  subject: '',
  grade: ''
});

// 上传对话框可见性
const uploadDialogVisible = ref(false);
// 预览对话框可见性
const previewDialogVisible = ref(false);
// 下载进度对话框可见性
const downloadProgressVisible = ref(false);

// 当前选中的资源
const currentResource = ref(null);
// 下载进度
const downloadProgress = ref(0);
// 下载状态
const downloadStatus = ref('');

// 上传表单
const uploadForm = reactive({
  name: '',
  category: '',
  subject: '',
  grade: '',
  description: '',
  file: null
});

// 打开上传对话框
const openUploadDialog = () => {
  uploadDialogVisible.value = true;
};

// 上传资源
const uploadResource = () => {
  if (!uploadForm.name || !uploadForm.category) {
    // 这里应该有表单验证
    return;
  }
  
  // 模拟上传操作
  resources.value.push({
    id: resources.value.length + 1,
    name: uploadForm.name,
    category: uploadForm.category,
    subject: uploadForm.subject,
    grade: uploadForm.grade,
    format: 'PDF', // 模拟文件格式
    size: '1.0MB', // 模拟文件大小
    uploadTime: new Date().toISOString().split('T')[0],
    downloads: 0
  });
  
  // 重置表单
  uploadForm.name = '';
  uploadForm.category = '';
  uploadForm.subject = '';
  uploadForm.grade = '';
  uploadForm.description = '';
  uploadForm.file = null;
  
  // 关闭对话框
  uploadDialogVisible.value = false;
};

// 下载资源
const downloadResource = (resource) => {
  // 设置当前资源
  currentResource.value = resource;
  
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
      
      // 模拟增加下载次数
      const resourceIndex = resources.value.findIndex(item => item.id === resource.id);
      if (resourceIndex !== -1) {
        resources.value[resourceIndex].downloads += 1;
      }
      
      // 延迟关闭对话框
      setTimeout(() => {
        downloadProgressVisible.value = false;
        
        // 模拟文件下载
        if (resource.format === 'PDF') {
          alert('PDF文件已下载，请在浏览器下载栏查看');
        } else if (resource.format === 'DOCX' || resource.format === 'DOC') {
          alert('Word文档已下载，请在浏览器下载栏查看');
        } else if (resource.format === 'PPTX' || resource.format === 'PPT') {
          alert('PowerPoint文件已下载，请在浏览器下载栏查看');
        } else if (resource.format === 'MP4' || resource.format === 'AVI') {
          alert('视频文件已下载，请在浏览器下载栏查看');
        } else {
          alert('文件已下载，请在浏览器下载栏查看');
        }
      }, 500);
    }
  }, 300);
};

// 预览资源
const previewResource = (resource) => {
  // 设置当前资源
  currentResource.value = resource;
  
  // 显示预览对话框
  previewDialogVisible.value = true;
};

// 获取资源预览内容
const getPreviewContent = (resource) => {
  if (!resource) return '';
  
  // 根据资源类型返回不同的预览内容
  if (resource.format === 'PDF') {
    return `<div class="preview-placeholder">
              <i class="el-icon-document"></i>
              <p>PDF文档预览</p>
              <p>${resource.name}</p>
            </div>`;
  } else if (resource.format === 'DOCX' || resource.format === 'DOC') {
    return `<div class="preview-placeholder">
              <i class="el-icon-document"></i>
              <p>Word文档预览</p>
              <p>${resource.name}</p>
            </div>`;
  } else if (resource.format === 'PPTX' || resource.format === 'PPT') {
    return `<div class="preview-placeholder">
              <i class="el-icon-document"></i>
              <p>PowerPoint预览</p>
              <p>${resource.name}</p>
            </div>`;
  } else if (resource.format === 'MP4' || resource.format === 'AVI') {
    return `<div class="preview-video">
              <video controls style="width: 100%; max-height: 400px;">
                <source src="#" type="video/mp4">
                您的浏览器不支持视频播放
              </video>
            </div>`;
  } else if (resource.format === 'JPG' || resource.format === 'PNG') {
    return `<div class="preview-image">
              <img src="#" alt="${resource.name}" style="max-width: 100%; max-height: 400px;">
            </div>`;
  } else {
    return `<div class="preview-placeholder">
              <i class="el-icon-document"></i>
              <p>无法预览此类型文件</p>
              <p>${resource.name}</p>
            </div>`;
  }
};

// 根据筛选条件过滤资源
const filteredResources = computed(() => {
  return resources.value.filter(resource => {
    // 关键词搜索
    if (searchKeyword.value && !resource.name.includes(searchKeyword.value)) {
      return false;
    }
    
    // 分类筛选
    if (filters.category && resource.category !== filters.category) {
      return false;
    }
    
    // 学科筛选
    if (filters.subject && resource.subject !== filters.subject) {
      return false;
    }
    
    // 年级筛选
    if (filters.grade && resource.grade !== filters.grade) {
      return false;
    }
    
    return true;
  });
});
</script>

<template>
  <div class="resource-library">
    <h2>资源库</h2>
    
    <el-card>
      <div class="search-bar">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索资源..."
          class="search-input"
          clearable
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        
        <el-select v-model="filters.category" placeholder="资源分类" clearable>
          <el-option
            v-for="category in resourceCategories"
            :key="category.id"
            :label="category.name"
            :value="category.name"
          />
        </el-select>
        
        <el-select v-model="filters.subject" placeholder="学科" clearable>
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
        
        <el-select v-model="filters.grade" placeholder="年级" clearable>
          <el-option label="初一" value="初一" />
          <el-option label="初二" value="初二" />
          <el-option label="初三" value="初三" />
          <el-option label="高一" value="高一" />
          <el-option label="高二" value="高二" />
          <el-option label="高三" value="高三" />
        </el-select>
        
        <el-button type="primary" @click="openUploadDialog">
          <el-icon><Upload /></el-icon> 上传资源
        </el-button>
      </div>
      
      <el-table :data="filteredResources" style="width: 100%">
        <el-table-column prop="name" label="资源名称" min-width="200" />
        <el-table-column prop="category" label="分类" width="100" />
        <el-table-column prop="subject" label="学科" width="80" />
        <el-table-column prop="grade" label="年级" width="80" />
        <el-table-column prop="format" label="格式" width="80" />
        <el-table-column prop="size" label="大小" width="80" />
        <el-table-column prop="uploadTime" label="上传时间" width="120" />
        <el-table-column prop="downloads" label="下载次数" width="100" />
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button size="small" type="primary" @click="downloadResource(scope.row)">
              <el-icon><Download /></el-icon> 下载
            </el-button>
            <el-button size="small" @click="previewResource(scope.row)">
              <el-icon><View /></el-icon> 预览
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 上传资源对话框 -->
    <el-dialog v-model="uploadDialogVisible" title="上传资源" width="50%">
      <el-form :model="uploadForm" label-width="100px">
        <el-form-item label="资源名称" required>
          <el-input v-model="uploadForm.name" placeholder="请输入资源名称" />
        </el-form-item>
        
        <el-form-item label="资源分类" required>
          <el-select v-model="uploadForm.category" placeholder="请选择资源分类" style="width: 100%">
            <el-option
              v-for="category in resourceCategories"
              :key="category.id"
              :label="category.name"
              :value="category.name"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="学科">
          <el-select v-model="uploadForm.subject" placeholder="请选择学科" style="width: 100%">
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
        
        <el-form-item label="适用年级">
          <el-select v-model="uploadForm.grade" placeholder="请选择年级" style="width: 100%">
            <el-option label="初一" value="初一" />
            <el-option label="初二" value="初二" />
            <el-option label="初三" value="初三" />
            <el-option label="高一" value="高一" />
            <el-option label="高二" value="高二" />
            <el-option label="高三" value="高三" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="资源描述">
          <el-input 
            v-model="uploadForm.description" 
            type="textarea" 
            :rows="3" 
            placeholder="请输入资源描述"
          />
        </el-form-item>
        
        <el-form-item label="上传文件" required>
          <el-upload
            class="upload-demo"
            action="#"
            :auto-upload="false"
            :limit="1"
          >
            <template #trigger>
              <el-button type="primary">选择文件</el-button>
            </template>
            <template #tip>
              <div class="el-upload__tip">
                支持各种格式的文件，单个文件不超过100MB
              </div>
            </template>
          </el-upload>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="uploadDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="uploadResource">上传</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 预览资源对话框 -->
    <el-dialog v-model="previewDialogVisible" title="资源预览" width="70%" destroy-on-close>
      <div v-if="currentResource" class="resource-preview">
        <div class="preview-header">
          <h3>{{ currentResource.name }}</h3>
          <div class="preview-info">
            <span>格式：{{ currentResource.format }}</span>
            <span>大小：{{ currentResource.size }}</span>
            <span>上传时间：{{ currentResource.uploadTime }}</span>
          </div>
        </div>
        
        <div class="preview-content" v-html="getPreviewContent(currentResource)"></div>
        
        <div class="preview-footer">
          <el-button type="primary" @click="downloadResource(currentResource)">
            <el-icon><Download /></el-icon> 下载此资源
          </el-button>
        </div>
      </div>
    </el-dialog>
    
    <!-- 下载进度对话框 -->
    <el-dialog v-model="downloadProgressVisible" title="下载进度" width="30%" :close-on-click-modal="false" :show-close="false">
      <div v-if="currentResource" class="download-progress">
        <p>正在下载：{{ currentResource.name }}</p>
        <el-progress :percentage="downloadProgress" :status="downloadProgress === 100 ? 'success' : ''"></el-progress>
        <p class="download-status">{{ downloadStatus }}</p>
      </div>
    </el-dialog>
  </div>
</template>

<style scoped>
.resource-library {
  width: 100%; /* 确保组件宽度占满父容器 */
}

.search-bar {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.search-input {
  width: 300px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.resource-preview {
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
  justify-content: center;
  align-items: center;
  background-color: #f5f7fa;
  border-radius: 4px;
  padding: 20px;
}

.preview-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #909399;
}

.preview-placeholder i {
  font-size: 48px;
  margin-bottom: 10px;
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
</style>