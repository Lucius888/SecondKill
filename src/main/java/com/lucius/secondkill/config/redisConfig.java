/**
 * Copyright (C), Lucius
 * FileName: redisConfig
 * Author:
 * Date:     2020/5/16 15:38
 * Description: 自定义的一个redisTemplate
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.lucius.secondkill.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


@Configuration
@AllArgsConstructor
@NoArgsConstructor
@Data
public class redisConfig {
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.timeout}")
    private int timeout;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.jedis.pool.max-active}")
    private int poolMaxTotal;
    @Value("${spring.redis.jedis.pool.max-idle}")
    private int poolMaxldle;
    @Value("${spring.redis.jedis.pool.max-wait}")
    private int poolMaxWait;


    /**
     * 将redis连接池注入spring容器
     *
     * @return
     */
    @Bean
    public JedisPool JedisPoolFactory() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(poolMaxldle);
        config.setMaxTotal(poolMaxTotal);
        //系统默认的是毫秒，配置文件中设置的是秒，在此进行一个转换
        config.setMaxWaitMillis(poolMaxWait * 1000);
        JedisPool jedisPool = new JedisPool(config, host, port, timeout * 1000, password, 0);
        return jedisPool;
    }


}
