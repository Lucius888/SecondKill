[TOC]

================================================


<<<<<<< HEAD
## :art: 系统介绍

本系统是使用SpringBoot开发的高并发限时抢购秒杀系统。整体是在[秒杀系统](https://github.com/qiurunze123/miaosha)项目上进行优化



## :hammer: 开发环境

- JDK1.8
- Centos7
- Intellij IDEA



## :zap: 项目的亮点：

- 使用分布式Seesion，可以让多台服务器同时可以响应。
- 使用Redis做缓存提高访问速度和并发量，减少数据库压力，利用内存标记减少redis的访问。
- 使用RabbitMQ消息队列完成异步下单，提升用户体验，削峰和降流。
- 安全性优化：shiro安全框架，数学公式验证码，接口限流防刷。
- 页面缓存，加快用户访问速度



## :bookmark: 开发技术
=======
================================================

## 项目的亮点：
- 使用分布式Seesion，可以实现让多台服务器同时可以响应。
- 使用redis做缓存提高访问速度和并发量，减少数据库压力，利用内存标记减少redis的访问。
- 使用消息队列完成异步下单，提升用户体验，削峰和降流。
- 安全性优化：shiro安全框架，数学公式验证码，接口限流防刷。
- 页面缓存，加快用户访问速度

================================================
>>>>>>> d70b672764d0d8b86f76c40321188e1dfc13d078

前端技术 ：Bootstrap + jQuery + Thymeleaf

后端技术 ：SpringBoot 2.0+ MyBatis + MySQL

中间件技术 : Druid + Redis + RabbitMQ
<<<<<<< HEAD



## :bug: 关键开发过程记录

### Shiro安全框架做登录认证

原项目中使用了两次MD5手动加密进行登录管理，显然更加麻烦，在此使用shiro安全框架进行了优化。简单记录shiro身份验证的步骤。

Shiro的三大部件：

- ```java
    ShiroFilterFactoryBean//对系统资源的访问权限进行配置
    ```

- ```java
    DefaultWebSecurityManager//安全管理器，在其中要注册自己的Relam。
    ```

- ```java
    ShiroRealm //需要自己写的一个类。在里面根据需要重写认证和授权两个方法
    ```

只要完成以上三大部件的配置就能够实现身份认证功能

### Session共享

既然使用了shiro，那么必不可能再去像源码那样去共享session,太复杂！在说Session共享之前聊一聊**Cookie和Session**

#### Cookie
Cookie技术是将用户的数据存储到客户端的技术，它的作用是为了让服务端根据每个客户端持有的cookie来区分不同客户端。

Cookie由cookie name、具有唯一性的cookie value以及一些属性(path、expires、domain等)构成，其中value是区分客户端的唯一依据。

Cookie的原理为：服务端在接收到客户端首次发送的请求后，服务端在响应首部中加入"set-cookie"字段发送给客户端，其中包含了JSSIONID作为唯一身份识别ID；客户端在此后的请求中都将携带这一字段。服务端借此就可以识别客户端，并从cookie中找到该客户端的信息。

整个过程：

client—the 1st time—>sever，header 的cookie中无JSSIONID

server–response—>client, server发现请求头中无JSSIONID，好，我来给你产生一个随机值，返回给你，在response的header中，多了一个东西, Set-Cookie:JSESSIONID=xxx

client–reveive response from server，发现response header中有Set-Cookie，好，以后我再请求server时，把这些Cookie都带上，其中就包含了JSESSIONID

client–the 2nd time—>server, 带上了所有的cookie内容

#### Session
但是cookie是存在本地浏览中的，始终存在安全问题。而且cookie保存的字段大小和类型都被限制了，因此产生了session。

Session的原理：服务端接收到某客户端首次发送的请求后，为此客户端生成一个session，并分配一段属于该session的缓冲区，同时将该**session配对的标识号JSESSIONID作为cookie的name添加到响应首部**中返回给客户端；客户端下次访问时，请求首部中将携带该JSESSIONID，服务端将根据该JSESSIONID寻找与之配对的session，如果能找到对应的session，进行相应的操作。

#### Session共享的几种方法

- Session复制：将Session信息复制到其他的节点。但是信息难免会出现不同步的情况
- Session粘滞：将同一个ip请求都定向到同一个节点，这就相当于是单机了
- Session集中管理：将Session进行缓存，每个节点都从缓存中判断Session信息

#### Shiro-Redis实现Session共享

Shiro中本身就提供了sessionManager和sessionDAO，而shiro的缓存都是由CacheManager管理。因此我们只需要重写CacheManager，将其操作指向我们的redis，然后实现我们自己的CachingSessionDAO定制缓存操作和缓存持久化，就可以实现Session共享了。

### RabbitMQ异步下单

![image-20200607173106170](C:\Users\Lucius\AppData\Roaming\Typora\typora-user-images\image-20200607173106170.png)

**Exchange四种**

- direct exchange
    根据消息的 route key 来将消息分发到相对应的queue 中
- fanout exchange
    将消息分发给所有绑定到此 exchange 的 queue 中
- topic exchange
    根据 route key 将消息分发到与此消息的 route key 相匹配的并且绑定到此 exchagne 中的 queue 中
- header exchange
    不使用 route key 作为路由的依据, 而是使用消息头属性来路由消息.

本项目使用的简单direct exchange。

- 系统初始化，把商品库存数量stock加载到Redis上面来。
- 后端收到秒杀请求，Redis预减库存，如果库存已经到达临界值的时候，就不需要继续请求下去，直接返回失败，即后面的大量请求无需给系统带来压力。 判断这个秒杀订单形成没有，判断是否已经秒杀到了，避免一个账户秒杀多个商品，判断是否重复秒杀。
- 库存充足，且无重复秒杀，将秒杀请求封装后消息入队，同时给前端返回一个code (0)，即代表返回排队中。（返回的并不是失败或者成功，此时还不能判断）
- 前端接收到数据后，显示排队中，并根据商品id轮询请求服务器（考虑200ms轮询一次）。
- 后端RabbitMQ监听秒杀通道，如果有消息过来，获取到传入的信息，执行真正的秒杀之前，要判断数据库的库存，判断是否重复秒杀，然后执行秒杀事务（秒杀事务是一个原子操作：库存减1，下订单，写入秒杀订单）。
- 此时，前端根据商品id轮询秒杀结果,查看是否生成了商品订单，如果请求返回-1代表秒杀失败，返回0代表排队中，返回>0代表商品id说明秒杀成功。
### 图形验证码
为防止机器人，刷票软件恶意频繁点击按钮来刷请求秒杀地址接口。在点击秒杀之前，先输入验证码，分散用户的请求，只有当验证码正确之后才会获得秒杀地址。

**思路：**点击秒杀之前，先输入验证码，验证码正确才能够获取秒杀地址。具体实现是服务端生成验证码，把结果计算出来存至缓存，把验证码图片发至客户端，此后客户端在请求秒杀地址前输入验证码值发请求验证，（去缓存里面取得值验证是否与用户输入相同），验证通过才会获取到秒杀地址。

### 接口限流防刷
**思路：**利用缓存实现，用户每次点击之后访问接口的时候，在缓存中生成一个计数器，第一次将这个计数器置1后存入缓存，并给其设定有效期，比如一分钟，一分钟之内再访问，那么数值加一。一分钟之内访问次数超过限定数值，直接返回失败重新登录。下一个一分钟，数据重新从0开始计算。因为缓存具有一个有效期，一分钟之后自动失效。

### 页面缓存
将页面以html的形式存入redis,当用户刷新页面时直接从reids中取html代码，减少用户对数据库的访问。这里的页面缓存实际上是将数据和页面都存在redis里面了，当其中的数据有变化的时候就显得有问题了。因此只能够用于一些非关键的通用的页面，比如大家都可以访问的首页。
=======
>>>>>>> d70b672764d0d8b86f76c40321188e1dfc13d078

```java
//2.手动渲染 使用模板引擎 templateName:模板名称 String templateName="goods_detail";
WebContext context = new WebContext(request, response,
                request.getServletContext(), request.getLocale(), model.asMap());
html = templateEngine.process("order_detail", context);

<<<<<<< HEAD
```

可以进一步做页面静态化，将html源码存在客户端，然后使用ajax进行数据交互，每次请求都直接发送数据然后进行渲染。实现前后端分离。
=======
## 关键开发过程记录

### Shiro安全框架做登录认证
原项目中使用了两次MD5手动加密进行登录管理，显然更加麻烦，在此使用shiro安全框架进行了优化。

### Session共享
既然使用了shiro，那么必不可能再去像源码那样去共享session,太复杂！使用shiro-redis插件完成session共享

### RabbitMQ异步下单
- 系统初始化，把商品库存数量stock加载到Redis上面来。
- 后端收到秒杀请求，Redis预减库存，如果库存已经到达临界值的时候，就不需要继续请求下去，直接返回失败，即后面的大量请求无需给系统带来压力。 判断这个秒杀订单形成没有，判断是否已经秒杀到了，避免一个账户秒杀多个商品，判断是否重复秒杀。
- 库存充足，且无重复秒杀，将秒杀请求封装后消息入队，同时给前端返回一个code (0)，即代表返回排队中。（返回的并不是失败或者成功，此时还不能判断）
- 前端接收到数据后，显示排队中，并根据商品id轮询请求服务器（考虑200ms轮询一次）。
- 后端RabbitMQ监听秒杀通道，如果有消息过来，获取到传入的信息，执行真正的秒杀之前，要判断数据库的库存，判断是否重复秒杀，然后执行秒杀事务（秒杀事务是一个原子操作：库存减1，下订单，写入秒杀订单）。
- 此时，前端根据商品id轮询秒杀结果,查看是否生成了商品订单，如果请求返回-1代表秒杀失败，返回0代表排队中，返回>0代表商品id说明秒杀成功。
### 图形验证码
为防止机器人，刷票软件恶意频繁点击按钮来刷请求秒杀地址接口。在点击秒杀之前，先输入验证码，分散用户的请求，只有当验证码正确之后才会获得秒杀地址。

### 接口限流防刷
思路：利用缓存实现，用户每次点击之后访问接口的时候，在缓存中生成一个计数器，第一次将这个计数器置1后存入缓存，并给其设定有效期，比如一分钟，一分钟之内再访问，那么数值加一。一分钟之内访问次数超过限定数值，直接返回失败重新登录。下一个一分钟，数据重新从0开始计算。因为缓存具有一个有效期，一分钟之后自动失效。

### 页面缓存
将页面以html的形式存入redis,当用户刷新页面时直接从reids中取html代码，减少用户对数据库的访问。

>>>>>>> d70b672764d0d8b86f76c40321188e1dfc13d078
