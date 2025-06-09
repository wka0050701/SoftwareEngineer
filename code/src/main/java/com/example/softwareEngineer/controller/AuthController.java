package com.example.softwareEngineer.controller;


import com.example.softwareEngineer.DTO.Result;
import com.example.softwareEngineer.DTO.User;
import com.example.softwareEngineer.Exception.BusinessException;
import com.example.softwareEngineer.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.softwareEngineer.utils.JwtUtils;
import com.example.softwareEngineer.utils.MD5Utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
     */
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        log.info("用户登录");
        // 1. 参数校验
        // 验证手机号格式（11位数字）

        if (!Pattern.matches("^\\d{11}$", user.getPhone())) {
            return Result.error("手机号必须为11位数字");
        }

        // 验证密码非空
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return Result.error("密码不能为空");
        }

        String encryptedPhone = MD5Utils.encrypt(user.getPhone());
        String encryptedPassword = MD5Utils.encrypt(user.getPassword());

        log.info("登录密码原始值: {}", user.getPhone());
        log.info("登录密码加密后: {}", encryptedPassword);
        //获取用户id
        int userId=authService.login(encryptedPhone,encryptedPassword);
        //返回数据
        if (userId != 0) {
            // 生成 JWT（保持不变）
            String jwt = JwtUtils.generateJwt(Collections.singletonMap("user_id", userId));

            // 构建嵌套的数据结构
            Map<String, Object> loginData = new LinkedHashMap<>();
            loginData.put("userId", userId);
            loginData.put("jwt", jwt);

            // 将嵌套结构放入 Result 的 data 字段
            return Result.success(loginData);
        }
        return Result.error("认证失败");
    }
    
    /**
     * 用户注册
     * @param user
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        log.info("用户注册");

        if (authService.isPhoneRegistered(user.getPhone())) {
            return Result.error("该手机号已注册");
        }

        String encryptedPhone = MD5Utils.encrypt(user.getPhone());
        String encryptedPassword = MD5Utils.encrypt(user.getPassword());

        // 校验手机号11位
        if (!Pattern.matches("^\\d{11}$", user.getPhone())) {
            return Result.error("手机号必须为11位数字");
        }
        // 校验密码
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return Result.error("密码不能为空");
        }

        user.setPhone(encryptedPhone);
        user.setPassword(encryptedPassword);

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

    @PostMapping("/password")
    public Result findPassword(@RequestBody User user) {

        // 1. 参数校验
        // 验证手机号格式（11位数字）
        if (!Pattern.matches("^\\d{11}$", user.getPhone())) {
            return Result.error("手机号必须为11位数字");
        }

        // 验证新密码非空
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return Result.error("新密码不能为空");
        }

        // 加密处理
        String encryptedPhone = MD5Utils.encrypt(user.getPhone());
        String encryptedNewPassword = MD5Utils.encrypt(user.getPassword());

        // 2. 检查新旧密码是否相同
        try {
            User existingUser = authService.findByPhone(encryptedPhone);
            if (existingUser != null && existingUser.getPassword().equals(encryptedNewPassword)) {
                return Result.error("新密码不能与原密码相同");
            }

            // 3. 执行密码重置
            authService.resetPassword(encryptedPhone, encryptedNewPassword);
            return Result.success();
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }
}
