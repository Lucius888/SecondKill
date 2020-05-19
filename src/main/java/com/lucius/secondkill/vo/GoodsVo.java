package com.lucius.secondkill.vo;

import com.lucius.secondkill.entity.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 设计数据库的时候将普通商品和秒杀商品放在两个文件里面了
 * 一个里面存的是基本信息
 * 一个存的是关于该商品的秒杀信息
 * 这个类继承了Goods，并且又拥有了秒杀商品的信息。
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GoodsVo extends Goods {

    private Double seckillPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
    private Integer version;

    }

