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
    
    /**
     * 用户登录
     * @param phone
     * @param password
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestParam("phone") String phone, @RequestParam("password") String password) {
        log.info("用户登录");
        // 1. 参数校验
        // 验证手机号格式（11位数字）
        if (!Pattern.matches("^\\d{11}$", phone)) {
            return Result.error("手机号必须为11位数字");
        }
        
        // 验证密码非空
        if (password == null || password.trim().isEmpty()) {
            return Result.error("密码不能为空");
        }
        //获取用户id
        int userId=authService.login(phone,password);
        //返回数据
        if (userId != 0) {
            Map<String, Object> data = new HashMap<>();
            data.put("user_id", userId);
            return Result.success(data);
        }
        return Result.error("注册失败");
        
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
        //获取用户id
        int userId = authService.register(user);
        //返回数据
        if (userId != 0) {
            Map<String, Object> data = new HashMap<>();
            data.put("user_id", userId);
            return Result.success(data);
        } else {
            return Result.error("注册失败");
        }
    }
}
