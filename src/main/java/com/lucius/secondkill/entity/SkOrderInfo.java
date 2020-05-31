package com.lucius.secondkill.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.io.Serializable;

/**
 * (SkOrderInfo)实体类
 *
 * @author makejava
 * @since 2020-05-31 10:10:32
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SkOrderInfo implements Serializable {
    
    private Long id;
    
    private Long userId;
    
    private Long goodsId;
    
    private Long deliveryAddrId;
    
    private String goodsName;
    
    private Integer goodsCount;
    
    private Double goodsPrice;
    /**
    * 订单渠道，1在线，2android，3ios
    */
    private Object orderChannel;
    /**
    * 订单状态，0新建未支付，1已支付，2已发货，3已收货，4已退款，5已完成
    */
    private Object status;
    
    private Date createDate;
    
    private Date payDate;




}