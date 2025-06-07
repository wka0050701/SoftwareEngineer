package com.example.softwareEngineer.controller;


import com.example.softwareEngineer.DTO.Admin;
import com.example.softwareEngineer.DTO.Product;
import com.example.softwareEngineer.DTO.Result;
import com.example.softwareEngineer.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
        return Result.success();
    }

    //删除菜品
    @DeleteMapping("/product")
    public Result deleteProduct(@RequestParam Integer productId) {
        adminService.deleteProduct(productId);
        return Result.success();
    }

    //修改菜品
    @PutMapping("/product")
    public Result updateProduct(@RequestBody Product product) {
        if (product.getProductId() == null) {
            return Result.error("修改菜品时必须提供productId");
        }
        adminService.updateProduct(product);
        return Result.success();
    }



    //查询菜品信息
    @GetMapping("/product/list")
    public Result listProducts(Integer categoryId, Integer status,
        @RequestParam(defaultValue = "1")Integer pageNum,
        @RequestParam(defaultValue = "10")Integer pageSize)
    {

        Map<String, Object> result = adminService.listProductsWithPageInfo(
                categoryId, status, pageNum, pageSize);
        return Result.success(result);
    }

    //修改商家信息
    @PutMapping("/message")
    public Result updateAdminInfo(@RequestBody Admin admin) {
        adminService.updateAdminInfo(admin);
        return Result.success();
    }
}
