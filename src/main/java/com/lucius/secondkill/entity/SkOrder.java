package com.lucius.secondkill.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * (SkOrder)实体类
 *
 * @author makejava
 * @since 2020-05-29 20:44:39
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SkOrder implements Serializable {
    
    private Long id;
    
    private Long userId;
    
    private Long orderId;
    
    private Long goodsId;
}