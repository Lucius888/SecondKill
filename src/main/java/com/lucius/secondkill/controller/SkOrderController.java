package com.lucius.secondkill.controller;

import com.lucius.secondkill.entity.SkGoods;
import com.lucius.secondkill.entity.SkOrder;
import com.lucius.secondkill.entity.SkOrderInfo;
import com.lucius.secondkill.entity.SkUser;
import com.lucius.secondkill.service.SkGoodsService;
import com.lucius.secondkill.service.SkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * (SkOrder)表控制层
 *
 * @author makejava
 * @since 2020-05-29 20:44:52
 */
@Controller
public class SkOrderController {

    @Autowired
    SkOrderService skOrderService;

    @Autowired
    SkGoodsService skGoodsService;

    @GetMapping(value = "/seckill/order/{orderId}")
    public String seckillOrderDetail(Model model,
                                     HttpServletRequest request,
                                     @PathVariable("orderId")long orderId){
        SkUser user = (SkUser) request.getSession().getAttribute("User");
        if (user == null) {
            return "login";
        }
        SkOrderInfo skOrderInfo=skOrderService.queryOrderinfoByorderId(orderId);
        SkGoods skGoods=skGoodsService.getGoodsByGoodsId(skOrderInfo.getGoodsId());
        model.addAttribute("goods",skGoods);
        model.addAttribute("orderinfo",skOrderInfo);
        return "order_detail";
    }

}