package com.lucius.secondkill;

import com.lucius.secondkill.redis.RedisService;
import com.lucius.secondkill.redis.UserKey;
import com.lucius.secondkill.service.SkGoodsService;
import com.lucius.secondkill.service.SkUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SecondkillApplicationTests {


    @Autowired
    RedisService redisService;

    @Autowired
    SkUserService skUserService;
    @Autowired
    SkGoodsService skGoodsService;

    @Test

    public void test() {
        int str=123;
        //过期时间是在初始化实例的时候设置的
        //这个组成了redis中真正的key:UserKey:id+aaa
        redisService.set(UserKey.getById, "aaa", str);
        System.out.println(redisService.get(UserKey.getById,"aaa",Integer.class));
        redisService.decr(UserKey.getById,"aaa");
        System.out.println(redisService.get(UserKey.getById,"aaa",Integer.class));
        redisService.incr(UserKey.getById,"aaa");
        System.out.println(redisService.get(UserKey.getById,"aaa",Integer.class));
    }


}
