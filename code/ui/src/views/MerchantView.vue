<script setup>
import { ref, onMounted, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { mockGetMerchantInfoAPI, mockUpdateMerchantInfoAPI } from '@/mocks/api.js'

const router = useRouter()

// --- 状态变量 ---
const merchantInfo = ref(null)
const isLoading = ref(true)
const errorMessage = ref('')
const isEditMode = ref(false)
const isUpdating = ref(false)
const updateMessage = ref('')

// 用于编辑表单的数据对象
const editForm = reactive({
  userId: null,
  name: '',
  description: '',
  phone: '',
  address: '',
  businessHours: '',
  minOrder: 0,
  deliveryFee: 0,
  status: 1,
})

// --- 函数 ---
async function fetchMerchantInfo() {
  const userId = localStorage.getItem('userId')
  const jwt = localStorage.getItem('jwt')
  if (!jwt || userId !== '1') { // 增加对商家ID的判断
    router.push('/login')
    return
  }
  isLoading.value = true
  errorMessage.value = ''
  try {
    const result = await mockGetMerchantInfoAPI(userId, jwt)
    if (result.code === 1) {
      merchantInfo.value = result.data
    } else {
      throw new Error(result.message || '获取商家信息失败')
    }
  } catch (error) {
    errorMessage.value = error.message
  } finally {
    isLoading.value = false
  }
}

onMounted(fetchMerchantInfo)

function toggleEditMode() {
  isEditMode.value = !isEditMode.value
  updateMessage.value = ''
  if (isEditMode.value) {
    const info = merchantInfo.value
    editForm.userId = localStorage.getItem('userId')
    editForm.name = info.name
    editForm.description = info.description
    editForm.phone = info.phone
    editForm.address = info.address
    editForm.businessHours = info.businessHours
    editForm.minOrder = info.minOrder
    editForm.deliveryFee = info.deliveryFee
    editForm.status = info.status
  }
}

async function handleUpdateMerchantInfo() {
  isUpdating.value = true
  updateMessage.value = ''
  const jwt = localStorage.getItem('jwt')
  try {
    const result = await mockUpdateMerchantInfoAPI(editForm, jwt)
    if (result.code === 1) {
      updateMessage.value = { type: 'success', text: '店铺信息更新成功！' }
      isEditMode.value = false
      fetchMerchantInfo()
    } else {
      throw new Error(result.message || '更新失败')
    }
  } catch (error) {
    updateMessage.value = { type: 'error', text: `更新失败: ${error.message}` }
  } finally {
    isUpdating.value = false
  }
}

function handleLogout() {
  localStorage.clear()
  router.push('/login')
}
</script>

<template>
  <div class="page-container">
    <div class="merchant-card">
      <div class="form-header">
        <h2>商家管理后台</h2>
        <p>管理您的店铺信息和运营状态</p>
      </div>

      <div v-if="isLoading" class="loading-state">
        <div class="spinner"></div>
      </div>
      <div v-else-if="errorMessage" class="message error-message">{{ errorMessage }}</div>
      
      <div v-else-if="merchantInfo">
        <!-- 展示模式 -->
        <div v-if="!isEditMode" class="details-view">
          <div class="detail-item">
            <span class="label">店铺名称</span>
            <span class="value">{{ merchantInfo.name }}</span>
          </div>
          <div class="detail-item full-width">
            <span class="label">店铺描述</span>
            <p class="value description">{{ merchantInfo.description }}</p>
          </div>
          <div class="detail-item">
            <span class="label">联系电话</span>
            <span class="value">{{ merchantInfo.phone }}</span>
          </div>
          <!-- Here is the change -->
          <div class="detail-item full-width">
            <span class="label">店铺地址</span>
            <p class="value description">{{ merchantInfo.address }}</p>
          </div>
          <div class="detail-item">
            <span class="label">营业时间</span>
            <span class="value">{{ merchantInfo.businessHours }}</span>
          </div>
          <div class="detail-item">
            <span class="label">最低起送价</span>
            <span class="value">¥{{ merchantInfo.minOrder }}</span>
          </div>
          <div class="detail-item">
            <span class="label">配送费</span>
            <span class="value">¥{{ merchantInfo.deliveryFee }}</span>
          </div>
          <div class="detail-item">
            <span class="label">营业状态</span>
            <span :class="['status-badge', merchantInfo.status === 1 ? 'status-open' : 'status-closed']">
              {{ merchantInfo.status === 1 ? '营业中' : '休息中' }}
            </span>
          </div>
          <div v-if="updateMessage" :class="`message ${updateMessage.type}-message`">{{ updateMessage.text }}</div>
          <button @click="toggleEditMode" class="action-btn edit-btn">编辑店铺信息</button>
        </div>

        <!-- 编辑模式 -->
        <form v-else @submit.prevent="handleUpdateMerchantInfo" class="edit-form">
          <div class="form-group"><label for="name">店铺名称</label><input type="text" id="name" v-model="editForm.name"></div>
          <div class="form-group"><label for="description">店铺描述</label><textarea id="description" v-model="editForm.description" rows="3"></textarea></div>
          <div class="form-group"><label for="phone">联系电话</label><input type="tel" id="phone" v-model="editForm.phone"></div>
          <div class="form-group"><label for="address">店铺地址</label><input type="text" id="address" v-model="editForm.address"></div>
          <div class="form-group"><label for="businessHours">营业时间</label><input type="text" id="businessHours" v-model="editForm.businessHours"></div>
          <div class="form-group"><label for="minOrder">最低起送价</label><input type="number" id="minOrder" v-model.number="editForm.minOrder"></div>
          <div class="form-group"><label for="deliveryFee">配送费</label><input type="number" id="deliveryFee" v-model.number="editForm.deliveryFee"></div>
          <div class="form-group">
            <label>营业状态</label>
            <select v-model.number="editForm.status" class="status-select">
              <option :value="1">营业中</option>
              <option :value="0">休息中</option> <!-- 假设 0 代表休息中 -->
            </select>
          </div>
          <div v-if="updateMessage" :class="`message ${updateMessage.type}-message`">{{ updateMessage.text }}</div>
          <div class="form-actions">
            <button type="button" @click="toggleEditMode" class="action-btn cancel-btn">取消</button>
            <button type="submit" :disabled="isUpdating" class="action-btn save-btn">{{ isUpdating ? '保存中...' : '保存更改' }}</button>
          </div>
        </form>

        <button @click="handleLogout" class="logout-btn">退出登录</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.page-container { display: flex; justify-content: center; align-items: flex-start; min-height: 90vh; background-color: #f0f2f5; padding: 2rem; }
.merchant-card { width: 100%; max-width: 700px; background-color: #ffffff; padding: 2.5rem; border-radius: 12px; box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1); }
.form-header { text-align: center; margin-bottom: 2.5rem; }
.form-header h2 { font-size: 1.8rem; font-weight: 600; color: #1f2937; margin: 0; }
.form-header p { margin-top: 0.5rem; font-size: 1rem; color: #6b7280; }
.details-view, .edit-form { display: flex; flex-direction: column; gap: 1.2rem; }
.detail-item { display: flex; justify-content: space-between; align-items: flex-start; padding: 1rem; background-color: #f9fafb; border-radius: 8px; border: 1px solid #e5e7eb; flex-wrap: wrap; }
.detail-item.full-width { flex-direction: column; align-items: flex-start; }
.detail-item .label { font-weight: 500; color: #4b5563; margin-bottom: 0.25rem; }
.detail-item .value { font-size: 1rem; color: #1f2937; text-align: right; }
.detail-item .value.description { text-align: left; margin-top: 0.25rem; color: #374151; line-height: 1.6; }
.status-badge { padding: 0.25rem 0.75rem; border-radius: 50px; font-weight: 500; font-size: 0.85rem; }
.status-open { background-color: #dcfce7; color: #166534; }
.status-closed { background-color: #fee2e2; color: #991b1b; }
.form-group { display: flex; flex-direction: column; }
.form-group label { margin-bottom: 0.5rem; font-weight: 500; color: #374151; font-size: 0.9rem; }
.form-group input, .form-group textarea, .form-group select { padding: 0.8rem 1rem; border: 1px solid #d1d5db; background-color: #f9fafb; border-radius: 8px; font-size: 1rem; font-family: inherit; }
.form-group input:focus, .form-group textarea:focus, .form-group select:focus { outline: none; border-color: hsla(160, 100%, 37%, 1); box-shadow: 0 0 0 3px hsla(160, 100%, 37%, 0.2); }
textarea { resize: vertical; }
.form-actions { display: flex; justify-content: flex-end; gap: 1rem; margin-top: 1rem; }
.action-btn, .logout-btn { padding: 0.8rem 1.5rem; color: white; border: none; border-radius: 8px; font-size: 1rem; font-weight: bold; cursor: pointer; transition: background-color 0.3s; }
.edit-btn { margin-top: 1rem; background-color: #3b82f6; }
.save-btn { background-color: hsla(160, 100%, 37%, 1); }
.cancel-btn { background-color: #6b7280; }
.logout-btn { margin-top: 1.5rem; background-color: #ef4444; }
.loading-state { text-align: center; padding: 3rem 0; color: #6b7280; }
.spinner { width: 40px; height: 40px; border: 4px solid #e5e7eb; border-top-color: hsla(160, 100%, 37%, 1); border-radius: 50%; animation: spin 1s linear infinite; margin: 0 auto 1rem; }
@keyframes spin { to { transform: rotate(360deg); } }
.message { padding: 1rem; border-radius: 6px; text-align: center; }
.error-message { color: #991b1b; background-color: #fee2e2; }
.success-message { color: #14532d; background-color: #dcfce7; }
</style>
