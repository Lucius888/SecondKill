/**
 * Copyright (C), Lucius
 * FileName: RedisService
 * Author:
 * Date:     2020/5/31 16:03
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.lucius.secondkill.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    @Autowired
    private RedisTemplate redisTemplate;

    //=============================common============================

    public static <T> String beanToString(T value) {
        if (value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class) {
            return String.valueOf(value);
        } else if (clazz == long.class || clazz == Long.class) {
            return String.valueOf(value);
        } else if (clazz == String.class) {
            return (String) value;
        } else {
            return JSON.toJSONString(value);
        }

    }

    public static <T> T stringToBean(String str, Class<T> clazz) {
        if (str == null || str.length() <= 0 || clazz == null) {
            return null;
        }
        if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(str);
        } else if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(str);
        } else if (clazz == String.class) {
            return (T) str;
        } else {
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效 失效时间为负数，说明该主键未设置失效时间（失效时间默认为-1）
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    //============================String=============================

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false 不存在
     */
    public boolean hasKey(KeyPrefix prefix, String key) {
        try {
            //生成真正的key
            String realKey = prefix.getPrefix() + key;
            return redisTemplate.hasKey(realKey);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 从redis连接池获取redis实例
     */
    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz) {
        //对key增加前缀，即可用于分类，也避免key重复
        String realKey = prefix.getPrefix() + key;
        if (realKey != null) {
            String str = (String) redisTemplate.opsForValue().get(realKey);
            T t = stringToBean(str, clazz);
            return t;
        } else {
            return null;
        }
    }

    /**
     * 存储对象
     */
    public <T> Boolean set(KeyPrefix prefix, String key, T value) {
        try {
            String str = beanToString(value);
            //真实key
            String realKey = prefix.getPrefix() + key;
            int seconds = prefix.expireSeconds();//获取过期时间
            if (seconds <= 0) {//永不过期
                redisTemplate.opsForValue().set(realKey, str);
            } else {
                redisTemplate.opsForValue().set(realKey, str, seconds);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     */
    public boolean delete(KeyPrefix prefix, String key) {

        String realKey = prefix.getPrefix() + key;
        return realKey == null ? null : redisTemplate.delete(realKey);

    }


    /**
     * 增加值
     * Redis Incr 命令将 key 中储存的数字值增一。
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作
     */
    public <T> Long incr(KeyPrefix prefix, String key) {
        String realKey = prefix.getPrefix() + key;
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate.opsForValue().increment(realKey);
    }

    /**
     * 减少值
     */
    public <T> Long decr(KeyPrefix prefix, String key) {

        String realKey = prefix.getPrefix() + key;
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate.opsForValue().decrement(realKey);
    }


}
