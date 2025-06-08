import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

// 使用 defineStore 创建一个 store，并给它一个唯一的名字 'cart'
export const useCartStore = defineStore('cart', () => {
  // --- State (状态) ---
  // 用 ref() 来定义 store 的核心数据：一个存放购物车商品的数组
  const items = ref([])

  // --- Getters (计算属性) ---
  // 就像组件里的 computed，它会根据 state 派生出新的值
  
  // 计算购物车中商品的总数量
  const totalItemsCount = computed(() => {
    // 使用 reduce 方法累加每个商品的 quantity
    return items.value.reduce((total, item) => total + item.quantity, 0)
  })

  // 计算购物车中商品的总价格
  const totalPrice = computed(() => {
    return items.value.reduce((total, item) => {
      return total + (item.price * item.quantity)
    }, 0).toFixed(2) // 保留两位小数
  })

  // --- Actions (方法) ---
  // 定义可以修改 state 的方法

  /**
   * 将一个商品添加到购物车
   * @param {object} product - 要添加的商品对象
   */
  function addToCart(product) {
    // 检查购物车中是否已经存在这个商品
    const existingItem = items.value.find(item => item.id === product.id)

    if (existingItem) {
      // 如果已存在，只增加它的数量
      existingItem.quantity++
      console.log(`商品 "${product.name}" 的数量已增加到 ${existingItem.quantity}`)
    } else {
      // 如果不存在，将新商品添加到数组中，并设置初始数量为 1
      items.value.push({ ...product, quantity: 1 })
      console.log(`已将新商品 "${product.name}" 加入购物车`)
    }
  }

  /**
   * 从购物车中移除一个商品
   * @param {number} productId - 要移除的商品的ID
   */
  function removeFromCart(productId) {
    const index = items.value.findIndex(item => item.id === productId)
    if (index !== -1) {
      console.log(`已从购物车移除商品 "${items.value[index].name}"`)
      items.value.splice(index, 1)
    }
  }
  
  /**
   * 增加指定商品的数量
   * @param {number} productId 
   */
  function incrementQuantity(productId) {
    const item = items.value.find(item => item.id === productId)
    if (item) {
      item.quantity++
    }
  }
  
  /**
   * 减少指定商品的数量，如果数量减到0，则移除该商品
   * @param {number} productId 
   */
  function decrementQuantity(productId) {
    const item = items.value.find(item => item.id === productId)
    if (item) {
      if (item.quantity > 1) {
        item.quantity--
      } else {
        removeFromCart(productId)
      }
    }
  }

  /**
   * 清空购物车
   */
  function clearCart() {
    items.value = []
    console.log("购物车已清空")
  }

  // 最后，将所有需要暴露给外部使用的 state, getters, actions 返回
  return { 
    items, 
    totalItemsCount,
    totalPrice,
    addToCart, 
    removeFromCart,
    incrementQuantity,
    decrementQuantity,
    clearCart
  }
})
