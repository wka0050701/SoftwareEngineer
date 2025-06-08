package com.example.softwareEngineer.service;


import com.example.softwareEngineer.DTO.Admin;
import com.example.softwareEngineer.DTO.Product;

import java.util.List;
import java.util.Map;

/**
 * 商家行为接口层
 * version:1.0
 */

public interface AdminService {
    //删除产品
    void deleteProduct(Integer productId);
    //添加产品
    void addProduct(Product product);
    //更新产品信息
    void updateProduct(Product product);
    //更新商家信息
    void updateAdminInfo(Admin admin);
    //查询产品
    Map<String, Object> listProductsWithPageInfo(Integer categoryId, Integer status, Integer pageNum, Integer pageSize,String OrderBy);
}
