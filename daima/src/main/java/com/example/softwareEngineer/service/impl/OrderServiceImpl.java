package com.example.softwareEngineer.service.impl;

import com.example.softwareEngineer.DTO.*;
import com.example.softwareEngineer.Exception.BusinessException;
import com.example.softwareEngineer.OrderStatusEnum;
import com.example.softwareEngineer.mapper.AdminMapper;
import com.example.softwareEngineer.mapper.OrderItemMapper;
import com.example.softwareEngineer.mapper.OrderMapper;
import com.example.softwareEngineer.service.AdminService;
import com.example.softwareEngineer.service.OrderService;
import com.example.softwareEngineer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final UserService userService;
    private final AdminService adminService;

    @Autowired
    public OrderServiceImpl(OrderMapper orderMapper, OrderItemMapper orderItemMapper, UserService userService, AdminService adminService) {
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.userService = userService;
        this.adminService = adminService;
    }

    // 生成订单号（示例）
    private String generateOrderNo() {
        return "ORD" + System.currentTimeMillis();
    }

    @Override
    public Integer createOrder(Integer userId,String jwt) {
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderNo(generateOrderNo()); // 生成订单号
        order.setStatus(1); // 状态：待付款
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        User user = userService.getUserById(userId);

        // 设置联系信息（如果用户信息中存在则使用，否则使用默认值）
        order.setContactPhone(user != null && user.getPhone() != null ? user.getPhone() : "未提供");
        order.setContactName(user != null && user.getNickname() != null ? user.getNickname() : "匿名");
        order.setAddress(user != null && user.getDefaultAddress() != null ? user.getDefaultAddress() : "未知地址");

        // 设置金额信息（示例：默认金额为0.00，实际业务中可能需要计算）
        order.setTotalAmount(BigDecimal.ZERO);
        order.setActualAmount(BigDecimal.ZERO);
        order.setDeliveryFee(BigDecimal.ZERO);

        // 插入订单（MyBatis自动回填orderId）
        orderMapper.createOrder(order);

        return order.getOrderId();
    }

    @Override
    public OrderItem addItemToOrder(Integer productId, OrderItem item) {
        // 校验订单是否存在
        Order order = orderMapper.getOrderById(item.getOrderId());
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        Product product = adminService.getProductById(productId);
        if(product == null) {
            throw new BusinessException("商品不存在");
        }

        item.setProductId(productId);

        BigDecimal price = product.getPrice();
        item.setPrice(price);

        // 计算总价
        if (item.getQuantity() == null || item.getQuantity() <= 0) {
            item.setQuantity(1); // 默认数量为1
        }

        item.setTotalPrice(price.multiply(new BigDecimal(item.getQuantity())));
        orderItemMapper.addOrderItem(item);
        order.setTotalAmount(order.getTotalAmount().add(item.getTotalPrice()));
        order.setUpdatedAt(LocalDateTime.now());

        Admin admin = adminService.getAdmin(1);

        order.setDeliveryFee(admin.getDeliveryFee());
        order.setActualAmount(order.getTotalAmount().add(order.getDeliveryFee()));

        adminService.changeOrder(order);
        return item;
    }

    @Override
    public void payOrder(Integer orderId, String paymentMethod, BigDecimal totalAmount) {

        Order order = orderMapper.getOrderById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 校验订单状态
        if (!OrderStatusEnum.UNPAID.getCode().equals(order.getStatus())) {
            throw new BusinessException("订单状态不是未支付");
        }

        BigDecimal payAmount = order.getActualAmount();

        orderMapper.updatePayOrder(orderId, OrderStatusEnum.PAID.getCode(),paymentMethod,payAmount);
    }

    @Override
    public Order getOrderDetails(Integer orderId) {
        return orderMapper.getOrderById(orderId);
    }

    @Override
    public void updateOrderStatus(Integer orderId, Integer newStatus) {
        orderMapper.updateOrderStatus(orderId, newStatus);
    }

    @Override
    public void refundOrder(Order order) {
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 校验订单状态
        if (!OrderStatusEnum.PAID.getCode().equals(order.getStatus())) {
            throw new BusinessException("订单状态不是已支付");
        }

        orderMapper.refundOrder(order.getOrderId(), OrderStatusEnum.CANCELED.getCode());
    }

    @Override
    public void cancelOrder(Integer orderId) {
        Order order = orderMapper.getOrderById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        // 使用枚举替代硬编码
        if (!Objects.equals(order.getStatus(), OrderStatusEnum.UNPAID.getCode())) {
            throw new BusinessException("订单状态不可取消，当前状态：" +
                    OrderStatusEnum.fromCode(order.getStatus()).getDesc());
        }
        orderMapper.updateOrderStatus(orderId, OrderStatusEnum.CANCELED.getCode());
        orderMapper.deleteOrder(orderId);
    }
}
