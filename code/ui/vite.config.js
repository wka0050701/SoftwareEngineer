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
      // 这条规则会把所有以 /api 开头的请求，转发到你的后端服务器
      '/api': {
        // 关键！这里要换成你后端服务器的真实地址和端口
        target: 'http://localhost:8080', 
        // 允许跨域
        changeOrigin: true
      }
    }
  }
})
