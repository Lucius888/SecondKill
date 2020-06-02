/**
 * Copyright (C), Lucius
 * FileName: SecKillController
 * Author:
 * Date:     2020/5/31 10:47
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.lucius.secondkill.controller;

import com.lucius.secondkill.entity.SkGoods;
import com.lucius.secondkill.entity.SkOrder;
import com.lucius.secondkill.entity.SkOrderInfo;
import com.lucius.secondkill.entity.SkUser;
import com.lucius.secondkill.redis.GoodsKey;
import com.lucius.secondkill.redis.OrderKey;
import com.lucius.secondkill.redis.RedisService;
import com.lucius.secondkill.result.CodeMsg;
import com.lucius.secondkill.service.SecKillService;
import com.lucius.secondkill.service.SkGoodsService;
import com.lucius.secondkill.service.SkOrderService;
import com.lucius.secondkill.service.SkUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.TemplateEngine;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class SecKillController implements InitializingBean {

    @Autowired
    TemplateEngine templateEngine;

    @Autowired
    SkGoodsService skGoodsService;

    @Autowired
    SkUserService skUserService;

    @Autowired
    SkOrderService skOrderService;

    @Autowired
    SecKillService secKillService;

    @Autowired
    RedisService redisService;

    /**初始版本
     * 秒杀按钮跳转界面
     */
//    @PostMapping(value = "/seckill/do_seckill")
//    @ResponseBody
//    public String list(Model model,
//                       HttpServletResponse response,
//                       @RequestParam("goodsId") long goodsId,
//                       HttpServletRequest request) {
//        //首先判断用户信息，失效就重新登录
//        SkUser user = (SkUser) request.getSession().getAttribute("User");
//        if (user == null) {
//            return "login";
//        }
//
//        //取页面缓存,取得是html的源码
//        String html = redisService.get(OrderKey.SeckillOrderByUidGidHtml, ""+"UID"+user.getId()+"&"+"GID"+goodsId, String.class);
//        //取到的话就直接返回
//        if (html != null) {
//            return html;
//        }
//
//        SkGoods skGoods = skGoodsService.getGoodsByGoodsId(goodsId);
//        //判断商品库存，库存大于0，才进行操作，多线程下会出错
//        int stockcount = skGoods.getStockCount();
//        if (stockcount <= 0) {//失败			库存至临界值1的时候，此时刚好来了加入10个线程，那么库存就会-10
//            model.addAttribute("errorMessage", CodeMsg.SECKILL_OVER);
//            return "seckill_fail";
//        }
//
//        //判断这个秒杀订单形成没有，判断是否已经秒杀到了，避免一个账户秒杀多个商品
//        SkOrder order = skOrderService.queryOrderByUserIdAndGoodsId(user.getId(), goodsId);
//        if (order != null) {//重复下单
//            model.addAttribute("errorMessage", CodeMsg.REPEATE_SECKILL);
//            return "seckill_fail";
//        }
//        //可以秒杀，原子操作：1.库存减1，2.下订单，3.写入秒杀订单--->是一个事务
//        SkOrderInfo orderinfo = secKillService.seckill(user, skGoods);
//        //如果秒杀成功，直接跳转到订单详情页上去。
//        model.addAttribute("orderinfo", orderinfo);
//        model.addAttribute("goods", skGoods);
//        //没有做支付业务，当生成订单的时候就当作是已经秒杀到了。
//
//        // 2.手动渲染 使用模板引擎 templateName:模板名称 String templateName="goods_detail";
//        WebContext context = new WebContext(request, response,
//                request.getServletContext(), request.getLocale(), model.asMap());
//        html = templateEngine.process("order_detail", context);
//
//        // 将渲染好的html保存至缓存
//        if (!StringUtils.isEmpty(html)) {
//            redisService.set(OrderKey.SeckillOrderByUidGidHtml, ""+"UID"+user.getId()+"&"+"GID"+goodsId, html);
//        }
//        return html;
//    }



    /**
     * 改进版本：添加rabbitmq
     * 秒杀按钮跳转界面
     */
    @PostMapping(value = "/seckill/do_seckill")
    public String list(Model model,
                       HttpServletResponse response,
                       @RequestParam("goodsId") long goodsId,
                       HttpServletRequest request) {
        //1.首先判断用户信息，失效就重新登录
        SkUser user = (SkUser) request.getSession().getAttribute("User");
        if (user == null) {
            return "login";
        }
        //2.预减少库存，减少redis里面的库存
        long stock=redisService.decr(GoodsKey.GoodsStock,""+goodsId);
        //3.判断减少数量1之后的stock，区别于查数据库时候的stock<=0
        if(stock<0) {
            //商品库存不足秒杀失败
            model.addAttribute("errorMessage", CodeMsg.SECKILL_OVER);
            return "seckill_fail";
        }

        SkGoods skGoods = skGoodsService.getGoodsByGoodsId(goodsId);
        //判断这个秒杀订单形成没有，判断是否已经秒杀到了，避免一个账户秒杀多个商品
        SkOrder order = skOrderService.queryOrderByUserIdAndGoodsId(user.getId(), goodsId);
        if (order != null) {//重复下单
            model.addAttribute("errorMessage", CodeMsg.REPEATE_SECKILL);
            return "seckill_fail";
        }
        //可以秒杀，原子操作：1.库存减1，2.下订单，3.写入秒杀订单--->是一个事务
        SkOrderInfo orderinfo = secKillService.seckill(user, skGoods);

        //如果秒杀成功，直接跳转到订单详情页上去。
        model.addAttribute("orderinfo", orderinfo);
        model.addAttribute("goods", skGoods);
        //没有做支付业务，当生成订单的时候就当作是已经秒杀到了。

        return "order_detail";
    }

    /**
     * 缓存与预热：
     * 在容器启动的时候，检测到了实现了接口InitializingBean之后，
     * 就会回调afterPropertiesSet方法。
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {

        List<SkGoods> goodslist=skGoodsService.listGoods();

        for(SkGoods skGoods:goodslist) {
            //如果不是null的时候，将库存加载到redis里面去 prefix---GoodsKey:gs ,	 key---商品id,	 value
            redisService.set(GoodsKey.GoodsStock, ""+skGoods.getId(), skGoods.getStockCount());
        }

    }

}
