package com.lucius.secondkill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//maxInactiveIntervalInSeconds: 设置Session失效时间，
//使用Redis Session之后，原Boot的server.session.timeout属性不再生效
//@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 86400*30)
@EnableCaching
public class SecondkillApplication {

    public static void main(String[] args) {

        SpringApplication.run(SecondkillApplication.class, args);
    }

}
