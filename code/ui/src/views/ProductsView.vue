<script setup>
import { ref, onMounted } from 'vue';
import { mockGetProductsAPI } from '@/mocks/api.js';
// --- 核心修改1：导入我们创建的 useCartStore ---
import { useCartStore } from '@/stores/cart.js';

const products = ref([]);
const isLoading = ref(true);
const errorMessage = ref('');

// --- 核心修改2：获取 cart store 的实例 ---
const cartStore = useCartStore();

// --- 核心修改3：修改函数，调用 store 里的方法 ---
// 现在这个函数不再是模拟的了，它会真正地操作全局购物车
function addToCart(product) {
  cartStore.addToCart(product);
  // 我们可以保留 alert 作为给用户的即时反馈
  alert(`已将“${product.name}”加入购物车！`);
}

onMounted(async () => {
  try {
    const result = await mockGetProductsAPI();
    if (result.code === 1) {
      products.value = result.data;
    } else {
      throw new Error(result.message);
    }
  } catch (error) {
    errorMessage.value = error.message || '加载商品失败，请稍后重试。';
  } finally {
    isLoading.value = false;
  }
});
</script>

<template>
  <div class="page-container">
    <div class="products-header">
      <h1>绝命炸鸡</h1>
      <p>用科学的态度对待炸鸡 —— 从腌制到火候，每一步都是‘精确计算’的艺术</p>
    </div>

    <div v-if="isLoading" class="loading-state">
      <div class="spinner"></div>
      <p>正在努力加载菜单...</p>
    </div>

    <div v-else-if="errorMessage" class="message error-message">
      {{ errorMessage }}
    </div>

    <div v-else class="products-grid">
      <div v-for="product in products" :key="product.id" class="product-card">
        <img :src="product.imageUrl" :alt="product.name" class="product-image">
        <div class="product-info">
          <h3 class="product-name">{{ product.name }}</h3>
          <p class="product-description">{{ product.description }}</p>
          <div class="product-footer">
            <span class="product-price">¥{{ product.price }}</span>
            <button @click="addToCart(product)" class="add-to-cart-btn">加入购物车</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.page-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
  background-color: #f7f8fa;
}

.products-header {
  text-align: center;
  margin-bottom: 2.5rem;
}

.products-header h1 {
  font-size: 2.5rem;
  font-weight: 700;
}

.products-header p {
  font-size: 1.1rem;
  color: #888;
  margin-top: 0.5rem;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 2rem;
}

.product-card {
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  display: flex;
  flex-direction: column;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.12);
}

.product-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.product-info {
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  flex-grow: 1; /* 让这个区域填满剩余空间 */
}

.product-name {
  font-size: 1.25rem;
  font-weight: 600;
  margin: 0 0 0.5rem 0;
}

.product-description {
  font-size: 0.9rem;
  color: #6b7280;
  line-height: 1.5;
  flex-grow: 1; /* 占据多余空间，将页脚推到底部 */
  margin-bottom: 1rem;
}

.product-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: auto; /* 将其推到卡片底部 */
}

.product-price {
  font-size: 1.4rem;
  font-weight: bold;
  color: #ef4444;
}

.add-to-cart-btn {
  border: none;
  background-color: hsla(160, 100%, 37%, 1);
  color: white;
  padding: 0.6rem 1.2rem;
  border-radius: 8px;
  font-size: 0.9rem;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s;
}

.add-to-cart-btn:hover {
  background-color: hsla(160, 100%, 30%, 1);
}

.loading-state, .message { /* 样式与之前组件相同 */
  text-align: center; padding: 3rem 0; color: #6b7280;
}
.spinner { width: 40px; height: 40px; border: 4px solid #e5e7eb; border-top-color: hsla(160, 100%, 37%, 1); border-radius: 50%; animation: spin 1s linear infinite; margin: 0 auto 1rem; }
@keyframes spin { to { transform: rotate(360deg); } }
.error-message { color: #991b1b; background-color: #fee2e2; padding: 1rem; border-radius: 6px; }
</style>
