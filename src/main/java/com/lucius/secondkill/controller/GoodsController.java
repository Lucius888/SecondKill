package com.lucius.secondkill.controller;

import com.alibaba.druid.util.StringUtils;
import com.lucius.secondkill.entity.SkUser;
import com.lucius.secondkill.redis.GoodsKey;
import com.lucius.secondkill.result.Result;
import com.lucius.secondkill.service.GoodsService;
import com.lucius.secondkill.service.SkUserService;
import com.lucius.secondkill.util.RedisUtil;
import com.lucius.secondkill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    SkUserService skUserService;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    GoodsService goodsService;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    /**
     * 商品列表页面
     * QPS:433
     * 1000 * 10
     */
    @RequestMapping(value = "/to_list", produces = "text/html")
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response, Model model, SkUser user) {

        //取缓存
        String html = redisUtil.get(GoodsKey.getGoodsList, "", String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("user", user);
        model.addAttribute("goodsList", goodsList);
        //手动渲染
        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);


        if (!StringUtils.isEmpty(html)) {
            redisUtil.set(GoodsKey.getGoodsList, "", html);
        }
        //结果输出
        return html;
    }

//    /**
//     * 商品详情页面
//     */
//    @RequestMapping(value = "/detail/{goodsId}")
//    @ResponseBody
//    public Result<GoodsDetailVo> detail(HttpServletRequest request, HttpServletResponse response, Model model, User user, @PathVariable("goodsId") long goodsId) {
//
//        //根据id查询商品详情
//        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
//        model.addAttribute("goods", goods);
//
//        long startTime = goods.getStartDate().getTime();
//        long endTime = goods.getEndDate().getTime();
//        long now = System.currentTimeMillis();
//
//        int seckillStatus = 0;
//        int remainSeconds = 0;
//
//        if (now < startTime) {//秒杀还没开始，倒计时
//            seckillStatus = 0;
//            remainSeconds = (int) ((startTime - now) / 1000);
//        } else if (now > endTime) {//秒杀已经结束
//            seckillStatus = 2;
//            remainSeconds = -1;
//        } else {//秒杀进行中
//            seckillStatus = 1;
//            remainSeconds = 0;
//        }
//        GoodsDetailVo vo = new GoodsDetailVo();
//        vo.setGoods(goods);
//        vo.setUser(user);
//        vo.setRemainSeconds(remainSeconds);
//        vo.setSeckillStatus(seckillStatus);
//
//        return Result.success(vo);
//    }

//    /**
//     * 商品详情页面
//     */
//    @RequestMapping(value = "/to_detail2/{goodsId}", produces = "text/html")
//    @ResponseBody
//    public String detail2(HttpServletRequest request, HttpServletResponse response, Model model, User user, @PathVariable("goodsId") long goodsId) {
//        model.addAttribute("user", user);
//
//        //取缓存
//        String html = redisService.get(GoodsKey.getGoodsDetail, "" + goodsId, String.class);
//        if (!StringUtils.isEmpty(html)) {
//            return html;
//        }
//
//        //根据id查询商品详情
//        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
//        model.addAttribute("goods", goods);
//
//        long startTime = goods.getStartDate().getTime();
//        long endTime = goods.getEndDate().getTime();
//        long now = System.currentTimeMillis();
//
//        int seckillStatus = 0;
//        int remainSeconds = 0;
//
//        if (now < startTime) {//秒杀还没开始，倒计时
//            seckillStatus = 0;
//            remainSeconds = (int) ((startTime - now) / 1000);
//        } else if (now > endTime) {//秒杀已经结束
//            seckillStatus = 2;
//            remainSeconds = -1;
//        } else {//秒杀进行中
//            seckillStatus = 1;
//            remainSeconds = 0;
//        }
//        model.addAttribute("seckillStatus", seckillStatus);
//        model.addAttribute("remainSeconds", remainSeconds);
//
//        //手动渲染
//        SpringWebContext ctx = new SpringWebContext(request, response,
//                request.getServletContext(), request.getLocale(), model.asMap(), applicationContext);
//        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
//        if (!StringUtils.isEmpty(html)) {
//            redisService.set(GoodsKey.getGoodsDetail, "" + goodsId, html);
//        }
//        return html;
//    }
//
//    /**
//     * 商品详情页面
//     */
//    @RequestMapping(value = "/detail/{goodsId}")
//    @ResponseBody
//    public Result<GoodsDetailVo> detail(HttpServletRequest request, HttpServletResponse response, Model model, User user, @PathVariable("goodsId") long goodsId) {
//
//        //根据id查询商品详情
//        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
//        model.addAttribute("goods", goods);
//
//        long startTime = goods.getStartDate().getTime();
//        long endTime = goods.getEndDate().getTime();
//        long now = System.currentTimeMillis();
//
//        int seckillStatus = 0;
//        int remainSeconds = 0;
//
//        if (now < startTime) {//秒杀还没开始，倒计时
//            seckillStatus = 0;
//            remainSeconds = (int) ((startTime - now) / 1000);
//        } else if (now > endTime) {//秒杀已经结束
//            seckillStatus = 2;
//            remainSeconds = -1;
//        } else {//秒杀进行中
//            seckillStatus = 1;
//            remainSeconds = 0;
//        }
//        GoodsDetailVo vo = new GoodsDetailVo();
//        vo.setGoods(goods);
//        vo.setUser(user);
//        vo.setRemainSeconds(remainSeconds);
//        vo.setSeckillStatus(seckillStatus);
//
//        return Result.success(vo);
//    }
}
