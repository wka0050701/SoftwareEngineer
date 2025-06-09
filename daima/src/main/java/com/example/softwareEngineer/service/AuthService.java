package com.example.softwareEngineer.service;

import com.example.softwareEngineer.DTO.Result;
import com.example.softwareEngineer.DTO.User;

/**
 * 登录注册接口层
 *   版本:1.0
 */
public interface AuthService {
    //登录函数
    int login(String phone, String password);
    //注册函数
    int register(User user);
    //找回密码
    int resetPassword(String phone, String password);
    //手机号查重
    boolean checkPhoneExist(String phone);

}
