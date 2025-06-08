/**
 * 这是一个模拟 API 的文件。
 * 它会模拟网络延迟和不同的后端响应，
 * 以便在没有真实后端的情况下进行前端开发和测试。
 */

// --- 身份认证相关 API ---

export function mockLoginAPI(formData) {
  console.log("正在调用【模拟】登录接口，接收到的数据:", formData);
  return new Promise((resolve) => {
    setTimeout(() => {
      if (formData.phone === '1' && formData.password === '123') {
        resolve({ code: 1, message: "成功", data: { id: 1, jwt: "mock-jwt-for-merchant-user" } });
      } else if (formData.phone === '2' && formData.password === '123') {
        resolve({ code: 1, message: "成功", data: { id: 102, jwt: "mock-jwt-for-normal-user" } });
      } else {
        resolve({ code: 0, message: "模拟的错误：手机号或密码不正确", data: null });
      }
    }, 1000);
  });
}

export function mockChangePasswordAPI(formData) {
    console.log("正在调用【模拟】修改密码接口，接收到的数据:", formData);
    return new Promise((resolve) => {
        setTimeout(() => {
            if (['1', '2'].includes(formData.phone)) {
                resolve({ code: 1, message: "成功" });
            } else {
                resolve({ code: 0, message: "模拟的错误：该手机号未注册" });
            }
        }, 1000);
    });
}

// --- 普通用户相关 API ---

export function mockGetUserProfileAPI(userId, jwt) {
  console.log(`正在调用【模拟】查询用户信息接口，查询 userId: ${userId}`);
  return new Promise((resolve) => {
    setTimeout(() => {
      if (!jwt) return resolve({ code: 0, message: "模拟的错误：未授权访问" });
      if (String(userId) === '102') {
        resolve({
          code: 1, message: "成功",
          data: { phone: "13800000102", nickname: "hhh", defaultAddress: "广州天河区" }
        });
      } else {
        resolve({ code: 0, message: "模拟的错误：用户不存在" });
      }
    }, 800);
  });
}

export function mockUpdateUserProfileAPI(formData, jwt) {
    console.log("正在调用【模拟】修改用户信息接口，提交的数据:", formData);
    return new Promise((resolve) => {
        setTimeout(() => {
            if (!jwt) return resolve({ code: 0, message: "模拟的错误：未授权访问" });
            console.log("模拟场景：用户信息更新成功！")
            resolve({ code: 1, message: "成功" });
        }, 1000);
    });
}

// --- 新增：商家相关 API ---

// 模拟获取商家信息
export function mockGetMerchantInfoAPI(userId, jwt) {
  console.log(`正在调用【模拟】查询商家信息接口，查询 userId: ${userId}`);
   return new Promise((resolve) => {
    setTimeout(() => {
      if (!jwt || String(userId) !== '1') {
        return resolve({ code: 0, message: "模拟的错误：未授权或非商家用户" });
      }
      resolve({
        code: 1,
        message: "成功",
        data: {
          name: "Los Pollos Hermanos 炸鸡店",
          description: "这样好吃得像溜冰一样的炸鸡哪里找（哭。",
          phone: "505-867-5309",
          address: "新墨西哥州阿尔伯克基市中央大道845号",
          businessHours: "10:00-22:00",
          minOrder: 20,
          deliveryFee: 5,
          status: 1 // 1-营业
        }
      });
    }, 800);
  });
}

// 模拟修改商家信息
export function mockUpdateMerchantInfoAPI(formData, jwt) {
  console.log("正在调用【模拟】修改商家信息接口，提交的数据:", formData);
  return new Promise((resolve) => {
    setTimeout(() => {
      if (!jwt || String(formData.userId) !== '1') {
        return resolve({ code: 0, message: "模拟的错误：未授权或非商家用户" });
      }
      console.log("模拟场景：商家信息更新成功！");
      resolve({ code: 1, message: "成功" });
    }, 1000);
  });
}


// --- 新增：商品相关 API ---
export function mockGetProductsAPI() {
  console.log("正在调用【模拟】获取商品列表接口");
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        code: 1,
        message: "成功",
        data: [
          { id: 1001, name: "招牌黄金脆皮鸡", description: "外酥里嫩，汁多味美，本店必点！", price: 18, imageUrl: "https://placehold.co/300x200/f6ad55/ffffff?text=炸鸡" },
          { id: 1002, name: "经典牛肉汉堡", description: "100%纯牛肉饼，搭配新鲜蔬菜和秘制酱料。", price: 25, imageUrl: "https://placehold.co/300x200/f687b3/ffffff?text=汉堡" },
          { id: 1003, name: "香辣鸡翅（4只）", description: "精选鸡翅，腌制入味，辣得过瘾。", price: 15, imageUrl: "https://placehold.co/300x200/76e8f5/ffffff?text=鸡翅" },
          { id: 1004, name: "大份薯条", description: "外脆内软，金黄诱人。", price: 10, imageUrl: "https://placehold.co/300x200/a8d879/ffffff?text=薯条" },
          { id: 1005, name: "冰镇快乐水", description: "吃炸鸡汉堡怎么能没有它？", price: 5, imageUrl: "https://placehold.co/300x200/7f7fd5/ffffff?text=可乐" },
          { id: 1006, name: "巧克力圣代", description: "丝滑冰淇淋，淋上浓郁巧克力酱。", price: 8, imageUrl: "https://placehold.co/300x200/e0c3fc/ffffff?text=圣代" },
        ]
      })
    }, 800);
  });
}

export function mockCreateOrderAPI() {
    console.log("正在调用【模拟】创建订单接口");
    return new Promise((resolve) => {
        setTimeout(() => {
            const mockOrderId = Date.now(); // 用时间戳生成一个唯一的订单ID
            console.log(`模拟场景：订单创建成功，订单ID为 ${mockOrderId}`);
            resolve({
                code: 1,
                message: "成功",
                data: {
                    orderId: mockOrderId
                }
            });
        }, 500);
    });
}

export function mockAddItemsToOrderAPI(orderId, itemData) {
    console.log(`正在调用【模拟】添加商品到订单接口，订单ID: ${orderId}, 商品数据:`, itemData);
    return new Promise((resolve) => {
        setTimeout(() => {
            console.log(`模拟场景：商品 ${itemData.productId} 已成功添加到订单 ${orderId}`);
            resolve({
                code: 1,
                message: "成功",
                data: {
                    itemId: Date.now() + itemData.productId,
                    orderId: orderId,
                    productId: itemData.productId,
                    quantity: itemData.quantity,
                    price: 20, // 假设一个固定的价格
                    totalPrice: 20 * itemData.quantity
                }
            });
        }, 200); // 模拟每个商品添加的短暂延迟
    });
}
