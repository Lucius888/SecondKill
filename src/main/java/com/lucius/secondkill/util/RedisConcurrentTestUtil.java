/**
 * Copyright (C), Lucius
 * FileName: RedisConcurrentTestUtil
 * Author:
 * Date:     2020/6/3 10:33
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.lucius.secondkill.util;

import com.lucius.secondkill.redis.GoodsKey;
import com.lucius.secondkill.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedisConcurrentTestUtil {
    @Autowired
    RedisService redisService;	//会出现循环依赖---Circular reference

    class ThreadTest implements Runnable{
        @Override
        public void run() {
            long stock= redisService.get(GoodsKey.gs1,"",long.class);
            String name=Thread.currentThread().getName();
            System.out.println("当前线程 ："+name+"  stock："+stock);
            //2.预减少库存，减少redis里面的库存
            //stock最初为10，100个线程同时去减少1次，最终stock应该为-90
            stock=redisService.decr(GoodsKey.gs1,"");
            //是否线程安全？
            if(stock<0) {
                System.out.println("结束!!!");
                return;
            }
            //应该只有10个线程能从这里通过
            System.out.println("验证当前有几个线程通过if(stock<0)  当前线程 ："+name+"  减1之后的stock："+stock);
        }
    }
    public  void test(){
        ThreadTest t1=new ThreadTest();
        //开启100个线程
        for(int i=1;i<=100;i++){
            new Thread(t1,"Thread-"+i).start();
        }
    }

}

