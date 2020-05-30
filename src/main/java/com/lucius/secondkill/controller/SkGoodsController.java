/**
 * Copyright (C), Lucius
 * FileName: SkGoodsController
 * Author:
 * Date:     2020/5/28 18:05
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.lucius.secondkill.controller;


import com.lucius.secondkill.entity.SkGoods;
import com.lucius.secondkill.entity.SkUser;
import com.lucius.secondkill.service.SkGoodsService;
import com.lucius.secondkill.service.SkUserService;
import com.lucius.secondkill.util.RedisUtil;
import com.lucius.secondkill.util.ValidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class SkGoodsController {

    @Resource
    RedisUtil redisUtil;

    @Resource
    ValidUtil validUtil;

    @Autowired
    SkGoodsService skGoodsService;

    @Autowired
    SkUserService skUserService;

    /**
     * 商品列表页面,如果是页面缓存的话就是存的html源码文件
     * 是
     */
    @RequestMapping(value = "/goods/to_list", produces = "text/html")
    public String list(HttpServletRequest request, HttpServletResponse response, Model model) {

//        //取缓存,取得是html的源码
//        String html = redisUtil.get(GoodsKey.getGoodsList, "", String.class);
//        if (!StringUtils.isEmpty(html)) {
//            return html;
//        }
        List<SkGoods> goodsList = skGoodsService.listGoods();
        model.addAttribute("goodsList", goodsList);
        return "goods_list";


//        //手动渲染
//        SpringWebContext ctx = new SpringWebContext(request, response,
//                request.getServletContext(), request.getLocale(), model.asMap(), applicationContext);
//        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
//
//        if (!StringUtils.isEmpty(html)) {
//            redisService.set(GoodsKey.getGoodsList, "", html);
//        }
//        //结果输出
//        return html;
    }

    /**
     * 商品详情页面
     */
    @RequestMapping(value = "/goods/to_detail/{goodsId}")
    public String toDetail(Model model, SkUser user, @PathVariable("goodsId") long goodsId) {

        //根据id查询商品详情
        SkGoods goods = skGoodsService.getGoodsByGoodsId(goodsId);

        model.addAttribute("goods", goods);

        long startTime = goods.getStartDate().getTime();
        long endTime = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int seckillStatus = 0;
        int remainSeconds = 0;

        if (now < startTime) {//秒杀还没开始，倒计时
            seckillStatus = 0;
            remainSeconds = (int) ((startTime - now) / 1000);
        } else if (now > endTime) {//秒杀已经结束
            seckillStatus = 2;
            remainSeconds = -1;
        } else {//秒杀进行中
            seckillStatus = 1;
            remainSeconds = (int) ((endTime - now) / 1000);
        }
        model.addAttribute("remainSeconds", remainSeconds);
        model.addAttribute("seckillStatus", seckillStatus);

        return "goods_detail";
    }


//    /**
//     * 秒杀按钮跳转界面
//     */
//    @RequestMapping(value = "/seckill/do_seckill", method = RequestMethod.POST)
//    public String list(Model model, @RequestParam("goodsId") long goodsId HttpServletRequest request) {
//        //首先判断用户信息，失效就重新登录
//        if (!validUtil.isValid()) {
//            return "login";
//        }
//
//        SkGoods skGoods=skGoodsService.getGoodsByGoodsId(goodsId);
//        //判断商品库存，库存大于0，才进行操作，多线程下会出错
//        int  stockcount=skGoods.getStockCount();
//        if(stockcount<=0) {//失败			库存至临界值1的时候，此时刚好来了加入10个线程，那么库存就会-10
//            model.addAttribute("errorMessage", CodeMsg.SECKILL_OVER);
//            return "miaosha_fail";
//        }
//        //判断这个秒杀订单形成没有，判断是否已经秒杀到了，避免一个账户秒杀多个商品
//        MiaoshaOrder order=orderService.getMiaoshaOrderByUserIdAndGoodsId(user.getId(),goodsId);
//        if(order!=null) {//重复下单
//            model.addAttribute("errorMessage", CodeMsg.REPEATE_SECKILL);
//            return "miaosha_fail";
//        }
//        //可以秒杀，原子操作：1.库存减1，2.下订单，3.写入秒杀订单--->是一个事务
//        OrderInfo orderinfo=miaoshaService.miaosha(user,skGoods);
//        //如果秒杀成功，直接跳转到订单详情页上去。
//        model.addAttribute("orderinfo", orderinfo);
//        model.addAttribute("goods", skGoods);
//        return "order_detail";//返回页面login
//
//    }
}
