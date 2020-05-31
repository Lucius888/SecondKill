package com.lucius.secondkill.service.impl;


import com.lucius.secondkill.dao.SkGoodsDao;
import com.lucius.secondkill.entity.SkGoods;
import com.lucius.secondkill.service.SkGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service("skGoodsService")
public class SkGoodsServiceImpl implements SkGoodsService {


    @Resource
    SkGoodsDao skGoodsDao;

    /**
     * 查询商品列表
     *
     * @return
     */
    @Override
    @Cacheable(value = "goods")
    public List<SkGoods> listGoods() {
        return skGoodsDao.listGoods();
    }

    /**
     * 根据id查询商品
     *
     * @return
     */
    @Override
    public SkGoods getGoodsByGoodsId(long goodsId) {
        return skGoodsDao.queryGoodsById(goodsId);
    }

    @Override
    public boolean reduceStock(SkGoods skGoods) {
        SkGoods sg=new SkGoods();
        sg.setId(skGoods.getId());
        //goods.setStockCount(goodsvo.getGoodsStock()-1);  sql里面去运算
        //goodsDao.reduceStock(goods.getGoodsId());
        int ret=skGoodsDao.reduceStock(skGoods);
        System.out.println("reduceStock1:"+ret);
        return ret>0;
        //return true;
    }

}
