package com.example.softwareEngineer.service;

import com.example.softwareEngineer.DTO.User;

/**
 * 用户行为接口层
 *   版本:1.0
 */
public interface UserService {
    //id查询函数
    User getUserById(int id);
    //id更新用户内容
    boolean updateUser(User user);
    

}
