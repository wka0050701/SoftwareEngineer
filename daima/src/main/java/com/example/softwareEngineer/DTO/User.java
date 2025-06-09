package com.example.softwareEngineer.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * 用户类
 *   版本:1.0
 */
public class User {
   private int userId;//用户id
   private String phone;//用户手机号（账号)
   private String password;//密码
   private String nickname;//用户名
   private String defaultAddress;//默认地址
}
