<script setup>
import { useCartStore } from '@/stores/cart.js';
import { RouterLink } from 'vue-router';

// 获取 cart store 的实例，以便在模板中直接使用
const cartStore = useCartStore();
</script>

<template>
  <div class="page-container">
    <div class="cart-card">
      <h1 class="cart-title">我的购物车</h1>

      <!-- 当购物车为空时显示 -->
      <div v-if="cartStore.items.length === 0" class="empty-cart">
        <div class="empty-icon">🛒</div>
        <h2>您的购物车是空的</h2>
        <p>快去挑选一些美味的商品吧！</p>
        <RouterLink to="/products" class="action-btn">去点餐</RouterLink>
      </div>

      <!-- 当购物车不为空时显示 -->
      <div v-else>
        <div class="cart-items">
          <!-- 循环遍历购物车中的每一个商品 -->
          <div v-for="item in cartStore.items" :key="item.id" class="cart-item">
            <img :src="item.imageUrl" :alt="item.name" class="item-image" />
            <div class="item-details">
              <h3 class="item-name">{{ item.name }}</h3>
              <p class="item-price">¥{{ item.price }}</p>
            </div>
            <div class="item-actions">
              <div class="quantity-control">
                <button @click="cartStore.decrementQuantity(item.id)" class="quantity-btn">-</button>
                <span class="quantity-display">{{ item.quantity }}</span>
                <button @click="cartStore.incrementQuantity(item.id)" class="quantity-btn">+</button>
              </div>
              <button @click="cartStore.removeFromCart(item.id)" class="remove-btn">移除</button>
            </div>
          </div>
        </div>

        <div class="cart-summary">
          <div class="summary-line">
            <span>商品总数：</span>
            <span>{{ cartStore.totalItemsCount }} 件</span>
          </div>
          <div class="summary-line total">
            <span>总计：</span>
            <span class="total-price">¥{{ cartStore.totalPrice }}</span>
          </div>
          <div class="summary-actions">
            <button @click="cartStore.clearCart()" class="clear-btn">清空购物车</button>
            <!-- --- 核心修改在这里 --- -->
            <RouterLink to="/checkout" class="checkout-btn">去结算</RouterLink>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.page-container {
  max-width: 900px;
  margin: 2rem auto;
  padding: 2rem;
  background-color: #f7f8fa;
}

.cart-card {
  background-color: #fff;
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.cart-title {
  font-size: 2rem;
  font-weight: 700;
  text-align: center;
  margin-bottom: 2rem;
  border-bottom: 1px solid #eee;
  padding-bottom: 1rem;
}

/* 购物车商品项 */
.cart-items {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.cart-item {
  display: flex;
  align-items: center;
  gap: 1.5rem;
  padding-bottom: 1.5rem;
  border-bottom: 1px solid #f0f2f5;
}

.cart-item:last-child {
  border-bottom: none;
}

.item-image {
  width: 80px;
  height: 80px;
  border-radius: 8px;
  object-fit: cover;
}

.item-details {
  flex-grow: 1;
}

.item-name {
  font-size: 1.1rem;
  font-weight: 600;
  margin: 0;
}

.item-price {
  font-size: 1rem;
  color: #888;
  margin-top: 0.25rem;
}

.item-actions {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 0.5rem;
}

.quantity-control {
  display: flex;
  align-items: center;
  border: 1px solid #ddd;
  border-radius: 6px;
}

.quantity-btn {
  background: none;
  border: none;
  width: 30px;
  height: 30px;
  font-size: 1.2rem;
  cursor: pointer;
  color: #555;
}

.quantity-display {
  width: 40px;
  text-align: center;
  font-weight: 500;
  font-size: 1rem;
}

.remove-btn {
  background: none;
  border: none;
  color: #999;
  font-size: 0.85rem;
  cursor: pointer;
}
.remove-btn:hover {
  color: #ef4444;
  text-decoration: underline;
}

/* 购物车为空时的样式 */
.empty-cart {
  text-align: center;
  padding: 4rem 0;
}
.empty-icon {
  font-size: 4rem;
  line-height: 1;
}
.empty-cart h2 {
  font-size: 1.5rem;
  margin-top: 1.5rem;
}
.empty-cart p {
  color: #888;
  margin-bottom: 2rem;
}
.action-btn {
  border: none;
  background-color: hsla(160, 100%, 37%, 1);
  color: white;
  padding: 0.8rem 2rem;
  border-radius: 50px;
  font-size: 1rem;
  font-weight: bold;
  cursor: pointer;
  text-decoration: none;
}
.action-btn:hover {
  background-color: hsla(160, 100%, 30%, 1);
}

/* 购物车总结区域 */
.cart-summary {
  margin-top: 2rem;
  padding-top: 1.5rem;
  border-top: 2px dashed #ddd;
}

.summary-line {
  display: flex;
  justify-content: space-between;
  font-size: 1rem;
  margin-bottom: 0.8rem;
  color: #555;
}
.summary-line.total {
  font-size: 1.2rem;
  font-weight: bold;
  color: #333;
}
.total-price {
  color: #ef4444;
}

.summary-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 1.5rem;
}

.clear-btn {
  background: none;
  border: 1px solid #ddd;
  color: #888;
  padding: 0.7rem 1.5rem;
  border-radius: 8px;
  cursor: pointer;
}
.clear-btn:hover {
  background-color: #f7f8fa;
  border-color: #ccc;
}
.checkout-btn {
  border: none;
  background-color: hsla(160, 100%, 37%, 1);
  color: white;
  padding: 0.8rem 2rem;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: bold;
  cursor: pointer;
  text-decoration: none; /* 为 RouterLink 添加 */
  display: inline-block; /* 为 RouterLink 添加 */
}
.checkout-btn:hover {
  background-color: hsla(160, 100%, 30%, 1);
}
</style>
