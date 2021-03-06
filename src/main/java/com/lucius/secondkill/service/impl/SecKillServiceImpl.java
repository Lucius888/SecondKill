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
import com.lucius.secondkill.entity.SkOrder;
import com.lucius.secondkill.entity.SkOrderInfo;
import com.lucius.secondkill.entity.SkUser;
import com.lucius.secondkill.redis.RedisService;
import com.lucius.secondkill.redis.SeckillKey;
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

    @Autowired
    RedisService redisService;




    /**
     * 秒杀，原子操作：1.库存减1，2.下订单，3.写入秒杀订单--->是一个事务
     * 返回生成的订单
     */
    @Transactional
    @Override
    public SkOrderInfo seckill(SkUser user, SkGoods skGoods) {
        //1.减少库存,即更新库存
        boolean success=skGoodsService.reduceStock(skGoods);//考虑减少库存失败的时候，不进行写入订单
        //生成订单
        if(success) {
            //2.下订单,其中有两个订单: order_info   miaosha_order
            SkOrderInfo skOrderInfo= skOrderService.createOrder(user, skGoods);;
            return skOrderInfo;
        }else {
            //秒杀失败
            //做一个标记，代表商品已经秒杀完了。
            redisService.set(SeckillKey.isGoodsOver, ""+skGoods.getId(), true);
            return null;
        }
    }


    @Override
    //获取秒杀结果
    public long getSeckillResult(long userId, long goodsId){
        SkOrder order = skOrderService.queryOrderByUserIdAndGoodsId(userId, goodsId);
        //秒杀成功，返回订单id
        if (order != null){
            return order.getOrderId();
        }else{
            //秒杀失败，判断商品是否已经卖完了
            boolean isOver = redisService.hasKey(SeckillKey.isGoodsOver, ""+goodsId);
            if(isOver) {
                //卖完了
                return -1;
            }else {
                //没卖完
                return 0;
            }
        }
    }

}
