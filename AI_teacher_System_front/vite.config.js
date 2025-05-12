import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',  // 后端API服务器地址
        changeOrigin: true,
        // 移除rewrite，保留/api前缀，因为后端接口路径中已包含/api
        // rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  }
})
