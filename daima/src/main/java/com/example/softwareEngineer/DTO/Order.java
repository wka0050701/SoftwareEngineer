package com.example.softwareEngineer.DTO;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Integer orderId;        // 订单ID，主键
    private String orderNo;         // 订单编号(业务使用)
    private Integer userId;         // 用户ID(逻辑关联users.user_id)
    private BigDecimal totalAmount; // 订单总金额
    private BigDecimal actualAmount;// 实际支付金额
    private BigDecimal deliveryFee; // 配送费
    private String contactName;     // 收货人姓名
    private String contactPhone;    // 收货人电话
    private String address;         // 收货地址
    private Integer status;         // 状态:1-待付款，2-已付款，3-制作中，4-配送中，5-已完成
    private String paymentMethod;   // 支付方式:wechat/alipay/cash等
    private LocalDateTime createdAt;         // 创建时间
    private LocalDateTime updatedAt;         // 最后更新时间


}
