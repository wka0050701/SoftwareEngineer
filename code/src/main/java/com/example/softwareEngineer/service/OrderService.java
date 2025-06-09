package com.example.softwareEngineer.service;


import com.example.softwareEngineer.DTO.Order;
import com.example.softwareEngineer.DTO.OrderItem;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Transactional
public interface OrderService {

    // 创建订单
    Integer createOrder(Integer userId,String jwt);

    // 添加商品到订单
    OrderItem addItemToOrder(Integer orderId, OrderItem item);

    // 取消订单
    void cancelOrder(Integer orderId);

    // 支付订单
    void payOrder(Integer orderId, String paymentMethod, BigDecimal totalAmount);

    // 查询订单
    Order getOrderDetails(Integer orderId);

    // 更新订单状态
    void updateOrderStatus(Integer orderId, Integer newStatus);

    // 处理退款
    void refundOrder(Order order);
}