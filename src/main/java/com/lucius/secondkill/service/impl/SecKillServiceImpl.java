/**
 * Copyright (C), Lucius
 * FileName: SecKillServiceImpl
 * Author:
 * Date:     2020/5/31 10:49
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.lucius.secondkill.service.impl;

import com.lucius.secondkill.entity.SkGoods;
import com.lucius.secondkill.entity.SkOrderInfo;
import com.lucius.secondkill.entity.SkUser;
import com.lucius.secondkill.service.SecKillService;
import com.lucius.secondkill.service.SkGoodsService;
import com.lucius.secondkill.service.SkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("secKillServiceImpl")
public class SecKillServiceImpl implements SecKillService {


    @Autowired
    SkGoodsService skGoodsService;


    @Autowired
    SkOrderService skOrderService;

    /**
     * 秒杀，原子操作：1.库存减1，2.下订单，3.写入秒杀订单--->是一个事务
     * 返回生成的订单
     */
    @Transactional
    @Override
    public SkOrderInfo seckill(SkUser user, SkGoods skGoods) {
        //1.减少库存,即更新库存
        //直接操作的数据库 还未用到缓存
        boolean success=skGoodsService.reduceStock(skGoods);//考虑减少库存失败的时候，不进行写入订单
        //生成订单
        SkOrderInfo skOrderInfo= skOrderService.createOrder(user, skGoods);
        return skOrderInfo;

//        if(success) {
//            //2.下订单,其中有两个订单: order_info   miaosha_order
//            SkOrderInfo orderinfo=orderService.createOrder_Cache(user, skGoods);
//            return orderinfo;
//        }else {//减少库存失败
//            //做一个标记，代表商品已经秒杀完了。
//            setGoodsOver(skGoods.getId());
//            return null;
//        }
    }
}
