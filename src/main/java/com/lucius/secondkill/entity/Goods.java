package com.lucius.secondkill.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by jiangyunxiong on 2018/5/22.
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Goods implements Serializable {
    private Long id;
    private String goodsName;
    private String goodsTitle;
    private String goodsImg;
    private String goodsDetail;
    private Double goodsPrice;
    private Integer goodsStock;

}
