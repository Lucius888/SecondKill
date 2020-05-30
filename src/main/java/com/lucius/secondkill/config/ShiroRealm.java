/**
 * Copyright (C), Lucius
 * FileName: ShiroRealm
 * Author:
 * Date:     2020/4/12 12:21
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.lucius.secondkill.config;

import com.lucius.secondkill.entity.SkUser;
import com.lucius.secondkill.service.SkUserService;
import com.lucius.secondkill.util.RedisUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Lazy;

import javax.annotation.Resource;


@EnableCaching
public class ShiroRealm extends AuthorizingRealm {

    @Resource
    RedisUtil redisUtil;


    /*
    这个@Lazy注解折磨了我两天！
    一直导致user无法正确缓存！
    https://stackoverflow.com/questions/21512791/spring-service-with-cacheable-methods-gets-initialized-without-cache-when-autowi
    https://segmentfault.com/q/1010000007100239
     */
    @Autowired
    @Lazy
    SkUserService skUserService;

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        return null;
    }

    /**
     * 认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 获取用户输入的用户名（用户名就是Id，手机号）
        // 获取用户输入的用户名和密码
        String userName = (String) token.getPrincipal();
        String password = new String((char[]) token.getCredentials());

        // 通过用户名到数据库查询用户信息
        SkUser skUser = skUserService.queryUserById(Long.parseLong(userName));
        //没有此人，返回null,自动显示异常
        if (skUser == null) {
            return null;
        }
            /*
            在这里对密码进行MD5盐加密
            盐值由用户名产生
            注意与HashedCredentialsMatcher中使用的算法和加密次数保持一致
            tohex也要与其保持一致类型
             */
        ByteSource salt = ByteSource.Util.bytes(skUser.getNickname());

//        String passwordSalt= PasswordUtil.shiroPasswordEncode(user.getLoginUserName(),user.getLoginPassword());
//        user.setLoginPassword(passwordSalt);
        //密码认证由shiro自动完成
        //此处的密码一定是数据库中的密码
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(skUser, skUser.getPassword(), salt, getName());
        return info;
    }
}

