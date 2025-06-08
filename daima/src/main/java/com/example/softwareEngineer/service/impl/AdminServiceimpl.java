package com.example.softwareEngineer.service.impl;

import com.example.softwareEngineer.DTO.Admin;
import com.example.softwareEngineer.DTO.Order;
import com.example.softwareEngineer.DTO.Product;
import com.example.softwareEngineer.Exception.BusinessException;
import com.example.softwareEngineer.mapper.AdminMapper;
import com.example.softwareEngineer.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        if (adminMapper.existsByProductId(productId)) {
            adminMapper.deleteProduct(productId);
        }else{
            throw new BusinessException("菜品不存在");
        }
    }
    
    /**
     * 添加产品
     * @param product
     */
    @Override
    public void addProduct(Product product) {
        if (!adminMapper.existsByName(product.getName())) {
            adminMapper.addProduct(product);
        }else if (adminMapper.existsByName(product.getName())) {
            throw new BusinessException("菜品名称「" + product.getName() + "」已存在，请更换名称后重试");
        }
    }
    
    /**
     * 更新产品信息
     * @param product
     */
    @Transactional
    public void updateProduct(Product product) {
        // 检查productId是否存在
        if (product.getProductId() == null) {
            throw new BusinessException("修改菜品时必须提供productId");
        }

        // 检查菜品是否存在
        if (!adminMapper.existsByProductId(product.getProductId())) {
            throw new BusinessException("菜品不存在，无法修改");
        }

        // 执行更新
        int rows = adminMapper.updateProduct(product);
        if (rows == 0) {
            throw new BusinessException("菜品更新失败，请检查参数");
        }
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
        adminMapper.updateShopInfo(admin);
    }
    
    /**
     * 查询产品
     * @param categoryId
     * @param status
     * @return
     */

    // 修改后（通过pageNum和pageSize计算offset）
    @Override
    public Map<String, Object> listProductsWithPageInfo(Integer categoryId, Integer status,
                                                        Integer pageNum, Integer pageSize,
                                                        String orderBy) {
        // 计算偏移量（无需再从Controller传递offset）
        int offset = (pageNum - 1) * pageSize;

        // 查询数据列表（直接传递pageNum、pageSize，MyBatis中使用offset和pageSize）
        List<Product> products = adminMapper.getProductList(
                categoryId, status, offset, pageSize, orderBy
        );

        // 查询总数
        long total = adminMapper.countProducts(categoryId, status);
        int pages = (int) Math.ceil((double) total / pageSize);

        // 封装结果
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("list", products);
        result.put("total", total);
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        result.put("pages", pages);
        return result;
    }
    
}
