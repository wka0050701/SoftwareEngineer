package com.example.softwareEngineer.DTO;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderItem {
    private Integer itemId;
    private Integer orderId;
    private Integer productId;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;
}
