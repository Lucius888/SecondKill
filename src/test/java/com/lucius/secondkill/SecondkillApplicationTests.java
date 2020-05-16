package com.lucius.secondkill;

import com.lucius.secondkill.entity.User;
import com.lucius.secondkill.redis.UserKey;
import com.lucius.secondkill.result.Result;
import com.lucius.secondkill.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SecondkillApplicationTests {


    @Autowired
    RedisUtil redisUtil;

    @Test
    public void test() {
        User user = new User(5, "555");
        boolean f = redisUtil.set(UserKey.getById, "5", user);
        User user1 = redisUtil.get(UserKey.getById, "5", User.class);
        System.out.println(user1.toString());
        System.out.println(Result.success(user1));

    }


}
