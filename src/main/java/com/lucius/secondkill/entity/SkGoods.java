package com.lucius.secondkill.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.io.Serializable;

/**
 * (SkGoods)实体类
 *
 * @author makejava
 * @since 2020-05-19 17:43:46
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SkGoods implements Serializable {

    private Long id;
    private Long goodsId;
    private String goodsName;
    private String goodsTitle;
    private String goodsImg;
    private String goodsDetail;
    private Double goodsPrice;
    private Integer goodsStock;
    /**
     * 秒杀价
     */
    private Double seckillPrice;
    /**
     * 库存数量
     */
    private Integer stockCount;
    /**
     * 秒杀开始时间
     */
    private Date startDate;
    /**
     * 秒杀结束时间
     */
    private Date endDate;
    /**
     * 并发版本控制
     */
    private int version;


}