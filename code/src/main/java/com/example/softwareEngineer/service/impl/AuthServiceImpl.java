package com.example.softwareEngineer.service.impl;


import com.example.softwareEngineer.DTO.User;
import com.example.softwareEngineer.mapper.AuthMapper;
import com.example.softwareEngineer.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service实现类
 *   版本:1.0
 */
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    public AuthMapper authmapper;
    
    /**
     * 登录函数
     * @param phone
     * @param password
     * @return
     */
    public int login(String phone, String password){
        User user = authmapper.findByPhone(phone);
        return  user.getUserId();
    }
    
    /**
     * 注册函数
     * @param user
     * @return
     */
    @Override
    public int register(User user) {
        // 插入前可加检查，比如手机号是否已注册
        User existing = authmapper.findByPhone(user.getPhone());
        if (existing != null) {
            throw new RuntimeException("该手机号已注册");
        }
        
        authmapper.insertUser(user);
        return user.getUserId();
    }
}

