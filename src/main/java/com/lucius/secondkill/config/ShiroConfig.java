/**
 * Copyright (C), Lucius
 * FileName: ShiroConfig
 * Author:
 * Date:     2020/4/12 12:15
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.lucius.secondkill.config;

import org.apache.log4j.Logger;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class ShiroConfig {
    private static Logger logger = Logger.getLogger(ShiroConfig.class);
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.timeout}")
    private int timeout;

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("defaultWebSecurityManager") DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);
        //设置未认证(登录)时，访问需要认证的资源时跳转的页面
        shiroFilterFactoryBean.setLoginUrl("/toLogin");
//        //设置认证成功时跳转的页面
        shiroFilterFactoryBean.setSuccessUrl("/index");
        //指定路径和过滤器的对应关系
        Map<String, String> filterMap = new LinkedHashMap<>();
        // 定义filterChain，静态资源不拦截
        // 配置不会被拦截的链接 顺序判断  相关静态资源
        filterMap.put("/static/**", "anon");
        filterMap.put("/css/**", "anon");
        filterMap.put("/font/**", "anon");
        filterMap.put("/images/**", "anon");
        filterMap.put("/js/**", "anon");
        // druid数据源监控页面不拦截
        filterMap.put("/druid/**", "anon");
        // 配置退出过滤器，其中具体的退出代码Shiro已经替我们实现了
//        filterMap.put("/admin/logout", "logout");

        filterMap.put("/index", "authc");
        filterMap.put("/goods/**", "authc");


        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        return shiroFilterFactoryBean;
    }


    @Bean
    //安全管理器配置
    public DefaultWebSecurityManager defaultWebSecurityManager(@Qualifier("shiroRealm") ShiroRealm shiroRealm, RedisCacheManager redisCacheManager, DefaultWebSessionManager redisSessionManager) {
        logger.info("--------------shiro已经加载----------------");
        // 配置SecurityManager，
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        // 注入shiroRealm
        defaultWebSecurityManager.setRealm(shiroRealm);
        defaultWebSecurityManager.setSessionManager(redisSessionManager);
        defaultWebSecurityManager.setCacheManager(redisCacheManager);

        return defaultWebSecurityManager;
    }

    /**
     * 配置shiro redisManager
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    @Bean
    public RedisManager redisManager() {
        logger.info("创建shiro redisManager,连接Redis..URL= " + host + ":" + port);
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host);
        redisManager.setPort(port);
        redisManager.setExpire(1800);// 配置缓存过期时间
        redisManager.setTimeout(timeout);
        redisManager.setPassword(password);
        return redisManager;
    }

    /**
     * cacheManager 缓存 redis实现
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    @Bean
    public RedisCacheManager redisCacheManager() {
        logger.info("创建RedisCacheManager...");
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis
     * 使用的是shiro-redis开源插件
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }

    /**
     * Session Manager
     * 使用的是shiro-redis开源插件
     */
    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO());
        // 删除过期的session
        sessionManager.setDeleteInvalidSessions(true);
        // 去掉URL中的JSESSIONID
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        // 是否定时检查session
        sessionManager.setSessionValidationSchedulerEnabled(true);
        return sessionManager;
    }


    @Bean
    @Lazy
    public ShiroRealm shiroRealm() {
        ShiroRealm shiroRealm = new ShiroRealm();

        //使用HashedCredentialsMatcher带加密的匹配器来替换原先明文密码匹配器
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //指定加密算法
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        //指定加密次数
        hashedCredentialsMatcher.setHashIterations(1024);
        // 生成16进制, 与注册时的生成格式相同
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);

        shiroRealm.setCredentialsMatcher(hashedCredentialsMatcher);

        return shiroRealm;
    }


}
