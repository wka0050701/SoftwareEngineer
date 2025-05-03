package com.example.softwareEngineer.mapper;

import com.example.softwareEngineer.DTO.Admin;
import com.example.softwareEngineer.DTO.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商家Mapper类
 * version:1.0
 */
@Mapper
public interface AdminMapper {
    //根据id删除产品
    void deleteById(Integer productId);
    
    //添加菜品
    void insert(Product product);
    //更新产品信息
    void updateProduct(Product product);
    
    //更新商家信息
    void updateAdminInfo(Admin admin);
    //查询所有产品
    List<Product> selectList(@Param("categoryId") Integer categoryId,
                             @Param("status") Integer status);
}
