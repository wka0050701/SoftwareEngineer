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
            throw new BusinessException("密码错误");
        }
        return user.getUserId();
    }
    
    /**
     * 注册函数
     */
    @Override
    public int register(User user) {
        log.info("注册用户，默认地址：{}", user.getDefaultAddress());

        User user1 = authmapper.findByPhone(user.getPhone());

        if (user1 != null) {
            throw new BusinessException("该手机号已注册");
        }

        int rowsAffected = authmapper.register(user);
        if (rowsAffected > 0) {
            // 从入参对象中获取生成的主键
            return user.getUserId(); // ✅ 正确返回生成的主键
        }
        throw new BusinessException("注册失败，数据库操作异常");
    }

    @Override
    @Transactional
    public void resetPassword(String phone, String newPassword){
// 1. 检查用户是否存在
        User user = authmapper.findByPhone(phone);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 2. 检查新旧密码是否相同（虽然Controller已检查，这里做二次验证）
        if (user.getPassword().equals(newPassword)) {
            throw new BusinessException("新密码不能与原密码相同");
        }

        // 3. 执行更新
        int affectedRows = authmapper.resetPassword(phone, newPassword);
        if (affectedRows == 0) {
            throw new BusinessException("密码更新失败");
        }

        log.info("用户ID {} 密码重置成功", user.getUserId());
    }

    @Override
    public boolean isPhoneRegistered(String phone) {
        User user = authmapper.findByPhone(phone);
        return user != null;
    }

    @Override
    public User findByPhone(String encryptedPhone) {
        return authmapper.findByPhone(encryptedPhone);
    }
}

