<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
// --- 标注1：从您创建的模拟文件中导入模拟 API 函数 ---
// 请确保您已经在 src/mocks/ 目录下创建了 api.js 文件
import { mockLoginAPI, mockChangePasswordAPI } from '@/mocks/api.js'

const router = useRouter()

// --- 原有登录逻辑 ---
const form = reactive({
  phone: '',
  password: ''
})
const isLoading = ref(false)
const errorMessage = ref('')

async function handleLogin() {
  errorMessage.value = ''
  if (!form.phone || !form.password) {
    errorMessage.value = '手机号和密码不能为空。'
    return
  }
  isLoading.value = true
  try {
    // --- 标注2：在这里进行 API 调用的切换 ---

    // ** 模式一：使用模拟 API (当前生效) **
    const result = await mockLoginAPI(form)

    /*
    // ** 模式二：使用真实 API (未来后端完成后，取消此段注释，并注释掉上面的模式一) **
    const response = await fetch('/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(form)
    })
    const result = await response.json()
    */

    if (result.code === 1) {
      // 登录成功，保存通用信息
      localStorage.setItem('userId', result.data.id)
      localStorage.setItem('jwt', result.data.jwt)

      // --- 核心修改在这里：根据 userId 进行角色判断和跳转 ---
      if (result.data.id === 1) {
        // 如果 userId 是 1，跳转到商家界面
        router.push('/merchant') // 假设商家界面路由为 /merchant
      } else {
        // 否则，跳转到普通用户个人中心
        router.push('/profile')
      }

    } else {
      errorMessage.value = result.message || '登录失败，请检查您的账号和密码。'
    }
  } catch (error) {
    console.error('登录过程中发生错误:', error)
    errorMessage.value = '网络请求失败，请稍后重试。'
  } finally {
    isLoading.value = false
  }
}

// --- 新增的修改密码模态框逻辑 ---
const showPasswordModal = ref(false)
const isUpdatingPassword = ref(false)
const passwordForm = reactive({
  phone: '',
  Password: '' // 注意：根据您的API文档，密码字段首字母大写
})
const passwordChangeError = ref('')
const passwordChangeSuccess = ref('')

// 打开模态框并重置状态
function openPasswordModal() {
  passwordChangeError.value = ''
  passwordChangeSuccess.value = ''
  passwordForm.phone = ''
  passwordForm.Password = ''
  showPasswordModal.value = true
}

// 处理密码修改的提交
async function handlePasswordChange() {
  passwordChangeError.value = ''
  passwordChangeSuccess.value = ''
  if (!passwordForm.phone || !passwordForm.Password) {
    passwordChangeError.value = '手机号和新密码不能为空。'
    return
  }
  
  isUpdatingPassword.value = true
  try {
    // --- 标注3：同样，对修改密码接口也进行切换 ---
    
    // ** 模式一：使用模拟 API (当前生效) **
    const result = await mockChangePasswordAPI(passwordForm)
    
    /*
    // ** 模式二：使用真实 API **
    const response = await fetch('/api/auth/password', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(passwordForm)
    })
    const result = await response.json()
    */

    if (result.code === 1) {
      passwordChangeSuccess.value = '密码修改成功！请使用新密码登录。'
      // 2秒后自动关闭弹窗
      setTimeout(() => {
        showPasswordModal.value = false
      }, 2000)
    } else {
      passwordChangeError.value = result.message || '修改失败，请重试。'
    }
  } catch (error) {
    console.error('修改密码过程中发生错误:', error)
    passwordChangeError.value = '网络请求失败，请稍后重试。'
  } finally {
    isUpdatingPassword.value = false
  }
}
</script>

<template>
  <div class="page-container">
    <div class="login-card">
      <div class="form-header">
        <h2>欢迎回来</h2>
        <p>登录您的 hhh 外卖平台账户</p>
      </div>
      <form @submit.prevent="handleLogin" class="login-form">
        <div class="form-group">
          <label for="phone">手机号</label>
          <input type="tel" id="phone" v-model="form.phone" placeholder="请输入您的手机号">
        </div>
        <div class="form-group">
          <label for="password">密码</label>
          <input type="password" id="password" v-model="form.password" placeholder="请输入您的密码">
        </div>
        <div v-if="errorMessage" class="message error-message">
          {{ errorMessage }}
        </div>
        <button type="submit" :disabled="isLoading">
          {{ isLoading ? '登录中...' : '立即登录' }}
        </button>
      </form>
      <div class="form-footer">
        <span>还没有账户？<RouterLink to="/register" class="link">立即注册</RouterLink></span>
        <a href="#" @click.prevent="openPasswordModal" class="link">忘记密码？</a>
      </div>
    </div>

    <div v-if="showPasswordModal" class="modal-overlay">
      <div class="modal-content">
        <h3>修改密码</h3>
        <p class="modal-subtitle">请输入您的手机号和新密码。</p>
        <form @submit.prevent="handlePasswordChange" class="modal-form">
          <div class="form-group">
            <label for="reset-phone">手机号</label>
            <input type="tel" id="reset-phone" v-model="passwordForm.phone" placeholder="用于验证的手机号">
          </div>
          <div class="form-group">
            <label for="reset-password">新密码</label>
            <input type="password" id="reset-password" v-model="passwordForm.Password" placeholder="请输入您的新密码">
          </div>
          <div v-if="passwordChangeError" class="message error-message">{{ passwordChangeError }}</div>
          <div v-if="passwordChangeSuccess" class="message success-message">{{ passwordChangeSuccess }}</div>
          <div class="modal-actions">
            <button type="button" class="btn-secondary" @click="showPasswordModal = false">取消</button>
            <button type="submit" class="btn-primary" :disabled="isUpdatingPassword">
              {{ isUpdatingPassword ? '提交中...' : '确认修改' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* --- 基础样式 (与之前相同) --- */
.page-container { display: flex; justify-content: center; align-items: center; min-height: 90vh; background-color: #f0f2f5; padding: 2rem; }
.login-card { width: 100%; max-width: 420px; background-color: #ffffff; padding: 2.5rem; border-radius: 12px; box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1); }
.form-header { text-align: center; margin-bottom: 2rem; }
.form-header h2 { font-size: 1.8rem; font-weight: 600; color: #1f2937; margin: 0; }
.form-header p { margin-top: 0.5rem; font-size: 1rem; color: #6b7280; }
.login-form, .modal-form { display: flex; flex-direction: column; gap: 1.25rem; }
.form-group { display: flex; flex-direction: column; }
.form-group label { margin-bottom: 0.5rem; font-weight: 500; color: #374151; font-size: 0.9rem; }
.form-group input { padding: 0.8rem 1rem; border: 1px solid #d1d5db; background-color: #f9fafb; border-radius: 8px; font-size: 1rem; transition: border-color 0.2s, box-shadow 0.2s; }
.form-group input:focus { outline: none; border-color: hsla(160, 100%, 37%, 1); box-shadow: 0 0 0 3px hsla(160, 100%, 37%, 0.2); }
button { padding: 0.8rem; color: white; border: none; border-radius: 8px; font-size: 1rem; font-weight: bold; cursor: pointer; transition: background-color 0.3s, transform 0.1s; margin-top: 0.5rem; }
button[type="submit"] { background-color: hsla(160, 100%, 37%, 1); }
button[type="submit"]:hover { background-color: hsla(160, 100%, 30%, 1); }
button:disabled { background-color: #9ca3af; cursor: not-allowed; }

/* 这里是修改的部分 */
.form-footer {
  display: flex;
  flex-direction: column; /* 将主轴方向改为垂直 */
  align-items: center;    /* 在交叉轴上居中（水平居中） */
  gap: 0.5rem;            /* 在项目之间添加一些间距 */
  margin-top: 1.5rem;
  font-size: 0.9rem;
  color: #4b5563;
}
.link { color: hsla(160, 100%, 37%, 1); font-weight: 500; text-decoration: none; }
.link:hover { text-decoration: underline; }
.message { padding: 0.8rem 1rem; border-radius: 6px; text-align: center; font-size: 0.9rem; }
.error-message { color: #991b1b; background-color: #fee2e2; }
.success-message { color: #14532d; background-color: #dcfce7; }

/* --- 新增的模态框样式 --- */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}
.modal-content {
  background: white;
  padding: 2rem;
  border-radius: 12px;
  width: 100%;
  max-width: 400px;
  box-shadow: 0 5px 15px rgba(0,0,0,0.3);
}
.modal-content h3 {
  margin-top: 0;
  font-size: 1.5rem;
  text-align: center;
}
.modal-subtitle {
  text-align: center;
  color: #6b7280;
  margin-top: -1rem;
  margin-bottom: 1.5rem;
}
.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 1.5rem;
}
.modal-actions button {
  margin-top: 0;
}
.btn-primary { background-color: hsla(160, 100%, 37%, 1); }
.btn-primary:hover { background-color: hsla(160, 100%, 30%, 1); }
.btn-secondary { background-color: #e5e7eb; color: #374151; }
.btn-secondary:hover { background-color: #d1d5db; }
</style>
