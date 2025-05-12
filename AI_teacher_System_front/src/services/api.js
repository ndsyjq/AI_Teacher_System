import axios from 'axios';

// 创建axios实例
const api = axios.create({
  baseURL: '',  // 移除'/api'前缀，避免与Vite代理路径重复
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
});

// 请求拦截器
api.interceptors.request.use(
  config => {
    // 可以在这里添加token等认证信息
    const token = localStorage.getItem('token');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    
    // 处理请求数据，避免循环引用问题
    if (config.data) {
      // 创建一个深拷贝函数，处理循环引用
      const getPlainObject = (obj) => {
        if (obj === null || typeof obj !== 'object') {
          return obj;
        }
        
        // 处理数组
        if (Array.isArray(obj)) {
          return obj.map(item => getPlainObject(item));
        }
        
        // 处理普通对象，排除Vue响应式对象的特殊属性
        const result = {};
        Object.keys(obj).forEach(key => {
          // 跳过Vue响应式对象的内部属性
          if (key === '__v_isRef' || key === '__v_isShallow' || 
              key === '__v_raw' || key === 'dep' || key === '__v_reactive') {
            return;
          }
          
          // 获取属性值
          let value = obj[key];
          
          // 处理Vue的ref对象
          if (value && typeof value === 'object' && 'value' in value && Object.keys(value).length === 1) {
            value = value.value;
          }
          
          result[key] = getPlainObject(value);
        });
        
        return result;
      };
      
      // 应用转换
      config.data = getPlainObject(config.data);
    }
    
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

// 响应拦截器
api.interceptors.response.use(
  response => {
    // 如果返回的状态码为200，说明接口请求成功，可以正常拿到数据
    if (response.status === 200) {
      return response.data;
    }
    return response;
  },
  error => {
    if (error.response && error.response.status) {
      switch (error.response.status) {
        case 401:
          // 未登录或token过期
          // 可以在这里处理登出逻辑
          break;
        case 403:
          // 没有权限
          break;
        case 404:
          // 请求不存在
          break;
        case 500:
          // 服务器错误
          break;
        default:
          // 其他错误
          break;
      }
    }
    return Promise.reject(error);
  }
);

export default api;