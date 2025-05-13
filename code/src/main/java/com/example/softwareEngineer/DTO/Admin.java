package com.example.softwareEngineer.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商家类
 * version:1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin {
    private Integer id; // 商家ID，通常从登录信息获取
    private String name;
    private String description;
    private String phone;
    private String address;
    private String businessHours;
    private Integer minOrder;
    private Integer deliveryFee;
    private Integer status;
}
