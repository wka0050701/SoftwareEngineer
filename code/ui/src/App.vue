<script setup>
import { ref, watch } from 'vue'
import { RouterLink, RouterView, useRoute, useRouter } from 'vue-router'
// 导入我们创建的 useCartStore
import { useCartStore } from '@/stores/cart'

const router = useRouter()
const route = useRoute()

// 获取 cart store 的实例
const cartStore = useCartStore()

// 状态变量
const isLoggedIn = ref(false)
const userRole = ref(null)

function checkAuthStatus() {
  const jwt = localStorage.getItem('jwt')
  const userId = localStorage.getItem('userId')
  if (jwt && userId) {
    isLoggedIn.value = true
    userRole.value = userId === '1' ? 'merchant' : 'user'
  } else {
    isLoggedIn.value = false
    userRole.value = null
  }
}

watch(() => route.path, checkAuthStatus, { immediate: true })

function handleLogout() {
  // 退出登录时，也应该清空购物车
  cartStore.clearCart()
  localStorage.clear()
  checkAuthStatus()
  router.push('/login')
}
</script>

<template>
  <div>
    <header>
      <div class="wrapper">
        <!-- 标注1：移除了 nav-left 和 nav-right 的 div，所有链接都直接放在 nav 内 -->
        <nav>
          <!-- 公共链接，始终显示 -->
          <RouterLink to="/">首页</RouterLink>

          <!-- 根据登录状态显示不同链接 -->
          <template v-if="isLoggedIn">
            <!-- 如果是普通用户，显示这些链接 -->
            <template v-if="userRole === 'user'">
              <RouterLink to="/products">开始点餐</RouterLink>
              <RouterLink to="/profile">个人中心</RouterLink>
              <RouterLink to="/cart" class="cart-link">
                🛒
                <span v-if="cartStore.totalItemsCount > 0" class="cart-badge">
                  {{ cartStore.totalItemsCount }}
                </span>
              </RouterLink>
            </template>
            
            <!-- 如果是商家，显示这个链接 -->
            <RouterLink v-if="userRole === 'merchant'" to="/merchant">商家后台</RouterLink>
            
            <!-- 所有已登录用户都显示退出登录 -->
            <a href="#" @click.prevent="handleLogout" class="logout-link">退出登录</a>
          </template>
          
          <!-- 未登录时显示这些链接 -->
          <template v-else>
            <RouterLink to="/register">注册</RouterLink>
            <RouterLink to="/login">登录</RouterLink>
          </template>
        </nav>
      </div>
    </header>

    <main>
      <RouterView />
    </main>
  </div>
</template>

<style scoped>
header {
  background-color: #ffffff;
  border-bottom: 1px solid #e5e7eb;
  padding: 0 2rem;
  position: sticky;
  top: 0;
  z-index: 50;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
}

.wrapper {
  max-width: 1280px;
  margin: 0 auto;
}

/* 标注2：修改 nav 样式，回到居中布局 */
nav {
  display: flex;
  justify-content: center; /* 让所有链接水平居中 */
  align-items: center;
  gap: 1.5rem; /* 保留更美观的间距方式 */
  padding: 1rem 0;
  font-size: 1rem;
}

nav a {
  padding: 0.5rem 1rem;
  color: #374151;
  text-decoration: none;
  font-weight: 500;
  border-radius: 6px;
  transition: background-color 0.2s, color 0.2s;
}

nav a:hover {
  background-color: #f3f4f6;
  color: #1f2937;
}

nav a.router-link-exact-active {
  color: hsla(160, 100%, 37%, 1);
  background-color: hsla(160, 100%, 37%, 0.1);
  font-weight: 600;
}

.logout-link {
  color: #ef4444;
}

.logout-link:hover {
  background-color: #fee2e2;
  color: #991b1b;
}

/* 购物车图标和徽章的样式保持不变 */
.cart-link {
  position: relative;
  font-size: 1.5rem;
  padding: 0.5rem;
}

.cart-badge {
  position: absolute;
  top: 0;
  right: 0;
  background-color: #ef4444;
  color: white;
  width: 18px;
  height: 18px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 0.75rem;
  font-weight: bold;
}

main {
  padding-top: 1rem;
}
</style>
