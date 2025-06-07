package com.example.softwareEngineer.mapper;

import com.example.softwareEngineer.DTO.Admin;
import com.example.softwareEngineer.DTO.Product;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 商家Mapper类
 * version:1.0
 */
@Mapper
public interface AdminMapper {
    //删除菜品
    @Delete("delete from products where product_id = #{productId}")
    void deleteProduct(Integer productId);

    /**
     * 检查菜品名称是否存在
     * @param name 菜品名称
     * @return true=存在，false=不存在
     */
    @Select("SELECT COUNT(1) FROM products WHERE name = #{name}")
    boolean existsByName(@Param("name") String name);

    @Select("SELECT COUNT(1) FROM products WHERE product_id = #{productId}")
    boolean existsByProductId(@Param("productId") Integer productId);

    //添加菜品
    @Insert("INSERT INTO products(category_id,name,price,description,stock,status) " +
            "VALUES(#{categoryId},#{name},#{price},#{description},#{stock},#{status})")
    @Options(useGeneratedKeys = true, keyProperty = "productId")
    int addProduct(Product product);

    // 修改菜品
    @Update("UPDATE products SET category_id=#{categoryId},name=#{name},price=#{price}," +
            "description=#{description},stock=#{stock},status=#{status} " +
            "WHERE product_id=#{productId}")
    int updateProduct(Product product);


    // 分页查询菜品列表（纯SQL版本）
    @Select("SELECT * FROM products " +
            "WHERE (#{categoryId} IS NULL OR category_id = #{categoryId}) " +
            "AND (#{status} IS NULL OR status = #{status}) " +
            "ORDER BY product_id DESC " +
            "LIMIT #{offset}, #{pageSize}")
    List<Product> getProductList(@Param("categoryId") Integer categoryId,
                                 @Param("status") Integer status,
                                 @Param("offset") Integer offset,
                                 @Param("pageSize") Integer pageSize);

    @Select("SELECT COUNT(1) FROM products " +
            "WHERE (#{categoryId} IS NULL OR category_id = #{categoryId}) " +
            "AND (#{status} IS NULL OR status = #{status})")
    long countProducts(@Param("categoryId") Integer categoryId,
                       @Param("status") Integer status);

    // 修改商家信息
    @Update("UPDATE shop_info SET name=#{name},description=#{description},phone=#{phone}," +
            "address=#{address},business_hours=#{businessHours},min_order=#{minOrder}," +
            "delivery_fee=#{deliveryFee},status=#{status} WHERE shop_id=#{shopId}")
    int updateShopInfo(Admin admin);

}
