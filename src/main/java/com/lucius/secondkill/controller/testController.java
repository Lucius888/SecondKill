/**
 * Copyright (C), Lucius
 * FileName: testController
 * Author:
 * Date:     2020/5/16 14:35
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.lucius.secondkill.controller;


import com.lucius.secondkill.entity.User;
import com.lucius.secondkill.redis.UserKey;
import com.lucius.secondkill.result.CodeMsg;
import com.lucius.secondkill.result.Result;
import com.lucius.secondkill.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test")
public class testController {

    @Autowired
    RedisUtil redisUtil;

    @RequestMapping("/hello")
    @ResponseBody
    public Result<String> hello() {//0代表成功
        return Result.success("hello sss");
    }
    @RequestMapping("/helloError")
    @ResponseBody
    public Result<String> helloError() {//0代表成功
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    /**
     *避免key被不同类的数据覆盖
     *使用Prefix前缀-->不同类别的缓存，用户、部门、
     */
    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<User> redisSet() {//0代表成功
        User user=new User(1,"1111");
        /*
        KeyPrefix prefix, String key, T value
        "UserKey:iduser"

        prefix就是前缀 ：id
        key 就是user
        value 就是值
        UserKey这个类名是在BaseKey中自动加上的
         */
        boolean f=redisUtil.set(UserKey.getById,"user",user);
        return Result.success(user);
    }
    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGetById() {//0代表成功
        User user=redisUtil.get(UserKey.getById,"user",User.class);
        //redisService.get("key1",String.class);
        System.out.println(user.toString());
        return Result.success(user);
    }

}
