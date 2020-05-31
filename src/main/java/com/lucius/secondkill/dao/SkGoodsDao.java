/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Lucius
 * @create 2020/5/19
 * @since 1.0.0
 */


package com.lucius.secondkill.dao;

import com.lucius.secondkill.entity.SkGoods;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SkGoodsDao {

    List<SkGoods> listGoods();

    SkGoods queryGoodsById(long goodsId);

    int reduceStock(SkGoods skGoods);


//    @Select("select g.*, sg.stock_count, sg.start_date, sg.end_date, sg.seckill_price, sg.version  from sk_goods_seckill sg left join sk_goods g  on sg.goods_id = g.id where g.id = #{goodsId}")
//    GoodsVo getGoodsVoByGoodsId(@Param("goodsId") long goodsId);
//
//    //stock_count > 0 和 版本号实现乐观锁 防止超卖
//    @Update("update sk_goods_seckill set stock_count = stock_count - 1, version= version + 1 where goods_id = #{goodsId} and stock_count > 0 and version = #{version}")
//    int reduceStockByVersion(SkGoods skGoods);
//
//    // 获取最新版本号
//    @Select("select version from sk_goods_seckill  where goods_id = #{goodsId}")
//    int getVersionByGoodsId(@Param("goodsId") long goodsId);

}
