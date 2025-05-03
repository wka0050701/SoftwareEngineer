package com.example.softwareEngineer.service.impl;

import com.example.softwareEngineer.DTO.User;
import com.example.softwareEngineer.mapper.UserMapper;
import com.example.softwareEngineer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户行为Service层
 *   版本:1.0
 *   作者:wka
 */
@Service
public class UserServiceimpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    /**
     * 通过id查询用户
     * @param id
     * @return
     */
    @Override
    public User getUserById(int id) {
        return userMapper.findById(id);
    }
    
    /**
     * 更新用户信息
     * @param user
     * @return
     */
    @Override
    public boolean updateUser(User user) {
        // 验证手机号格式
        if (user.getPhone() == null || !user.getPhone().matches("^[0-9]{11}$")) {
            throw new IllegalArgumentException("手机号必须是11位数字");
        }
        return userMapper.updateUser(user);
    }
}
