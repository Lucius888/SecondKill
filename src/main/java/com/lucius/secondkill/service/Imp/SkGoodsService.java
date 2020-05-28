package com.lucius.secondkill.service.Imp;


import com.lucius.secondkill.dao.SkGoodsDao;
import com.lucius.secondkill.entity.SkGoods;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by jiangyunxiong on 2018/5/22.
 */
@Service
public class SkGoodsService implements com.lucius.secondkill.service.SkGoodsService {

    //乐观锁冲突最大重试次数
    private static final int DEFAULT_MAX_RETRIES = 5;

    @Resource
    SkGoodsDao skGoodsDao;

    /**
     * 查询商品列表
     *
     * @return
     */
    @Override
    @Cacheable(value = "goods")
    public List<SkGoods> listGoodsVo() {
        return skGoodsDao.listGoodsVo();
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

}
