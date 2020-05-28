package com.lucius.secondkill;

import com.lucius.secondkill.dao.SkUserDao;
import com.lucius.secondkill.util.RedisUtil;

import org.junit.Test;
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
        redisUtil.set("name", "lucius");
        System.out.println(redisUtil.get("name"));


    }


}
