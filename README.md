## 系统介绍
本系统是使用SpringBoot开发的高并发限时抢购秒杀系统
整体是在[秒杀系统](https://github.com/qiurunze123/miaosha)项目上进行优化


## 开发环境
JDK 1.8
CentOS 7
IntelliJ IDEA 

## 压测工具
JMeter


## 开发技术
前端技术 ：Bootstrap + jQuery + Thymeleaf

后端技术 ：SpringBoot 2.0+ MyBatis + MySQL

中间件技术 : Druid + Redis + RabbitMQ + Guava

================================================

关键开发过程记录
## Shiro安全框架做登录认证
原项目中使用了两次MD5手动加密进行登录管理，显然更加麻烦，在此使用shiro安全框架进行了优化。

## session共享
既然使用了shiro，那么必不可能再去像源码那样去共享session,太复杂！
