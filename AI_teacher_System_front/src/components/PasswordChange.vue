<script setup>
import { ref, reactive } from 'vue';
import { ElMessage } from 'element-plus';
import { userService } from '../services';

const passwordForm = reactive({
  currentPassword: '',
  newPassword: '',
  confirmPassword: ''
});

const rules = {
  currentPassword: [
    { required: true, message: '请输入当前密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== passwordForm.newPassword) {
          callback(new Error('两次输入的密码不一致'));
        } else {
          callback();
        }
      },
      trigger: 'blur'
    }
  ]
};

const formRef = ref(null);

// 加载状态
const loading = ref(false);

const submitForm = async () => {
  if (!formRef.value) return;
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        loading.value = true;
        const passwordData = {
          currentPassword: passwordForm.currentPassword,
          newPassword: passwordForm.newPassword
        };
        
        const response = await userService.changePassword(passwordData);
        if (response.code === 200) {
          ElMessage.success('密码修改成功');
          // 重置表单
          passwordForm.currentPassword = '';
          passwordForm.newPassword = '';
          passwordForm.confirmPassword = '';
        } else {
          ElMessage.error(response.message || '密码修改失败');
        }
      } catch (error) {
        console.error('密码修改失败:', error);
        ElMessage.error('密码修改失败，请稍后再试');
      } finally {
        loading.value = false;
      }
    } else {
      ElMessage.error('请正确填写表单');
      return false;
    }
  });
};
</script>

<template>
  <div class="password-change-container">
    <div class="password-header">
      <h2 class="password-title">修改密码</h2>
      <p class="password-subtitle">定期更改密码可以提高账户安全性</p>
    </div>

    <el-card class="password-card">
      <el-form
        ref="formRef"
        :model="passwordForm"
        :rules="rules"
        label-width="100px"
        class="password-form"
      >
        <el-form-item label="当前密码" prop="currentPassword">
          <el-input
            v-model="passwordForm.currentPassword"
            type="password"
            placeholder="请输入当前密码"
            show-password
          ></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input
            v-model="passwordForm.newPassword"
            type="password"
            placeholder="请输入新密码"
            show-password
          ></el-input>
        </el-form-item>
        <el-form-item label="确认新密码" prop="confirmPassword">
          <el-input
            v-model="passwordForm.confirmPassword"
            type="password"
            placeholder="请再次输入新密码"
            show-password
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm" :loading="loading">确认修改</el-button>
          <el-button @click="formRef?.resetFields()">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="password-tips-card">
      <template #header>
        <div class="card-header">
          <span>密码安全提示</span>
        </div>
      </template>
      <div class="password-tips">
        <p><el-icon><InfoFilled /></el-icon> 密码长度至少6个字符</p>
        <p><el-icon><InfoFilled /></el-icon> 建议使用字母、数字和特殊字符的组合</p>
        <p><el-icon><InfoFilled /></el-icon> 避免使用容易被猜到的信息，如生日、姓名等</p>
        <p><el-icon><InfoFilled /></el-icon> 定期更换密码可以提高账户安全性</p>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.password-change-container {
  padding: 20px;
}

.password-header {
  margin-bottom: 30px;
}

.password-title {
  font-size: 24px;
  color: #303133;
  margin-bottom: 10px;
}

.password-subtitle {
  font-size: 14px;
  color: #909399;
}

.password-card {
  margin-bottom: 20px;
}

.password-form {
  max-width: 500px;
}

.password-tips-card {
  max-width: 500px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.password-tips p {
  margin: 10px 0;
  display: flex;
  align-items: center;
}

.password-tips .el-icon {
  margin-right: 8px;
  color: #409EFF;
}
</style>