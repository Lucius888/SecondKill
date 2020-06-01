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
import com.lucius.secondkill.redis.GoodsKey;
import com.lucius.secondkill.redis.RedisService;
import com.lucius.secondkill.service.SkGoodsService;
import com.lucius.secondkill.service.SkOrderService;
import com.lucius.secondkill.service.SkUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class SkGoodsController {

    @Autowired
    TemplateEngine templateEngine;

    @Autowired
    SkGoodsService skGoodsService;

    @Autowired
    SkUserService skUserService;

    @Autowired
    SkOrderService skOrderService;

    @Autowired
    RedisService redisService;

    /**
     * 商品列表页面,如果是页面缓存的话就是存的html源码文件
     * 是
     */
    @RequestMapping(value = "/goods/to_list", produces = "text/html")
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response, Model model) {

        //取页面缓存,取得是html的源码
        String html = redisService.get(GoodsKey.GoodsListHtml, "", String.class);
        //取到的话就直接返回
        if (html != null) {
            return html;
        }
        //取商品缓存
        List<SkGoods> goodsList=skGoodsService.listGoods();

        model.addAttribute("goodsList", goodsList);

        WebContext context = new WebContext(request, response,
                request.getServletContext(), request.getLocale(), model.asMap());
        html = templateEngine.process("goods_list", context);
        if (html != null) {
            redisService.set(GoodsKey.GoodsListHtml, "", html);
        }
        return html;
    }

    /**
     * 商品详情页面
     * 未做页面缓存
     */
    @RequestMapping(value = "/goods/to_detail/{goodsId}")
    public String toDetail(Model model, @PathVariable("goodsId") long goodsId) {

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

    /**
     * 做了页面缓存的to_detail商品详情页。
     * @param model
     * @param goodsId
     * @return
     */
    @RequestMapping(value="/goods/to_detail_html/{goodsId}",produces = "text/html")  //produces="text/html"
    @ResponseBody
    public String toDetailCachehtml(Model model,
                                    HttpServletRequest request,
                                    HttpServletResponse response,
                                    @PathVariable("goodsId")long goodsId) {
        // 1.取缓存
        // key中包含了商品id用于区分
        String html = redisService.get(GoodsKey.GoodsDetail, ""+goodsId, String.class);//不同商品页面不同的详情
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        //缓存中没有，则将业务数据取出，放到缓存中去。
        SkGoods goods=skGoodsService.getGoodsByGoodsId(goodsId);

        model.addAttribute("goods", goods);

        //既然是秒杀，还要传入秒杀开始时间，结束时间等信息
        long startTime = goods.getStartDate().getTime();
        long endTime = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int seckillStatus = 0;
        int remainSeconds = 0;

        //秒杀还没开始，倒计时
        if (now < startTime) {
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

        // 2.手动渲染 使用模板引擎 templateName:模板名称 String templateName="goods_detail";
        WebContext context = new WebContext(request, response,
                request.getServletContext(), request.getLocale(), model.asMap());
        html = templateEngine.process("goods_detail", context);

        // 将渲染好的html保存至缓存
        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.GoodsDetailHtml, ""+goodsId, html);
        }
        return html;//html是已经渲染好的html文件
    }
}
