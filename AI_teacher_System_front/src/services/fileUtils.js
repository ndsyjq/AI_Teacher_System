// @/utils/fileUtils.js
import axios from 'axios';
import api from "@/services/api.js";

export async function handleFileStream(url, options = {}) {
    try {
        const {
            responseType = 'blob',
            headers = {},
            onSuccess = () => {},
            onError = () => {}
        } = options;

        const response = await api.get(url, {
            responseType,
            headers
        });

        if (response.status !== 200) {
            throw new Error(`请求失败: ${response.status}`);
        }

        let result;
        if (responseType === 'blob') {
            result = URL.createObjectURL(response.data);
        } else if (responseType === 'arraybuffer') {
            // 转换为Base64的逻辑保持不变
        }

        onSuccess(result);
        return result;
    } catch (error) {
        console.error('处理文件流时出错:', error);
        onError(error);
        return null;
    }

}
export function cleanupImageUrl(imageUrl) {
    if (imageUrl && imageUrl.startsWith('blob:')) {
        URL.revokeObjectURL(imageUrl);
    }
}