package com.example.softwareEngineer.controller;


import com.example.softwareEngineer.DTO.Admin;
import com.example.softwareEngineer.DTO.Product;
import com.example.softwareEngineer.DTO.Result;
import com.example.softwareEngineer.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商家控制类
 * version:1.0
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    //添加菜品
    @PostMapping("/product")
    public Result addProduct(@RequestBody Product product) {
        adminService.addProduct(product);
        return Result.success("添加成功");
    }
    //删除菜品
    @DeleteMapping("/product/{productId}")
    public Result deleteProduct(@PathVariable("productId") Integer productId) {
        adminService.deleteProduct(productId);
        return Result.success("删除成功");
    }
    //修改菜品
    @PutMapping("/product")
    public Result updateProduct(@RequestBody Product product) {
        adminService.updateProduct(product);
        return Result.success("修改成功");
    }
    //修改商家信息
    @PutMapping("/change")
    public Result updateAdminInfo(@RequestBody Admin admin) {
        // 实际开发中商家ID应从登录token中获取
        adminService.updateAdminInfo(admin);
        return Result.success("商家信息修改成功");
    }
    //查询菜品信息
    @GetMapping("/product/list")
    public Result listProducts(
        @RequestParam(required = false) Integer categoryId,
        @RequestParam(required = false) Integer status) {
        
        List<Product> products = adminService.listProducts(categoryId, status);
        return Result.success(products); // 使用查询成功的静态方法
    }
}
