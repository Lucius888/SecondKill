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
import com.lucius.secondkill.result.CodeMsg;
import com.lucius.secondkill.service.SecKillService;
import com.lucius.secondkill.service.SkGoodsService;
import com.lucius.secondkill.service.SkOrderService;
import com.lucius.secondkill.service.SkUserService;
import com.lucius.secondkill.util.RedisUtil;
import com.lucius.secondkill.util.ValidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class SecKillController {



    @Autowired
    SkGoodsService skGoodsService;

    @Autowired
    SkUserService skUserService;

    @Autowired
    SkOrderService skOrderService;

    @Autowired
    SecKillService secKillService;

    /**
     * 秒杀按钮跳转界面
     */
    @PostMapping(value = "/seckill/do_seckill")
    public String list(Model model,
                       @RequestParam("goodsId") long goodsId ,
                       HttpServletRequest request) {
        //首先判断用户信息，失效就重新登录
        SkUser user=(SkUser) request.getSession().getAttribute("User");
        if (user==null) {
            return "login";
        }

        SkGoods skGoods=skGoodsService.getGoodsByGoodsId(goodsId);
        //判断商品库存，库存大于0，才进行操作，多线程下会出错
        int  stockcount=skGoods.getStockCount();
        if(stockcount<=0) {//失败			库存至临界值1的时候，此时刚好来了加入10个线程，那么库存就会-10
            model.addAttribute("errorMessage", CodeMsg.SECKILL_OVER);
            return "seckill_fail";
        }

        //判断这个秒杀订单形成没有，判断是否已经秒杀到了，避免一个账户秒杀多个商品
        SkOrder order=skOrderService.queryOrderByUserIdAndGoodsId(user.getId(),goodsId);
        if(order!=null) {//重复下单
            model.addAttribute("errorMessage", CodeMsg.REPEATE_SECKILL);
            return "seckill_fail";
        }
        //可以秒杀，原子操作：1.库存减1，2.下订单，3.写入秒杀订单--->是一个事务
        SkOrderInfo orderinfo=secKillService.seckill(user,skGoods);
        //如果秒杀成功，直接跳转到订单详情页上去。
        model.addAttribute("orderinfo", orderinfo);
        model.addAttribute("goods", skGoods);
        //没有做支付业务，当生成订单的时候就当作是已经秒杀到了。
        return "order_detail";
    }
}
