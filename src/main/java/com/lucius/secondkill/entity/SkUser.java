package com.lucius.secondkill.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.io.Serializable;

/**
 * (SkUser)实体类
 *
 * @author makejava
 * @since 2020-05-18 13:50:32
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
//@JsonIgnoreProperties(value = {"handler"})
public class SkUser implements Serializable {
    /**
    * 用户id
     * 此处的用户ID就是手机号，登录的用户名
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
    * 混淆盐
    */
    private String salt;
    /**
    * 头像，云存储的ID
    */
    private String head;
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