/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Lucius
 * @create 2020/5/28
 * @since 1.0.0
 */


package com.lucius.secondkill.service;

import com.lucius.secondkill.entity.SkGoods;

import java.util.List;

public interface SkGoodsService {


    //获取商品列表
    List<SkGoods> listGoods();


    //根据商品Id获取商品
    SkGoods getGoodsByGoodsId(long goodsId);
}
