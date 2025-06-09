package com.example.softwareEngineer.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 商家类
 * version:1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin {
    private Integer shopId; // 商家ID，通常从登录信息获取
    private String name;
    private String description;
    private String phone;
    private String address;
    private String businessHours;
    private Integer minOrder;
    private BigDecimal deliveryFee;
    private Integer status;
}
