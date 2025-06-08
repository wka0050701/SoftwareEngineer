package com.example.softwareEngineer.controller;


import com.example.softwareEngineer.DTO.Result;
import com.example.softwareEngineer.DTO.User;
import com.example.softwareEngineer.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 注册登录层
 * 版本:1.0
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController  {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        log.info("用户登录");
        // 1. 参数校验
        // 验证手机号格式（11位数字）
        Map<String, Object> data = new HashMap<>();
        data.put("userId",1);
        data.put("jwt","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNzM1Njg5NjAwLCJleHAiOjE3MzU3NzYwMDB9.7vfJz7Z6w7z6Jz6w7z6Jz6w7z6Jz6w7z6Jz6w7z6Jz6");
        return Result.success(data);
    }
    
    /**
     * 用户注册
     * @param user
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        log.info("用户注册");
        // 校验手机号11位
        if (!Pattern.matches("^\\d{11}$", user.getPhone())) {
            return Result.error("手机号必须为11位数字");
        }
        
        // 校验密码
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return Result.error("密码不能为空");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("userId", 1);
        data.put("jwt","jwt令牌");
        return Result.success(1);
    }
}
