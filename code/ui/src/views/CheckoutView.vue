<script setup>
import { ref } from 'vue';
import { useCartStore } from '@/stores/cart.js';
import { useRouter } from 'vue-router';
// 稍后我们会在这里导入模拟 API
// import { mockCreateOrderAPI, mockAddItemsToOrderAPI } from '@/mocks/api.js';

const cartStore = useCartStore();
const router = useRouter();

const isLoading = ref(false);
const errorMessage = ref('');

// 如果购物车是空的，直接跳转回商品页
if (cartStore.items.length === 0) {
  router.replace('/products');
}

async function handlePlaceOrder() {
  isLoading.value = true;
  errorMessage.value = '';

  try {
    // ---- 这里是未来调用真实 API 的地方 ----
    // 1. 创建订单
    // const createOrderResult = await mockCreateOrderAPI();
    // if (createOrderResult.code !== 1) throw new Error('创建订单失败');
    // const orderId = createOrderResult.data.orderId;

    // 2. 将商品逐一添加到订单
    // for (const item of cartStore.items) {
    //   await mockAddItemsToOrderAPI(orderId, { productId: item.id, quantity: item.quantity });
    // }

    // ---- 为方便演示，我们先用一个假的成功流程 ----
    await new Promise(resolve => setTimeout(resolve, 1500)); // 模拟网络延迟
    
    // 3. 成功后清空购物车并跳转
    cartStore.clearCart();
    alert('下单成功！');
    router.push('/profile'); // 假设跳转到个人中心查看订单

  } catch (error) {
    errorMessage.value = error.message || '下单过程中发生未知错误。';
  } finally {
    isLoading.value = false;
  }
}
</script>

<template>
  <div class="page-container">
    <div class="checkout-card">
      <h1 class="checkout-title">确认订单</h1>
      
      <div class="order-summary">
        <h2 class="summary-title">订单商品</h2>
        <div class="summary-items">
          <div v-for="item in cartStore.items" :key="item.id" class="summary-item">
            <span class="item-name">{{ item.name }}</span>
            <span class="item-quantity">x {{ item.quantity }}</span>
            <span class="item-price">¥{{ (item.price * item.quantity).toFixed(2) }}</span>
          </div>
        </div>
        <div class="summary-total">
          <span>总计</span>
          <span class="total-price">¥{{ cartStore.totalPrice }}</span>
        </div>
      </div>

      <div class="delivery-info">
        <h2 class="summary-title">配送信息</h2>
        <p><strong>收货人:</strong> 可爱的普通用户</p>
        <p><strong>联系电话:</strong> 13800000102</p>
        <p><strong>配送地址:</strong> 地球村某个角落 (稍后从用户信息获取)</p>
      </div>

      <div v-if="errorMessage" class="message error-message">
        {{ errorMessage }}
      </div>
      
      <button @click="handlePlaceOrder" :disabled="isLoading" class="place-order-btn">
        {{ isLoading ? '正在提交订单...' : '确认下单' }}
      </button>

    </div>
  </div>
</template>

<style scoped>
.page-container {
  max-width: 800px;
  margin: 2rem auto;
  padding: 2rem;
  background-color: #f7f8fa;
}

.checkout-card {
  background-color: #fff;
  padding: 2rem 2.5rem;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.checkout-title {
  text-align: center;
  font-size: 2rem;
  font-weight: 700;
  margin-bottom: 2rem;
}

.summary-title {
  font-size: 1.2rem;
  font-weight: 600;
  padding-bottom: 0.8rem;
  margin-bottom: 1rem;
  border-bottom: 1px solid #eee;
}

.order-summary {
  margin-bottom: 2rem;
}

.summary-item {
  display: flex;
  justify-content: space-between;
  padding: 0.5rem 0;
}

.item-name {
  color: #333;
}
.item-quantity {
  color: #888;
}
.item-price {
  font-weight: 500;
}

.summary-total {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin-top: 1rem;
  padding-top: 1rem;
  border-top: 1px solid #eee;
  font-size: 1.1rem;
  font-weight: bold;
}

.total-price {
  font-size: 1.5rem;
  color: #ef4444;
  margin-left: 1rem;
}

.delivery-info {
  margin-bottom: 2.5rem;
}

.delivery-info p {
  line-height: 1.8;
  color: #555;
}

.place-order-btn {
  width: 100%;
  padding: 1rem;
  border: none;
  background-color: hsla(160, 100%, 37%, 1);
  color: white;
  border-radius: 8px;
  font-size: 1.2rem;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s;
}

.place-order-btn:hover {
  background-color: hsla(160, 100%, 30%, 1);
}

.place-order-btn:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.message {
  margin-bottom: 1.5rem;
  text-align: center;
  padding: 1rem;
  border-radius: 6px;
}
.error-message {
  color: #991b1b;
  background-color: #fee2e2;
}
</style>
