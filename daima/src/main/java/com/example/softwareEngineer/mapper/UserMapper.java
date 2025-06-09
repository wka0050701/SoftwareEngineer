package com.example.softwareEngineer.mapper;

import com.example.softwareEngineer.DTO.User;
import org.apache.ibatis.annotations.*;

/**
 * 用户行为mapper层
 *   版本:1.0
 */
@Mapper
public interface UserMapper {

    //通过id查询用户信息
    @Select("SELECT phone, nickname, default_address as defaultAddress FROM users WHERE user_id = #{userId}")
    User getUserInfo(Integer userId);

    //更新用户信息
    @Update("UPDATE users SET nickname=#{nickname}, default_address=#{defaultAddress} WHERE user_id=#{userId}")
    int updateUserInfo(User user);
}
