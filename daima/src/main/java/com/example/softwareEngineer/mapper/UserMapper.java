package com.example.softwareEngineer.mapper;

import com.example.softwareEngineer.DTO.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户行为mapper层
 *   版本:1.0
 *   作者:wka
 */
@Mapper
public interface UserMapper {
    //通过id查询用户信息
    User findById(int id);
    //更新用户信息
    boolean updateUser(User user);
}
