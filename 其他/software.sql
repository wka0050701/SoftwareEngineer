create table categories
(
    category_id int auto_increment comment '分类ID，主键'
        primary key,
    name        varchar(50)       not null comment '分类名称',
    sort        int     default 0 null comment '排序权重（数字越小越靠前）',
    status      tinyint default 1 null comment '状态：1-显示，0-隐藏'
)
    comment '商品分类表';

create index idx_sort
    on categories (sort)
    comment '排序索引';

create table order_items
(
    item_id       int auto_increment comment '订单项ID，主键'
        primary key,
    order_id      int            not null comment '所属订单ID（逻辑关联orders.order_id）',
    product_id    int            not null comment '商品ID（逻辑关联products.product_id）',
    product_name  varchar(100)   not null comment '商品名称（下单时快照）',
    product_image varchar(255)   null comment '商品图片（下单时快照）',
    price         decimal(10, 2) not null comment '下单时单价',
    quantity      int default 1  not null comment '购买数量',
    specs         json           null comment '规格选择，JSON格式存储'
)
    comment '订单商品明细表';

create index idx_order
    on order_items (order_id)
    comment '订单ID索引';

create index idx_product
    on order_items (product_id)
    comment '商品ID索引';

create table orders
(
    order_id       int auto_increment comment '订单ID，主键'
        primary key,
    order_no       varchar(32)                              not null comment '订单编号（业务使用）',
    user_id        int                                      not null comment '用户ID（逻辑关联users.user_id）',
    total_amount   decimal(10, 2)                           not null comment '订单总金额',
    actual_amount  decimal(10, 2)                           not null comment '实际支付金额',
    delivery_fee   decimal(10, 2) default 0.00              null comment '配送费',
    contact_name   varchar(50)                              not null comment '收货人姓名',
    contact_phone  varchar(20)                              not null comment '收货人电话',
    address        text                                     not null comment '收货地址',
    remark         text                                     null comment '订单备注',
    status         tinyint        default 1                 null comment '状态：1-待付款，2-已付款，3-制作中，4-配送中，5-已完成，6-已取消',
    payment_method varchar(20)                              null comment '支付方式：wechat/alipay/cash等',
    created_at     timestamp      default CURRENT_TIMESTAMP null comment '创建时间',
    updated_at     timestamp      default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '最后更新时间',
    constraint order_no
        unique (order_no)
)
    comment '订单主表';

create index idx_created_at
    on orders (created_at)
    comment '创建时间索引';

create index idx_order_no
    on orders (order_no)
    comment '订单编号索引';

create index idx_status
    on orders (status)
    comment '状态索引';

create index idx_user
    on orders (user_id)
    comment '用户ID索引';

create table products
(
    product_id   int auto_increment comment '商品ID，主键'
        primary key,
    category_id  int                                 not null comment '所属分类ID（逻辑关联categories.category_id）',
    name         varchar(100)                        not null comment '商品名称',
    image        varchar(255)                        null comment '商品图片URL',
    price        decimal(10, 2)                      not null comment '销售价格',
    origin_price decimal(10, 2)                      null comment '原价（划线价）',
    description  text                                null comment '商品描述',
    stock        int       default 999               null comment '库存数量',
    status       tinyint   default 1                 null comment '状态：1-上架，0-下架',
    sort         int       default 0                 null comment '排序权重',
    created_at   timestamp default CURRENT_TIMESTAMP null comment '创建时间'
)
    comment '商品信息表';

create index idx_category
    on products (category_id)
    comment '分类ID索引';

create index idx_sort
    on products (sort)
    comment '排序索引';

create index idx_status
    on products (status)
    comment '状态索引';

create table reviews
(
    review_id  int auto_increment comment '评价ID，主键'
        primary key,
    order_id   int                                 not null comment '订单ID（逻辑关联orders.order_id）',
    user_id    int                                 not null comment '用户ID（逻辑关联users.user_id）',
    rating     tinyint                             not null comment '评分：1-5星',
    content    text                                null comment '评价内容',
    images     varchar(500)                        null comment '评价图片URL，多个用逗号分隔',
    reply      text                                null comment '商家回复内容',
    created_at timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    constraint order_id
        unique (order_id)
)
    comment '订单评价表';

create index idx_order
    on reviews (order_id)
    comment '订单ID索引';

create index idx_rating
    on reviews (rating)
    comment '评分索引';

create index idx_user
    on reviews (user_id)
    comment '用户ID索引';

create table shop_info
(
    shop_id        int auto_increment comment '商家ID，主键'
        primary key,
    name           varchar(100)                             not null comment '店铺名称',
    logo           varchar(255)                             null comment '店铺LOGO URL',
    description    text                                     null comment '店铺描述',
    phone          varchar(20)                              not null comment '联系电话',
    address        text                                     not null comment '店铺地址',
    business_hours varchar(50)                              null comment '营业时间，如9:00-21:00',
    min_order      decimal(10, 2) default 0.00              null comment '最低起送价',
    delivery_fee   decimal(10, 2) default 0.00              null comment '配送费',
    status         tinyint        default 1                 null comment '营业状态：1-营业中，0-休息中',
    created_at     timestamp      default CURRENT_TIMESTAMP null comment '创建时间'
)
    comment '商家信息表';

create index idx_status
    on shop_info (status)
    comment '营业状态索引';

create table users
(
    user_id         int auto_increment comment '用户ID，主键'
        primary key,
    phone           varchar(20)                         not null comment '手机号（登录账号）',
    password        varchar(255)                        not null comment '加密后的密码',
    nickname        varchar(50)                         null comment '用户昵称',
    avatar          varchar(255)                        null comment '头像URL',
    default_address text                                null comment '默认收货地址',
    created_at      timestamp default CURRENT_TIMESTAMP null comment '创建时间'
)
    comment '用户信息表';

create index idx_phone
    on users (phone)
    comment '手机号索引';


