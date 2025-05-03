package com.example.softwareEngineer.mapper;


import com.example.softwareEngineer.DTO.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 登录注册mapper层
 *   版本:1.0
 *   作者:wka
 */
@Mapper
public interface AuthMapper {
    //通过账号查询用户信息
    User findByPhone(String phone);
    //注册用户
    void insertUser(User user);
}
