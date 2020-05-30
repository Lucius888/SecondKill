package com.lucius.secondkill.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.io.Serializable;

/**
 * (SkUser)实体类
 *
 * @author makejava
 * @since 2020-05-30 10:18:08
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SkUser implements Serializable {
    /**
    * 用户id
    */
    private Long id;
    /**
    * 昵称
    */
    private String nickname;
    /**
    * MD5(MD5(pass明文+固定salt)+salt
    */
    private String password;

    /**
    * 注册时间
    */
    private Date registerDate;
    /**
    * 上次登录时间
    */
    private Date lastLoginDate;
    /**
    * 登录次数
    */
    private Integer loginCount;



}