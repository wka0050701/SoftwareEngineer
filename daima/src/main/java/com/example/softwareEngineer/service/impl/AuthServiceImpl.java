package com.example.softwareEngineer.service.impl;


import com.example.softwareEngineer.DTO.User;
import com.example.softwareEngineer.Exception.BusinessException;
import com.example.softwareEngineer.mapper.AuthMapper;
import com.example.softwareEngineer.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service实现类
 *   版本:1.0
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    public AuthMapper authmapper;
    
    /**
     * 登录函数
     */
    public int login(String encryptedPhone, String encryptedPassword) { // 参数改为加密后的字符串
        User user = authmapper.findByPhone(encryptedPhone); // 用加密后的手机号查询
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        log.info("查询到的用户ID: {}", user.getUserId()); // 新增日志
        if (!user.getPassword().equals(encryptedPassword)) { // 对比加密后的密码
            throw new RuntimeException("密码错误");
        }
        return user.getUserId();
    }
    
    /**
     * 注册函数
     * @param user
     * @return
     */
    @Override
    public int register(User user) {
        log.info("注册用户，默认地址：{}", user.getDefaultAddress()); // 调试日志
        // 插入前可加检查，比如手机号是否已注册
        User existing = authmapper.findByPhone(user.getPhone());
        if (existing != null) {
            throw new RuntimeException("该手机号已注册");
        }
        return authmapper.register(user);
    }

    @Override
    @Transactional
    public int resetPassword(String phone,String newPassword){
        return authmapper.resetPassword(phone,newPassword);
    }

    @Override
    @Transactional
    public boolean checkPhoneExist(String phone){
        return authmapper.checkPhoneExist(phone);
    }
}

