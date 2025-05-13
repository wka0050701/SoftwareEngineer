package com.example.softwareEngineer.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 产品类
 * version:1.0
 */
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
    
}