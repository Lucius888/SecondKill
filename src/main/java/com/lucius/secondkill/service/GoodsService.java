package com.lucius.secondkill.service;


import com.lucius.secondkill.dao.SkGoodsDao;
import com.lucius.secondkill.entity.SkGoods;
import com.lucius.secondkill.vo.GoodsVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by jiangyunxiong on 2018/5/22.
 */
@Service
public class GoodsService {

    //乐观锁冲突最大重试次数
    private static final int DEFAULT_MAX_RETRIES = 5;

    @Resource
    SkGoodsDao skGoodsDao;

    /**
     * 查询商品列表
     *
     * @return
     */
    public List<GoodsVo> listGoodsVo() {
        return skGoodsDao.listGoodsVo();
    }

//    /**
//     * 根据id查询指定商品
//     *
//     * @return
//     */
//    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
//        return skGoodsDao.getGoodsVoByGoodsId(goodsId);
//    }
//
//    /**
//     * 减少库存，每次减一
//     *
//     * @return
//     */
//    public boolean reduceStock(GoodsVo goods) {
//        int numAttempts = 0;
//        int ret = 0;
//        SkGoods sg = new SkGoods();
//        sg.setGoodsId(goods.getId());
//        sg.setVersion(goods.getVersion());
//        do {
//            numAttempts++;
//            try {
//                sg.setVersion(skGoodsDao.getVersionByGoodsId(goods.getId()));
//                ret = skGoodsDao.reduceStockByVersion(sg);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            if (ret != 0) {
//                break;
//            }
//        } while (numAttempts < DEFAULT_MAX_RETRIES);
//
//        return ret > 0;
//    }
}
