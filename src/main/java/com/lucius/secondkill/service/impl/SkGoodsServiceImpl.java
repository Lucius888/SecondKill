package com.lucius.secondkill.service.impl;


import com.lucius.secondkill.config.ShiroConfig;
import com.lucius.secondkill.dao.SkGoodsDao;
import com.lucius.secondkill.entity.SkGoods;
import com.lucius.secondkill.redis.GoodsKey;
import com.lucius.secondkill.redis.RedisService;
import com.lucius.secondkill.service.SkGoodsService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service("skGoodsService")
public class SkGoodsServiceImpl implements SkGoodsService {

    private static Logger logger = Logger.getLogger(SkGoodsServiceImpl.class);

    @Resource
    SkGoodsDao skGoodsDao;
    @Resource
    RedisService redisService;

    /**
     * 查询商品列表
     *
     * @return
     */
    @Override
    public List<SkGoods> listGoods() {
        //先去查缓存
        List<SkGoods> goodsList = redisService.get(GoodsKey.GoodsList, "", List.class);
        if (goodsList == null) {
            logger.info("没有GoodsList缓存");
            //缓存查不到，查数据库
            goodsList = skGoodsDao.listGoods();
            //存入缓存
            redisService.set(GoodsKey.GoodsList, "", goodsList);
        }else{
            logger.info("我取的是GoodsList缓存");
        }
        return goodsList;
    }

    /**
     * 根据id查询商品
     *
     * @return
     */
    @Override
    public SkGoods getGoodsByGoodsId(long goodsId) {
        SkGoods goods = redisService.get(GoodsKey.GoodsDetail, "" + goodsId, SkGoods.class);
        if (goods == null) {
            goods = skGoodsDao.queryGoodsById(goodsId);
            redisService.set(GoodsKey.GoodsDetail, "" + goodsId, goods);
        }
        logger.info("我取得是Goods缓存");
        return goods;
    }

    @Override
    public boolean reduceStock(SkGoods skGoods) {
        SkGoods sg = new SkGoods();
        sg.setId(skGoods.getId());
        //goods.setStockCount(goodsvo.getGoodsStock()-1);  sql里面去运算
        //goodsDao.reduceStock(goods.getGoodsId());
        int ret = skGoodsDao.reduceStock(skGoods);
        System.out.println("reduceStock1:" + ret);
        return ret > 0;
        //return true;
    }

}
