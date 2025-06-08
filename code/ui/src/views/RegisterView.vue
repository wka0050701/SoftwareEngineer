<script setup>
// 从 vue 引入 reactive 和 ref，用于创建响应式数据
import { reactive, ref } from 'vue'
// 从 vue-router 引入 useRouter，用于在代码中跳转路由
import { useRouter } from 'vue-router'

// 获取 router 实例，以便在注册成功后跳转页面
const router = useRouter()

// 使用 reactive 创建一个对象，来存放表单的所有数据，方便管理
const form = reactive({
  phone: '',
  password: '',
  nickname: '',
  defaultAddress: ''
})

// 使用 ref 创建独立的响应式变量，用于控制 UI 状态
const isLoading = ref(false)      // 控制按钮是否为加载中状态
const errorMessage = ref('')    // 存放错误提示信息
const successMessage = ref('')  // 存放成功提示信息

// 定义一个异步函数，在表单提交时调用
async function handleRegister() {
  // 每次提交前，先清空上一次的提示信息
  errorMessage.value = ''
  successMessage.value = ''

  // 1. 前端基础校验
  if (!form.phone || !form.password || !form.nickname || !form.defaultAddress) {
    errorMessage.value = '所有字段均为必填项。'
    return // 阻止后续代码执行
  }
  if (!/^\d{11}$/.test(form.phone)) {
    errorMessage.value = '请输入有效的11位手机号。'
    return
  }

  // 开始调用 API，将按钮设置为加载中状态
  isLoading.value = true

  // 2. 调用 API
  try {
    // 使用浏览器自带的 fetch 函数发送 POST 请求
    const response = await fetch('/api/auth/register', {
      method: 'POST',
      headers: {
        // 告诉后端我们发送的是 JSON 格式的数据
        'Content-Type': 'application/json'
      },
      // 将表单数据对象转换为 JSON 字符串后作为请求体发送
      body: JSON.stringify(form)
    })

    // 将后端返回的响应体解析为 JSON 对象
    const result = await response.json()

    // 3. 处理后端返回的结果
    if (result.code === 1) {
      // 业务成功
      successMessage.value = `注册成功！您的用户ID是 ${result.data.userId}。正在跳转到登录页...`
      // 延迟2秒后跳转到登录页
      setTimeout(() => {
        router.push('/login') // 假设你有一个 /login 的路由
      }, 2000)
    } else {
      // 业务失败，显示后端返回的错误信息
      errorMessage.value = result.message || '注册失败，请稍后再试。'
    }
  } catch (error) {
    // 捕获网络错误或其他意外错误
    console.error('注册过程中发生错误:', error)
    errorMessage.value = '网络请求失败，请检查您的网络连接。'
  } finally {
    // 无论成功还是失败，最后都要结束加载状态
    isLoading.value = false
  }
}
</script>

<template>
  <div class="page-container">
    <div class="register-card">
      
      <div class="form-header">
        <h2>创建您的账户</h2>
      </div>

      <form @submit.prevent="handleRegister" class="register-form">
        
        <div class="form-group">
          <label for="phone">手机号</label>
          <input type="tel" id="phone" v-model="form.phone" placeholder="请输入11位手机号">
        </div>

        <div class="form-group">
          <label for="password">密码</label>
          <input type="password" id="password" v-model="form.password" placeholder="请输入密码">
        </div>

        <div class="form-group">
          <label for="nickname">昵称</label>
          <input type="text" id="nickname" v-model="form.nickname" placeholder="请输入您的昵称">
        </div>

        <div class="form-group">
          <label for="defaultAddress">默认地址</label>
          <input type="text" id="defaultAddress" v-model="form.defaultAddress" placeholder="请输入您的收货地址">
        </div>

        <div v-if="errorMessage" class="message error-message">
          {{ errorMessage }}
        </div>
        <div v-if="successMessage" class="message success-message">
          {{ successMessage }}
        </div>
        
        <button type="submit" :disabled="isLoading">
          {{ isLoading ? '注册中...' : '立即创建' }}
        </button>
      </form>

      <div class="form-footer">
        已有账户？<RouterLink to="/login" class="login-link">立即登录</RouterLink>
      </div>

    </div>
  </div>
</template>

<style scoped>
/* 1. 页面“舞台”容器 */
.page-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 90vh; /* 至少和视口一样高 */
  background-color: #f0f2f5; /* 更柔和的背景色 */
  padding: 2rem;
}

/* 2. 注册表单“卡片” */
.register-card {
  width: 100%;
  max-width: 420px;
  background-color: #ffffff;
  padding: 2.5rem;
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

/* 3. 表单头部 */
.form-header {
  text-align: center;
  margin-bottom: 2rem;
}

.form-header h2 {
  font-size: 1.8rem;
  font-weight: 600;
  color: #1f2937;
  margin: 0;
}

.form-header p {
  margin-top: 0.5rem;
  font-size: 1rem;
  color: #6b7280;
}

/* 4. 表单主体 */
.register-form {
  display: flex;
  flex-direction: column;
  gap: 1.25rem; /* 优化表单项间距 */
}

.form-group {
  display: flex;
  flex-direction: column;
}

.form-group label {
  margin-bottom: 0.5rem;
  font-weight: 500; /* 字体稍细一点 */
  color: #374151;
  font-size: 0.9rem;
}

.form-group input {
  padding: 0.8rem 1rem;
  border: 1px solid #d1d5db;
  background-color: #f9fafb;
  border-radius: 8px; /* 更大的圆角 */
  font-size: 1rem;
  transition: border-color 0.2s, box-shadow 0.2s;
}

.form-group input:focus {
  outline: none;
  border-color: hsla(160, 100%, 37%, 1);
  box-shadow: 0 0 0 3px hsla(160, 100%, 37%, 0.2);
}

button {
  padding: 0.8rem;
  background-color: hsla(160, 100%, 37%, 1);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s, transform 0.1s;
  margin-top: 0.5rem; /* 与提示信息拉开距离 */
}

button:hover {
  background-color: hsla(160, 100%, 30%, 1);
}

button:active {
  transform: scale(0.98);
}

button:disabled {
  background-color: #9ca3af;
  cursor: not-allowed;
}

/* 消息提示框 */
.message {
  padding: 0.8rem 1rem;
  border-radius: 6px;
  text-align: center;
  font-size: 0.9rem;
}

.error-message {
  color: #991b1b;
  background-color: #fee2e2;
}

.success-message {
  color: #14532d;
  background-color: #dcfce7;
}

/* 表单底部 */
.form-footer {
  text-align: center;
  margin-top: 1.5rem;
  font-size: 0.9rem;
  color: #4b5563;
}

.login-link {
  color: hsla(160, 100%, 37%, 1);
  font-weight: 500;
  text-decoration: none;
}

.login-link:hover {
  text-decoration: underline;
}
</style>
