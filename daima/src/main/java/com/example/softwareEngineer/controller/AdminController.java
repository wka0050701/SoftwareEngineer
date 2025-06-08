package com.example.softwareEngineer.controller;


import com.example.softwareEngineer.DTO.Admin;
import com.example.softwareEngineer.DTO.Product;
import com.example.softwareEngineer.DTO.Result;
import com.example.softwareEngineer.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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
    public Result deleteProduct(@RequestBody Product product) {
        adminService.deleteProduct(product.getProductId());
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

    // 排序处理方法（封装业务逻辑）
    private String handleSort(String sortField, Integer sortOrder) {
        // 排序字段白名单（使用枚举或常量列表）
        List<String> validFields = Arrays.asList("price", "stock", "name", "status", "product_id");
        if (sortField == null) {
            return "product_id DESC"; // 默认按ID降序
        }
        if (!validFields.contains(sortField)) {
            throw new IllegalArgumentException("无效的排序字段：" + sortField);
        }
        String order = sortOrder == 1 ? "ASC" : "DESC"; // 默认降序（与原逻辑一致）
        return sortField + " " + order;
    }

    //查询菜品信息
    @GetMapping("/product/list")
    public Result listProducts(@RequestParam(required = false) Integer categoryId,    // 分类ID（可选）
                               @RequestParam(required = false) Integer status,        // 状态（可选）
                               @RequestParam(defaultValue = "1") Integer pageNum,      // 页码（默认1）
                               @RequestParam(defaultValue = "10") Integer pageSize,    // 每页数量（默认10）
                               @RequestParam(required = false) Integer sortOrder,       // 排序方向（1升序，2降序）
                               @RequestParam(required = false) String sortField)        // 排序字段（如price、stock）)
    {

        // 校验页码和每页数量
        if (pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize < 1 || pageSize > 100) {
            pageSize = 10; // 限制每页最大数量
        }

        // 处理排序逻辑（含白名单校验）
        String orderBy = handleSort(sortField, sortOrder);

        // 调用Service层（传递pageNum和pageSize，无需offset）
        Map<String, Object> result = adminService.listProductsWithPageInfo(
                categoryId, status, pageNum, pageSize, orderBy
        );
        return Result.success(result);
    }

    //修改商家信息
    @PutMapping("/message")
    public Result updateAdminInfo(@RequestBody Admin admin) {
        adminService.updateAdminInfo(admin);
        return Result.success();
    }
}
