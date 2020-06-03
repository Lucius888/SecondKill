package com.lucius.secondkill.rabbitmq;


import com.lucius.secondkill.entity.SkUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息体
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SeckillMessage {
    private SkUser user;
    private long goodsId;

}
