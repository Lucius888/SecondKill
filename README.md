## 系统介绍
本系统是使用SpringBoot开发的高并发限时抢购秒杀系统
整体是在[秒杀系统](https://github.com/qiurunze123/miaosha)项目上进行优化


## 开发环境
JDK 1.8
CentOS 7
IntelliJ IDEA 

## 开发技术
前端技术 ：Bootstrap + jQuery + Thymeleaf

后端技术 ：SpringBoot 2.0+ MyBatis + MySQL

中间件技术 : Druid + Redis + RabbitMQ 

================================================

关键开发过程记录
## Shiro安全框架做登录认证
原项目中使用了两次MD5手动加密进行登录管理，显然更加麻烦，在此使用shiro安全框架进行了优化。

## session共享
既然使用了shiro，那么必不可能再去像源码那样去共享session,太复杂！使用shiro-redis插件完成session共享

## 页面静态化
将页面以html的形式存入redis,当用户刷新页面时直接从reids中取html代码，减少用户对数据库的访问。

## RabbitMQ异步下单
- 系统初始化，把商品库存数量stock加载到Redis上面来。
- 后端收到秒杀请求，Redis预减库存，如果库存已经到达临界值的时候，就不需要继续请求下去，直接返回失败，即后面的大量请求无需给系统带来压力。
判断这个秒杀订单形成没有，判断是否已经秒杀到了，避免一个账户秒杀多个商品，判断是否重复秒杀。
- 库存充足，且无重复秒杀，将秒杀请求封装后消息入队，同时给前端返回一个code (0)，即代表返回排队中。（返回的并不是失败或者成功，此时还不能判断）
- 前端接收到数据后，显示排队中，并根据商品id轮询请求服务器（考虑200ms轮询一次）。
- 后端RabbitMQ监听秒杀通道，如果有消息过来，获取到传入的信息，执行真正的秒杀之前，要判断数据库的库存，判断是否重复秒杀，然后执行秒杀事务（秒杀事务是一个原子操作：库存减1，下订单，写入秒杀订单）。
- 此时，前端根据商品id轮询秒杀结果,查看是否生成了商品订单，如果请求返回-1代表秒杀失败，返回0代表排队中，返回>0代表商品id说明秒杀成功。

## 图形验证码
为防止机器人，刷票软件恶意频繁点击按钮来刷请求秒杀地址接口。在点击秒杀之前，先输入验证码，分散用户的请求，只有当验证码正确之后才会获得秒杀地址。

## 接口限流防刷
思路：利用缓存实现，用户每次点击之后访问接口的时候，在缓存中生成一个计数器，第一次将这个计数器置1后存入缓存，并给其设定有效期，比如一分钟，一分钟之内再访问，那么数值加一。一分钟之内访问次数超过限定数值，直接返回失败重新登录。下一个一分钟，数据重新从0开始计算。因为缓存具有一个有效期，一分钟之后自动失效。

