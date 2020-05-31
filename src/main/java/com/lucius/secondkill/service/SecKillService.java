/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Lucius
 * @create 2020/5/31
 * @since 1.0.0
 */


package com.lucius.secondkill.service;

import com.lucius.secondkill.entity.SkGoods;
import com.lucius.secondkill.entity.SkOrderInfo;
import com.lucius.secondkill.entity.SkUser;

public interface SecKillService {

    SkOrderInfo seckill(SkUser user, SkGoods skGoods);
}
