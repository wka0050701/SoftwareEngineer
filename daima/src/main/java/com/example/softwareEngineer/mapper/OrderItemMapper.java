package com.example.softwareEngineer.mapper;

import com.example.softwareEngineer.DTO.OrderItem;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

@Mapper
public interface OrderItemMapper {

    @Insert("INSERT INTO order_items " +
            "(order_id, product_id, quantity, price,total_price) " +
            "VALUES (#{orderId}, #{productId}, #{quantity}, #{price},#{totalPrice})")
    @Options(useGeneratedKeys = true, keyProperty = "itemId")
    void addOrderItem(OrderItem item);
}
