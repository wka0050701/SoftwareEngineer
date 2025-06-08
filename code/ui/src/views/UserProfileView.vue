<script setup>
import { ref, onMounted, reactive } from 'vue'
import { useRouter } from 'vue-router'
// --- 标注1：从模拟文件中导入新增的 API 函数 ---
import { mockGetUserProfileAPI, mockUpdateUserProfileAPI } from '@/mocks/api.js'

const router = useRouter()

// --- 状态变量 ---
const userProfile = ref(null)
const isLoading = ref(true)
const errorMessage = ref('')
const isEditMode = ref(false) 
const isUpdating = ref(false) 
const updateMessage = ref('') 

// 用于编辑表单的数据对象
const editForm = reactive({
  userId: null,
  phone: '',
  nickname: '',
  defaultAddress: ''
})

// --- 函数 ---

// 获取用户信息的函数
async function fetchUserProfile() {
  const userId = localStorage.getItem('userId')
  const jwt = localStorage.getItem('jwt')

  if (!jwt) {
    router.push('/login')
    return
  }

  isLoading.value = true
  errorMessage.value = ''
  try {
    // --- 标注2：切换为模拟 API 调用 ---

    // ** 模式一：使用模拟 API (当前生效) **
    const result = await mockGetUserProfileAPI(userId, jwt)

    /*
    // ** 模式二：使用真实 API (未来后端完成后，取消此段注释，并注释掉上面的模式一) **
    const response = await fetch(`/api/user/query?userId=${userId}`, {
      headers: { 'Authorization': `Bearer ${jwt}` }
    })
    if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`)
    const result = await response.json()
    */
    
    if (result.code === 1) {
      userProfile.value = result.data
    } else {
      throw new Error(result.message || '获取用户信息失败')
    }
  } catch (error) {
    console.error("获取用户信息时出错:", error)
    errorMessage.value = error.message
    if (String(error).includes('401')) {
      localStorage.clear()
      router.push('/login')
    }
  } finally {
    isLoading.value = false
  }
}

// onMounted 钩子，在组件加载后执行
onMounted(fetchUserProfile)

// 切换编辑模式的函数
function toggleEditMode(cancel = false) {
  isEditMode.value = !isEditMode.value
  updateMessage.value = '' // 清除消息
  if (isEditMode.value) {
    // 进入编辑模式时，用当前用户信息填充表单
    editForm.userId = localStorage.getItem('userId')
    editForm.phone = userProfile.value.phone
    editForm.nickname = userProfile.value.nickname
    editForm.defaultAddress = userProfile.value.defaultAddress
  }
}

// 处理信息更新的函数
async function handleUpdateProfile() {
  isUpdating.value = true
  updateMessage.value = ''
  const jwt = localStorage.getItem('jwt')
  try {
    // --- 标注3：同样，切换为模拟 API 调用 ---

    // ** 模式一：使用模拟 API (当前生效) **
    const result = await mockUpdateUserProfileAPI(editForm, jwt)

    /*
    // ** 模式二：使用真实 API **
    const response = await fetch('/api/user/modify', {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${jwt}`
      },
      body: JSON.stringify(editForm)
    })
    const result = await response.json()
    */

    if (result.code === 1) {
      updateMessage.value = { type: 'success', text: '信息更新成功！' }
      // 成功后退出编辑模式并重新获取最新信息
      isEditMode.value = false
      fetchUserProfile() 
    } else {
      throw new Error(result.message || '更新失败')
    }
  } catch (error) {
    console.error("更新用户信息时出错:", error)
    updateMessage.value = { type: 'error', text: `更新失败: ${error.message}` }
  } finally {
    isUpdating.value = false
  }
}

// 处理退出登录的函数
function handleLogout() {
  localStorage.clear()
  router.push('/login')
}
</script>

<template>
  <div class="page-container">
    <div class="profile-card">
      <div class="form-header">
        <h2>个人中心</h2>
        <p>在这里管理您的账户信息</p>
      </div>

      <div v-if="isLoading" class="loading-state">
        <div class="spinner"></div>
        <p>正在加载...</p>
      </div>

      <div v-else-if="errorMessage" class="message error-message">
        {{ errorMessage }}
      </div>

      <div v-else-if="userProfile">
        <!-- 展示模式 -->
        <div v-if="!isEditMode" class="profile-details">
          <div class="detail-item">
            <span class="label">用户昵称</span>
            <span class="value">{{ userProfile.nickname }}</span>
          </div>
          <div class="detail-item">
            <span class="label">手机号码</span>
            <span class="value">{{ userProfile.phone }}</span>
          </div>
          <div class="detail-item">
            <span class="label">默认地址</span>
            <span class="value">{{ userProfile.defaultAddress }}</span>
          </div>
           <div v-if="updateMessage" :class="`message ${updateMessage.type}-message`">
            {{ updateMessage.text }}
          </div>
          <button @click="toggleEditMode" class="action-btn edit-btn">编辑信息</button>
        </div>

        <!-- 编辑模式 -->
        <form v-else @submit.prevent="handleUpdateProfile" class="edit-form">
          <div class="form-group">
            <label for="nickname">用户昵称</label>
            <input type="text" id="nickname" v-model="editForm.nickname">
          </div>
           <div class="form-group">
            <label for="phone">手机号码</label>
            <input type="tel" id="phone" v-model="editForm.phone">
          </div>
           <div class="form-group">
            <label for="address">默认地址</label>
            <input type="text" id="address" v-model="editForm.defaultAddress">
          </div>
          <div v-if="updateMessage" :class="`message ${updateMessage.type}-message`">
            {{ updateMessage.text }}
          </div>
          <div class="form-actions">
            <button type="button" @click="toggleEditMode(true)" class="action-btn cancel-btn">取消</button>
            <button type="submit" :disabled="isUpdating" class="action-btn save-btn">
              {{ isUpdating ? '保存中...' : '保存更改' }}
            </button>
          </div>
        </form>

        <button @click="handleLogout" class="logout-btn">退出登录</button>
      </div>

    </div>
  </div>
</template>

<style scoped>
/* 基础和展示模式样式 (部分与之前相同) */
.page-container { display: flex; justify-content: center; align-items: flex-start; min-height: 90vh; background-color: #f0f2f5; padding: 2rem; }
.profile-card { width: 100%; max-width: 600px; background-color: #ffffff; padding: 2.5rem; border-radius: 12px; box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1); }
.form-header { text-align: center; margin-bottom: 2.5rem; }
.form-header h2 { font-size: 1.8rem; font-weight: 600; color: #1f2937; margin: 0; }
.form-header p { margin-top: 0.5rem; font-size: 1rem; color: #6b7280; }
.profile-details, .edit-form { display: flex; flex-direction: column; gap: 1.5rem; }
.detail-item { display: flex; justify-content: space-between; align-items: center; padding: 1rem; background-color: #f9fafb; border-radius: 8px; border: 1px solid #e5e7eb; }
.detail-item .label { font-weight: 500; color: #374151; }
.detail-item .value { font-size: 1rem; color: #1f2937; }
.logout-btn { margin-top: 1.5rem; background-color: #ef4444; color: white; border: none; border-radius: 8px; font-weight: bold; cursor: pointer; padding: 0.8rem; font-size: 1rem; transition: background-color 0.3s; }
.logout-btn:hover { background-color: #dc2626; }
.loading-state { text-align: center; padding: 3rem 0; color: #6b7280; }
.spinner { width: 40px; height: 40px; border: 4px solid #e5e7eb; border-top-color: hsla(160, 100%, 37%, 1); border-radius: 50%; animation: spin 1s linear infinite; margin: 0 auto 1rem; }
@keyframes spin { to { transform: rotate(360deg); } }
.message { padding: 1rem; border-radius: 6px; text-align: center; }
.error-message { color: #991b1b; background-color: #fee2e2; }
.success-message { color: #14532d; background-color: #dcfce7; }

/* 编辑模式新增样式 */
.form-group { display: flex; flex-direction: column; }
.form-group label { margin-bottom: 0.5rem; font-weight: 500; color: #374151; font-size: 0.9rem; }
.form-group input { padding: 0.8rem 1rem; border: 1px solid #d1d5db; background-color: #f9fafb; border-radius: 8px; font-size: 1rem; transition: border-color 0.2s, box-shadow 0.2s; }
.form-group input:focus { outline: none; border-color: hsla(160, 100%, 37%, 1); box-shadow: 0 0 0 3px hsla(160, 100%, 37%, 0.2); }
.form-actions { display: flex; justify-content: flex-end; gap: 1rem; }
.action-btn { padding: 0.8rem 1.5rem; color: white; border: none; border-radius: 8px; font-size: 1rem; font-weight: bold; cursor: pointer; transition: background-color 0.3s; }
.edit-btn { background-color: #3b82f6; }
.edit-btn:hover { background-color: #2563eb; }
.save-btn { background-color: hsla(160, 100%, 37%, 1); }
.save-btn:hover { background-color: hsla(160, 100%, 30%, 1); }
.cancel-btn { background-color: #6b7280; }
.cancel-btn:hover { background-color: #4b5563; }
</style>
