package com.example.softwareEngineer.DTO;

import lombok.*;

import java.math.BigDecimal;

/**
 * 产品类
 * version:1.0
 */
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private Integer productId;
    private Integer categoryId;
    private String name;
    private BigDecimal price;
    private String description;
    private Integer stock;
    private Integer status;
    private String image;
    
}