package com.lucius.secondkill.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @Version: 1.0
 * @Desc:
 */
@Configuration
@EnableCaching
public class RedisCacheConfig extends CachingConfigurerSupport {
//    /**
//     * Spring Cache 自定义key生成器，缓存数据时key生成策略
//     *
//     * @return
//     */
//    @Bean
//    @Override
////    自定义的缓存key的生成策略
//    public KeyGenerator keyGenerator() {
//        return new KeyGenerator() {
//            @Override
//            public Object generate(Object target, Method method, Object... params) {
//                StringBuffer sb = new StringBuffer();
////                sb.append(target.getClass().getSimpleName());
////                sb.append('.');
//                sb.append(method.getName());
////                sb.append(':');
//                for (Object obj : params) {
//                    sb.append(obj.toString());
//                }
//                return sb.toString();
//            }
//        };
//    }

    //自定义缓存配置
    //duration 配置的是缓存过期时间，作为参数的原因是可以针对不同的缓存进行设置不同的缓存过期时间
    private RedisCacheConfiguration redisCacheConfiguration(Duration duration) {
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
//        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//
//        //解决查询缓存转换异常的问题
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(om);
//                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
        //获取默认配置

        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        // 配置序列化（解决乱码的问题）
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(duration)
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(genericJackson2JsonRedisSerializer))
                .disableCachingNullValues();
        return config;
    }

    //缓存管理器
    //可以在这个里面配置多个缓存 比如user和goods
    @Bean
    public RedisCacheManager cacheManager(LettuceConnectionFactory lettuceConnectionFactory) {
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
        //获取user默认配置
        RedisCacheConfiguration userRedisCacheConfiguration = redisCacheConfiguration(Duration.ofHours(1L));
        //获取goods默认配置
        RedisCacheConfiguration goodsRedisCacheConfiguration = redisCacheConfiguration(Duration.ofHours(2L));

        redisCacheConfigurationMap.put("goods", goodsRedisCacheConfiguration);
        redisCacheConfigurationMap.put("user", userRedisCacheConfiguration);

        //RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig();
        RedisCacheConfiguration defaultCacheConfig = redisCacheConfiguration(Duration.ofHours(1L));

        RedisCacheManager redisCacheManager = RedisCacheManager.builder(lettuceConnectionFactory)
                .cacheDefaults(defaultCacheConfig)
                .withInitialCacheConfigurations(redisCacheConfigurationMap)
                .build();

        return redisCacheManager;
    }

    //设置RedisTemplate序列化
    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);

        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        //String的序列化
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        GenericToStringSerializer genericToStringSerializer = new GenericToStringSerializer(Object.class);

        // key采用String的序列化方式
        redisTemplate.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        redisTemplate.setValueSerializer(genericToStringSerializer);
        // hash的value序列化方式采用jackson
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }
}
