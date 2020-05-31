/**
 * Copyright (C), Lucius
 * FileName: ValidUtil
 * Author:
 * Date:     2020/5/29 19:32
 * Description: 判断该用户登录信息是否有效
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.lucius.secondkill.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidUtil {

//    @Autowired
//    RedisUtil redisUtil;
//
//    public Boolean isValid() {
//        //登录成功后要对session做分布式管理
//        // 获取当前Subject对象
//        Subject subject = SecurityUtils.getSubject();
//        //获取绑定在当前subjuct的session
//        Session session = subject.getSession();
//
//        if (redisUtil.hasKey("shiro_redis_session:" + session.getId())) {
//            //redis有此人登录信息,正常浏览
//            return true;
//        } else {
//            return false;
//        }
//
//    }


}
