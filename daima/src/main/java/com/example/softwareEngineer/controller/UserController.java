package com.example.softwareEngineer.controller;

import com.example.softwareEngineer.DTO.Result;
import com.example.softwareEngineer.DTO.User;
import com.example.softwareEngineer.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户控制层
 *   版本:1.0
 *   作者:wka
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
    @GetMapping("/{id}")
    public Result getUserInfo(@PathVariable("id") int id) {
        log.info("用户查询");
        User user = userService.getUserById(id);
        if (user != null) {
            Map<String, Object> data = new HashMap<>();
            data.put("phone", user.getPhone());
            data.put("password", user.getPassword());
            data.put("nickname", user.getNickname());
            data.put("default_address", user.getDefaultAddress());
            return Result.success(data);
        } else {
            return Result.error("用户未找到");
        }
    }
    
    /**
     * 用户更新信息
     * @param id
     * @param user
     * @return
     */
    @PutMapping("/{id}")
    public Result updateUser(@PathVariable int  id, @RequestBody User user) {
        log.info("用户更新信息");
        try {
            user.setUserId(id);
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