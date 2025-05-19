<script setup>
import { ref, reactive, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { userService } from '../services';
import {Plus} from "@element-plus/icons-vue";
import {cleanupImageUrl, handleFileStream} from "@/services/fileUtils.js";

const userForm = reactive({
  username: '',
  realName: '',
  email: '',
  phone: '',
  department: '',
  position: '',
  avatar: ''
});
// 加载中状态
const loading = ref(false);
// 从文件流获取头像
const avatarLoaded = ref(false);
const fetchAvatarByStream = async (avatarPath) => {
  if (!avatarPath) return;

  try {
    avatarLoaded.value = false;
    // 调用文件流处理方法
    debugger
    const streamUrl = await handleFileStream(avatarPath, {
      headers: {
        Authorization: `Bearer ${token}`
      },
      onError: (error) => {
        console.error('加载头像失败:', error);
        ElMessage.error('加载头像失败，请稍后再试');
      }
    });

    if (streamUrl) {
      // 清理之前的URL（如果有）
      if (avatarUrl.value && avatarUrl.value.startsWith('blob:')) {
        cleanupImageUrl(avatarUrl.value);
      }

      avatarUrl.value = streamUrl;
    }
  } catch (error) {
    console.error('处理头像文件流时出错:', error);
    ElMessage.error('加载头像失败，请稍后再试');
  } finally {
    avatarLoaded.value = true;
  }
};

// 获取用户信息
const fetchUserInfo = async () => {
  try {
    loading.value = true;
    const response = await userService.getUserInfo();
    if (response.code === 200) {
      Object.assign(userForm, response.data);
      if (response.data.avatar) {
        debugger
        await fetchAvatarByStream(response.data.avatar);
        debugger
      }
      ElMessage.success('用户信息加载成功');

    } else {
      ElMessage.error(response.message || '加载用户信息失败');
    }
  } catch (error) {
    console.error('加载用户信息失败:', error);
    ElMessage.error('加载用户信息失败，请稍后再试');
  } finally {
    loading.value = false;
  }
};

// 组件挂载时加载用户信息
onMounted(() => {
  fetchUserInfo();
});
const token=localStorage.getItem('token');
const avatarUrl = ref('');

const handleAvatarSuccess = async(response) => {
  if (response.code === 200) {
    avatarUrl.value = response.data;
    userForm.avatar = response.data;
    // 修改为传递对象，确保参数名为avatarUrl
    const result = await userService.uploadAvatar({avatar:response.data,
    username: userForm.username});
    if (result.code === 200) {
      ElMessage.success('头像更新成功');
      await fetchAvatarByStream(response.data);
    } else {
      ElMessage.error(result.message || '更新头像失败');
    }
  } else {
    ElMessage.error(response.message || '传递头像URL失败');
  }

};

const beforeAvatarUpload = (file) => {
  const isJPG = file.type === 'image/jpeg' || file.type === 'image/png';
  const isLt2M = file.size / 1024 / 1024 < 2;

  if (!isJPG) {
    ElMessage.error('头像图片只能是 JPG 或 PNG 格式!');
  }
  if (!isLt2M) {
    ElMessage.error('头像图片大小不能超过 2MB!');
  }
  return isJPG && isLt2M;
};

const saveProfile = async () => {
  try {
    loading.value = true;
    const response = await userService.updateUserInfo(userForm);
    if (response.code === 200) {
      ElMessage.success('个人信息保存成功');
    } else {
      ElMessage.error(response.message || '保存个人信息失败');
    }
  } catch (error) {
    console.error('保存个人信息失败:', error);
    ElMessage.error('保存个人信息失败，请稍后再试');
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <div class="user-profile-container">
    <div class="profile-header">
      <h2 class="profile-title">个人信息</h2>
      <p class="profile-subtitle">查看和编辑您的个人资料</p>
    </div>

    <el-card class="profile-card">
      <el-form :model="userForm" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="avatar-container">
              <el-upload
                class="avatar-uploader"
                action="/api/file/upload"
                name="file"
                :headers="{'Authorization':`Bearer ${token}`}"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload"
              >
                <img v-if="avatarUrl" :src="avatarUrl" class="avatar"  alt=""/>
                <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
              </el-upload>
              <p class="avatar-tip">点击上传头像</p>
            </div>
          </el-col>
          <el-col :span="16">
            <el-form-item label="用户名">
              <el-input v-model="userForm.username" disabled></el-input>
            </el-form-item>
            <el-form-item label="真实姓名">
              <el-input v-model="userForm.realName"></el-input>
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="userForm.email"></el-input>
            </el-form-item>
            <el-form-item label="手机号码">
              <el-input v-model="userForm.phone"></el-input>
            </el-form-item>
            <el-form-item label="所属部门">
              <el-input v-model="userForm.department"></el-input>
            </el-form-item>
            <el-form-item label="职位">
              <el-input v-model="userForm.position"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item>
          <el-button type="primary" @click="saveProfile" :loading="loading">保存信息</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
.user-profile-container {
  padding: 20px;
}

.profile-header {
  margin-bottom: 30px;
}

.profile-title {
  font-size: 24px;
  color: #303133;
  margin-bottom: 10px;
}

.profile-subtitle {
  font-size: 14px;
  color: #909399;
}

.profile-card {
  margin-bottom: 20px;
}

.avatar-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.avatar-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 178px;
  height: 178px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.avatar-uploader:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}

.avatar-tip {
  margin-top: 10px;
  color: #909399;
  font-size: 12px;
}
</style>