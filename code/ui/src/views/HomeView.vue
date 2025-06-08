<script setup>
// 为了使用编程式导航，我们从 vue-router 导入 useRouter
// 不过这里我们用 <RouterLink>，所以暂时不需要脚本逻辑
import { RouterLink } from 'vue-router'
</script>

<template>
  <div class="page-container">
    <div class="welcome-card">
      
      <div class="welcome-icon"><img src="../assets/welcome.jpg" alt="平台Logo" class="welcome-icon-img" /></div>
      
      <h1 class="welcome-title">欢迎来到 hhh 外卖平台</h1>
      
      <p class="welcome-subtitle">
        HH外卖，喜笑颜开
      </p>
      
      <div class="actions-container">
        <RouterLink to="/login" class="action-btn login-btn">登录</RouterLink>
        <RouterLink to="/register" class="action-btn register-btn">注册</RouterLink>
      </div>

    </div>
  </div>
</template>

<style scoped>
/* 页面“舞台”容器 */
.page-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 90vh;
  background-color: #f0f2f5;
  padding: 2rem;
}

/* 欢迎内容“卡片” */
.welcome-card {
  width: 100%;
  max-width: 550px;
  background-color: #ffffff;
  padding: 4rem 3rem;
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.welcome-icon {
  font-size: 4rem;
  margin-bottom: 1.5rem;
  line-height: 1;
}

.welcome-title {
  font-size: 2.2rem;
  font-weight: 700;
  color: #1f2937;
  margin: 0;
}

.welcome-icon-img {
  width: 64px;
  height: 64px;
  margin-bottom: 1.5rem;
  border-radius: 50%; 
  object-fit: cover; 
}

.welcome-subtitle {
  font-size: 1.1rem;
  color: #6b7280;
  margin-top: 0.8rem;
  max-width: 400px;
  line-height: 1.6;
}

/* 操作按钮容器 */
.actions-container {
  display: flex;
  gap: 1rem;
  margin-top: 2.5rem;
}

/* 按钮统一样式 */
.action-btn {
  padding: 0.8rem 2rem;
  border: none;
  border-radius: 50px; /* 胶囊形状 */
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  text-decoration: none;
  display: inline-block;
  transition: all 0.3s ease;
}

/* 登录按钮样式 */
.login-btn {
  background-color: hsla(160, 100%, 37%, 1);
  color: white;
}

.login-btn:hover {
  background-color: hsla(160, 100%, 30%, 1);
  transform: translateY(-2px);
  box-shadow: 0 4px 15px hsla(160, 100%, 37%, 0.3);
}

/* 注册按钮样式 */
.register-btn {
  background-color: #e5e7eb;
  color: #374151;
}

.register-btn:hover {
  background-color: #d1d5db;
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}
</style>
