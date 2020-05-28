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
import com.lucius.secondkill.result.Result;
import com.lucius.secondkill.service.Imp.SkGoodsService;
import com.lucius.secondkill.util.RedisUtil;
import com.lucius.secondkill.vo.GoodsDetailVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class SkGoodsController {

    @Resource
    RedisUtil redisUtil;

    @Resource
    SkGoodsService skGoodsService;

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
        List<SkGoods> goodsList = skGoodsService.listGoodsVo();
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
}
