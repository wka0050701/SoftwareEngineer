package com.example.softwareEngineer.service.impl;

import com.example.softwareEngineer.DTO.Admin;
import com.example.softwareEngineer.DTO.Product;
import com.example.softwareEngineer.mapper.AdminMapper;
import com.example.softwareEngineer.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商家操作类
 * version:1.0
 */
@Service
public class AdminServiceimpl implements AdminService {
    
    @Autowired
    private AdminMapper adminMapper;
    
    /**
     * 删除产品
     * @param productId
     */
    @Override
    @Transactional
    public void deleteProduct(Integer productId) {
        // 这里可以添加业务逻辑，如检查菜品是否存在等
        adminMapper.deleteById(productId);
    }
    
    /**
     * 添加产品
     * @param product
     */
    @Override
    public void addProduct(Product product) {
        // 这里可以添加业务逻辑，如参数校验等
        adminMapper.insert(product);
    }
    
    /**
     * 更新产品信息
     * @param product
     */
    @Transactional
    public void updateProduct(Product product) {
        // 这里可以添加业务逻辑，如参数校验等
        adminMapper.updateProduct(product);
    }
    
    /**
     * 更新商家信息
     * @param admin
     */
    @Override
    @Transactional
    public void updateAdminInfo(Admin admin) {
        // 实际开发中应添加参数校验
        if (admin.getPhone() != null && !admin.getPhone().matches("^1[3-9]\\d{9}$")) {
            throw new IllegalArgumentException("手机号格式不正确");
        }
        adminMapper.updateAdminInfo(admin);
    }
    
    /**
     * 查询产品
     * @param categoryId
     * @param status
     * @return
     */
    @Override
    public List<Product> listProducts(Integer categoryId, Integer status) {
        return adminMapper.selectList(categoryId, status);
    }
    
}
