## 📘 外卖平台后端接口文档（Java Spring Boot）

------

### 1. 用户模块

#### 1.1 注册 / 登录

- `POST /api/user/login`
  - 参数：手机号、验证码
  - 返回：JWT Token，用户信息

#### 1.2 获取用户信息

- `GET /api/user/info`
  - Header：Authorization: Bearer 
  - 返回：用户信息

#### 1.3 地址管理

- `GET /api/user/address`
- `POST /api/user/address`
- `PUT /api/user/address/{id}`
- `DELETE /api/user/address/{id}`

------

### 2. 商品模块

#### 2.1 获取商家商品分类及商品列表

- `GET /api/shop/{shopId}/categories`
- `GET /api/shop/{shopId}/products?categoryId=xx`

#### 2.2 获取商品详情

- `GET /api/product/{id}`

------

### 3. 订单模块

#### 3.1 创建订单

- `POST /api/order`
  - 参数：商品列表、地址ID、支付方式、备注
  - 返回：订单ID，支付信息（如二维码）

#### 3.2 查询订单状态

- `GET /api/order/{id}`

#### 3.3 用户订单列表

- `GET /api/user/orders?page=1&status=1`

------

### 4. 商家后台模块

#### 4.1 登录

- `POST /api/admin/login`
  - 参数：账号、密码

#### 4.2 商品管理

- `GET /api/admin/products`
- `POST /api/admin/product`
- `PUT /api/admin/product/{id}`
- `DELETE /api/admin/product/{id}`

#### 4.3 订单处理

- `GET /api/admin/orders`
- `PUT /api/admin/order/{id}/status`

#### 4.4 数据统计

- `GET /api/admin/dashboard`

------

### 5. 评价模块

#### 5.1 提交评价

- `POST /api/review`

#### 5.2 查看评价

- `GET /api/shop/{shopId}/reviews`

------

### 6. 公共接口

#### 6.1 获取验证码（短信）

- `POST /api/common/sendCode`
  - 参数：手机号

#### 6.2 文件上传（头像、商品图）

- `POST /api/common/upload`

------

接口返回统一结构：

```json
{
  "code": 0,
  "message": "成功",
  "data": {}
}
```