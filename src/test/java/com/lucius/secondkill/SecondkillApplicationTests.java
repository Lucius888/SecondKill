package com.lucius.secondkill;

import com.lucius.secondkill.dao.SkUserDao;
import com.lucius.secondkill.entity.SkUser;
import com.lucius.secondkill.redis.UserKey;
import com.lucius.secondkill.util.RedisUtil;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class SecondkillApplicationTests {


    @Autowired
    RedisUtil redisUtil;
    @Resource
    SkUserDao skUserDao;

    @Test
    public void test() {
//        SkUser skUser=skUserDao.queryById(Long.parseLong("15972100306"));
//        System.out.println(skUser.toString());

        ByteSource salt = ByteSource.Util.bytes("lucius");
        String encodePassword = new SimpleHash("MD5", "luo123", salt, 1024).toHex();
        System.out.println(encodePassword);


    }



}
