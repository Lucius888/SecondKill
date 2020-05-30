package com.lucius.secondkill;

import com.lucius.secondkill.entity.SkGoods;
import com.lucius.secondkill.entity.SkUser;
import com.lucius.secondkill.service.SkGoodsService;
import com.lucius.secondkill.service.SkUserService;
import com.lucius.secondkill.util.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SecondkillApplicationTests {


    @Autowired
    RedisUtil redisUtil;
    @Autowired
    SkUserService skUserService;
    @Autowired
    SkGoodsService skGoodsService;

    @Test

    public void test() {

        List<SkGoods> goodsList = skGoodsService.listGoods();
        System.out.println(goodsList.toString());
        SkUser skUser = skUserService.queryUserById(15972100306L);
        System.out.println(skUser.toString());
    }


}
