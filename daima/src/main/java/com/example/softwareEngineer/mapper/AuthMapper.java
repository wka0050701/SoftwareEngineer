package com.example.softwareEngineer.mapper;


import com.example.softwareEngineer.DTO.User;
import org.apache.ibatis.annotations.*;

/**
 * 登录注册mapper层
 *   版本:1.0
 */
@Mapper
public interface AuthMapper {
    //通过账号查询用户信息
    @Select("select user_id,phone,password,nickname,default_address from `software-sql`.users where " +
            "phone  = #{phone}")
    User findByPhone(String phone);

    //注册用户
    @Insert("insert into users(phone,password,nickname,default_address)" +
            "values (#{phone},#{password},#{nickname},#{defaultAddress})")
    @Options(useGeneratedKeys = true,keyProperty = "userId")
    int register(User user);

    //手机号查重
    @Select("select count(*) from users where phone = #{phone}")
    boolean checkPhoneExist(String phone);

    //用户登录
    @Select("SELECT user_id, phone, password, nickname, default_address as defaultAddress FROM users WHERE phone = #{phone}")
    User login(String phone);

    //找回密码
    @Update("UPDATE users SET password = #{newPassword} WHERE phone = #{phone}")
    int resetPassword(@Param("phone") String phone, @Param("newPassword") String newPassword);
}
