package com.lucius.secondkill.service;

import com.lucius.secondkill.entity.SkGoods;
import com.lucius.secondkill.entity.SkOrder;
import com.lucius.secondkill.entity.SkOrderInfo;
import com.lucius.secondkill.entity.SkUser;

import java.util.List;

/**
 * (SkOrder)表服务接口
 *
 * @author makejava
 * @since 2020-05-29 20:44:39
 */
public interface SkOrderService {

    /**
     * 通过ID查询单条数据
     */
    SkOrder queryOrderByUserIdAndGoodsId(long userId, long goodsId);


    /**
     * 生成订单
     * @param user
     * @param skGoods
     * @return
     */
    SkOrderInfo createOrder(SkUser user, SkGoods skGoods) ;


//    /**
//     * 查询多条数据
//     *
//     * @param offset 查询起始位置
//     * @param limit 查询条数
//     * @return 对象列表
//     */
//    List<SkOrder> queryAllByLimit(int offset, int limit);
//
//    /**
//     * 新增数据
//     *
//     * @param skOrder 实例对象
//     * @return 实例对象
//     */
//    SkOrder insert(SkOrder skOrder);
//
//    /**
//     * 修改数据
//     *
//     * @param skOrder 实例对象
//     * @return 实例对象
//     */
//    SkOrder update(SkOrder skOrder);
//
//    /**
//     * 通过主键删除数据
//     *
//     * @param id 主键
//     * @return 是否成功
//     */
//    boolean deleteById(Long id);

}