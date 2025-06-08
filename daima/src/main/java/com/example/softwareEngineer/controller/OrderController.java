package com.example.softwareEngineer.controller;

import com.example.softwareEngineer.DTO.Order;
import com.example.softwareEngineer.DTO.OrderItem;
import com.example.softwareEngineer.DTO.Product;
import com.example.softwareEngineer.DTO.Result;
import com.example.softwareEngineer.mapper.OrderMapper;
import com.example.softwareEngineer.service.OrderService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import utils.JwtUtils;

import java.util.Collections;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderMapper orderMapper;

    // POST /orders 创建订单
    @PostMapping
    public Result createOrder( @RequestParam("userId") Integer userId,
                               @RequestHeader(value = "Authorization", required = false) String jwt) {

        if (userId == null) {
            return Result.error("用户ID不能为空");
        }

        try {
            Claims claims = JwtUtils.parseJWT(jwt);
            Integer userIdFromJwt = (Integer) claims.get("user_id");

            // 校验 JWT 中的用户 ID 与请求参数中的 id 是否一致（可选安全校验）
            if (!userIdFromJwt.equals(userId)) {
                return Result.error("权限验证失败");
            }
        } catch (Exception e) {
            log.error("JWT 验证失败：{}", e.getMessage());
            return Result.error("无效的 JWT 令牌");
        }

        Integer orderId = orderService.createOrder(userId, jwt);
        Map<String,Integer> data = Collections.singletonMap("orderId",orderId);
        return Result.success(data);
    }

    // POST /orders/{orderId}/items 添加商品到订单
    @PostMapping("/{orderId}/items")
    public Result addItemToOrder(@PathVariable("orderId") Integer orderId,@RequestBody OrderItem item) {
        item.setOrderId(orderId);
        Integer productId = item.getProductId();
        OrderItem result = orderService.addItemToOrder(productId, item);
        return Result.success(result);
    }

    // POST /orders/{orderId}/cancel 取消订单
    @PostMapping("/{orderId}/cancel")
    public Result cancelOrder(@PathVariable Integer orderId,
                              @RequestParam("userId") Integer userId,
                              @RequestHeader("Authorization") String Authorization) {
        try {
            Claims claims = JwtUtils.parseJWT(Authorization);
            Integer userIdFromJwt = (Integer) claims.get("user_id");

            // 校验 JWT 中的用户 ID 与请求参数中的 id 是否一致（可选安全校验）
            if (!userIdFromJwt.equals(userId)) {
                return Result.error("权限验证失败");
            }
        } catch (Exception e) {
            log.error("JWT 验证失败：{}", e.getMessage());
            return Result.error("无效的 JWT 令牌");
        }

        orderService.cancelOrder(orderId);
        return Result.success();
    }

    @PostMapping("/{orderId}/pay")
    public Result payOrder(@PathVariable("orderId") Integer orderId,
                           @RequestBody  Order order){
        Order order1 = orderService.getOrderDetails(orderId);
        if(order1==null){
            return Result.error("订单不存在");
        }

        // 处理支付
        orderService.payOrder(orderId, order.getPaymentMethod(),order.getActualAmount());

        return Result.success();

    }


    @PostMapping("/{orderId}/refund")
    public Result refundOrder(@PathVariable("orderId") Integer orderId,
                              @RequestParam("userId") Integer userId,
                              @RequestHeader("Authorization") String Authorization){
        if (userId == null) {
            return Result.error("用户ID不能为空");
        }

        try {
            Claims claims = JwtUtils.parseJWT(Authorization);
            Integer userIdFromJwt = (Integer) claims.get("user_id");

            // 校验 JWT 中的用户 ID 与请求参数中的 id 是否一致（可选安全校验）
            if (!userIdFromJwt.equals(userId)) {
                return Result.error("权限验证失败");
            }
        } catch (Exception e) {
            log.error("JWT 验证失败：{}", e.getMessage());
            return Result.error("无效的 JWT 令牌");
        }

        Order order1 = orderService.getOrderDetails(orderId);
        if(order1==null){
            return Result.error("订单不存在");
        }

        // 处理支付
        orderService.refundOrder(order1);

        return Result.success();
    }

    // GET /orders/{orderId}/query 查询订单
    @GetMapping("/{orderId}/query")
    public Result getOrder(@PathVariable Integer orderId,
                           @RequestParam("userId")Integer userId,
                           @RequestHeader("Authorization") String Authorization) {
        if (userId == null) {
            return Result.error("用户ID不能为空");
        }

        try {
            Claims claims = JwtUtils.parseJWT(Authorization);
            Integer userIdFromJwt = (Integer) claims.get("user_id");

            // 校验 JWT 中的用户 ID 与请求参数中的 id 是否一致（可选安全校验）
            if (!userIdFromJwt.equals(userId)) {
                return Result.error("权限验证失败");
            }
        } catch (Exception e) {
            log.error("JWT 验证失败：{}", e.getMessage());
            return Result.error("无效的 JWT 令牌");
        }

        Order order1 = orderService.getOrderDetails(orderId);
        if(order1==null){
            return Result.error("订单不存在");
        }



        return Result.success(orderService.getOrderDetails(orderId));
    }

    // PUT /orders/{orderId}/status 更改订单状态
    @PutMapping("/{orderId}/status")
    public Result updateOrderStatus(@PathVariable Integer orderId, @RequestBody Order order) {
        orderService.updateOrderStatus(orderId, order.getStatus());
        return Result.success();
    }
}






