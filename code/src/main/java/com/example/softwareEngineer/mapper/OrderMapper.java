package com.example.softwareEngineer.mapper;

import com.example.softwareEngineer.DTO.Order;
import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;

@Mapper
public interface OrderMapper {

    @Insert("INSERT INTO orders (" +
            "order_no, user_id, status, created_at, " +
            "contact_phone, total_amount, actual_amount, contact_name, address, " +
            "delivery_fee, updated_at, payment_method" + // 添加所有字段
            ") VALUES (" +
            "#{orderNo}, #{userId}, #{status}, #{createdAt}, " +
            "#{contactPhone}, #{totalAmount}, #{actualAmount}, #{contactName}, #{address}, " +
            "#{deliveryFee}, #{updatedAt}, #{paymentMethod}" + // 明确映射所有字段
            ")")
    @Options(useGeneratedKeys = true, keyProperty = "orderId")
    void createOrder(Order order);

    // 查询订单
    @Select("SELECT * FROM orders WHERE order_id = #{orderId}")
    Order getOrderById(Integer orderId);

    // 更新订单状态
    @Update("UPDATE orders SET status = #{status} WHERE order_id = #{orderId}")
    void updateOrderStatus(@Param("orderId") Integer orderId, @Param("status") Integer status);

    // 删除订单（逻辑删除或物理删除）
    @Delete("DELETE FROM orders WHERE order_id = #{orderId}")
    void deleteOrder(Integer orderId);


    @Update("update orders set status=#{status} where order_id = #{orderId}")
    void refundOrder(@Param("orderId") Integer orderId, @Param("status") Integer status);


    @Update("update orders set status = #{status},payment_method=#{paymentMethod} ,actual_amount=#{actualAmount} where order_id=#{orderId}")
    void updatePayOrder(@Param("orderId")Integer orderId,@Param("status")Integer status,@Param("paymentMethod")String paymentMethod, @Param("actualAmount") BigDecimal actualAmount);
}
