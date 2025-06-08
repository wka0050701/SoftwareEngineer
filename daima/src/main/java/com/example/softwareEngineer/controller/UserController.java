package com.example.softwareEngineer.controller;

import com.example.softwareEngineer.DTO.Result;
import com.example.softwareEngineer.DTO.User;
import com.example.softwareEngineer.service.UserService;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.softwareEngineer.utils.JwtUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 用户控制层
 *   版本:1.0
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    
    /**
     * 用户查询信息
     * @param id
     * @return
     */
    @GetMapping("/query")
    public Result getUserInfo(@RequestParam("id") int id,
                              @RequestHeader(value = "Authorization",required = false) String jwt) {
        log.info("用户查询");
        try {
            Claims claims = JwtUtils.parseJWT(jwt);
            Integer userIdFromJwt = (Integer) claims.get("user_id");

            // 校验 JWT 中的用户 ID 与请求参数中的 id 是否一致（可选安全校验）
            if (!userIdFromJwt.equals(id)) {
                return Result.error("权限验证失败");
            }
        } catch (Exception e) {
            log.error("JWT 验证失败：{}", e.getMessage());
            return Result.error("无效的 JWT 令牌");
        }

        User user = userService.getUserById(id);
        if (user != null) {
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("phone", user.getPhone());
            data.put("nickname", user.getNickname());
            data.put("default_address", user.getDefaultAddress());
            return Result.success(data);
        } else {
            return Result.error("用户未找到");
        }
    }
    
    /**
     * 用户更新信息
     */
    @PutMapping("/modify")
    public Result updateUser(@RequestBody User user) {
        log.info("用户更新信息");
        try {
            log.info("修改用户:{}",user);
            boolean isUpdated = userService.updateUser(user);
            if (isUpdated) {
                return Result.success();
            } else {
                return Result.error("更新失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}