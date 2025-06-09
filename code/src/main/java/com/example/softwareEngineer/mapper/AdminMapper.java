package com.example.softwareEngineer.mapper;

import com.example.softwareEngineer.DTO.Admin;
import com.example.softwareEngineer.DTO.Order;
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

    @Select("select shop_id, name,description, business_hours,address,delivery_fee, status, created_at " +
            "FROM shop_info " +
            "WHERE shop_id = #{shopId}")
    Admin getAdmin(Integer shopId);

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
    @Insert("INSERT INTO products(category_id,name,price,description,stock,status,image) " +
            "VALUES(#{categoryId},#{name},#{price},#{description},#{stock},#{status},#{image})")
    @Options(useGeneratedKeys = true, keyProperty = "productId")
    void addProduct(Product product);

    // 修改菜品
    @Update("UPDATE products SET category_id=#{categoryId},name=#{name},price=#{price}," +
            "description=#{description},stock=#{stock},status=#{status} " +
            "WHERE product_id=#{productId}")
    int updateProduct(Product product);


    // 分页查询（使用预编译参数，禁止拼接字段名）
    @Select("SELECT * FROM products " +
            "WHERE (#{categoryId} IS NULL OR category_id = #{categoryId}) " +
            "AND (#{status} IS NULL OR status = #{status}) " +
            "ORDER BY ${orderBy}  " + // 由Controller层保证orderby参数合法（如price ASC）
            "LIMIT #{offset}, #{pageSize}")
    List<Product> getProductList(@Param("categoryId") Integer categoryId,
                                 @Param("status") Integer status,
                                 @Param("offset") Integer offset,
                                 @Param("pageSize") Integer pageSize,
                                 @Param("orderBy") String orderBy);

    @Select("SELECT COUNT(1) FROM products " +
            "WHERE (#{categoryId} IS NULL OR category_id = #{categoryId}) " +
            "AND (#{status} IS NULL OR status = #{status})")
    long countProducts(@Param("categoryId") Integer categoryId,
                       @Param("status") Integer status);

    // 修改商家信息
    @Update("UPDATE shop_info SET name=#{name},description=#{description},phone=#{phone}," +
            "address=#{address},business_hours=#{businessHours},min_order=#{minOrder}," +
            "delivery_fee=#{deliveryFee},status=#{status} WHERE shop_id=#{shopId}")
    void updateShopInfo(Admin admin);


    @Select("SELECT product_id,category_id, name, price, stock,status, description, created_at " +
            "FROM products " +
            "WHERE product_id = #{productId}")
    Product getProductById(Integer productId);


    @Update("UPDATE orders set total_amount = #{totalAmount}, actual_amount = #{actualAmount},delivery_fee=#{deliveryFee} where order_id = #{orderId}")
    void changeOrder(Order order);
}
