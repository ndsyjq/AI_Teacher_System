// @/utils/fileUtils.js
import axios from 'axios';

// 创建可配置的 axios 实例
const createAxiosInstance = (baseURL = '', defaultHeaders = {}) => {
    const token = localStorage.getItem('token');
    const instance = axios.create({
        baseURL,
        timeout: 10000,
        headers: {
            'Content-Type': 'application/json',
            'Authorization' : `Bearer ${token}`,
            ...defaultHeaders
        }
    });

    // 添加响应拦截器（可选）
    instance.interceptors.response.use(
        (response) => response,
        (error) => {
            console.error('请求失败:', error);
            return Promise.reject(error);
        }
    );

    return instance;
};

/**
 * 处理文件流（使用可配置的 axios 实例）
 * @param {string} url - 请求地址
 * @param {Object} options - 配置项
 * @param {('blob'|'arraybuffer'|'json'|'text')} [options.responseType='blob'] - 响应类型
 * @param {Object} [options.headers={}] - 请求头
 * @param {string} [options.baseURL=''] - 基础 URL
 * @param {function} [options.onSuccess] - 成功回调
 * @param {function} [options.onError] - 错误回调
 * @returns {Promise<string|null>} 处理后的结果（blob URL、Base64 或文本）
 */
export async function handleFileStream(url, options = {}) {
    try {
        const {
            responseType = 'blob',
            headers = {},
            baseURL = '',
            onSuccess = () => {},
            onError = () => {}
        } = options;

        // 创建 axios 实例
        const axiosInstance = createAxiosInstance(`/api/user${baseURL}`, headers);

        // 发送请求
        const response = await axiosInstance.get(url, {
            responseType,
            headers: {
                'Accept': getAcceptHeader(responseType), // 根据响应类型设置 Accept 头
                ...headers // 合并用户自定义头
            }
        });

        // 检查 HTTP 状态码
        if (response.status < 200 || response.status >= 300) {
            throw new Error(`请求失败: ${response.status} ${response.statusText}`);
        }

        // 处理响应数据
        let result;
        switch (responseType) {
            case 'blob':
                result = URL.createObjectURL(response.data);
                break;

            case 'arraybuffer':
                result = arrayBufferToBase64(response.data);
                break;

            case 'json':
                result = response.data;
                break;

            case 'text':
                result = response.data;
                break;

            default:
                throw new Error(`不支持的响应类型: ${responseType}`);
        }

        onSuccess(result);
        return result;
    } catch (error) {
        console.error('处理文件流时出错:', error);
        onError({
            message: error.message,
            status: error.response?.status || null,
            data: error.response?.data || null
        });
        return null;
    }
}

/**
 * 清理 blob URL 以释放内存
 * @param {string} imageUrl - 待清理的 URL
 */
export function cleanupImageUrl(imageUrl) {
    if (typeof imageUrl === 'string' && imageUrl.startsWith('blob:')) {
        try {
            URL.revokeObjectURL(imageUrl);
        } catch (error) {
            console.warn('撤销 URL 失败:', error);
        }
    }
}

/**
 * 根据响应类型返回对应的 Accept 请求头
 * @param {string} responseType - 响应类型
 * @returns {string} Accept 头值
 */
function getAcceptHeader(responseType) {
    switch (responseType) {
        case 'blob':
            return 'application/octet-stream, image/*, */*';
        case 'arraybuffer':
            return 'application/octet-stream';
        case 'json':
            return 'application/json';
        case 'text':
            return 'text/plain';
        default:
            return '*/*';
    }
}

/**
 * 将 ArrayBuffer 转换为 Base64 字符串
 * @param {ArrayBuffer} buffer - 二进制数据
 * @returns {string} Base64 编码字符串
 */
function arrayBufferToBase64(buffer) {
    const bytes = new Uint8Array(buffer);
    const binary = Array.from(bytes, byte => String.fromCharCode(byte)).join('');
    return `data:application/octet-stream;base64,${btoa(binary)}`;
}