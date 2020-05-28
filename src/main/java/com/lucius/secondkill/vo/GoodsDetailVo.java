package com.lucius.secondkill.vo;

import com.lucius.secondkill.entity.SkGoods;
import com.lucius.secondkill.entity.SkUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by jiangyunxiong on 2018/5/24.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GoodsDetailVo {
    private int seckillStatus = 0;
    private int remainSeconds = 0;
    private SkGoods goods;
    private SkUser user;

}
