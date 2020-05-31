package com.lucius.secondkill.service.impl;

import com.lucius.secondkill.dao.SkOrderInfoDao;
import com.lucius.secondkill.entity.SkGoods;
import com.lucius.secondkill.entity.SkOrder;
import com.lucius.secondkill.dao.SkOrderDao;
import com.lucius.secondkill.entity.SkOrderInfo;
import com.lucius.secondkill.entity.SkUser;
import com.lucius.secondkill.service.SkOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * (SkOrder)表服务实现类
 *
 * @author makejava
 * @since 2020-05-29 20:44:39
 */
@Service("skOrderService")
public class SkOrderServiceImpl implements SkOrderService {
    @Resource
    private SkOrderDao skOrderDao;

    @Resource
    private SkOrderInfoDao skOrderInfoDao;

    /**
     * 通过ID查询单条数据

     */
    @Override
    public SkOrder queryOrderByUserIdAndGoodsId(long userId, long goodsId) {
        return this.skOrderDao.queryOrderByUserIdAndGoodsId(userId,goodsId);
    }


    /**
     * 生成订单,事务
     * @param user
     * @param skGoods
     * @return
     */
    @Transactional
    @Override
    public SkOrderInfo createOrder(SkUser user, SkGoods skGoods) {
        //1.生成SkOrderInfo
        SkOrderInfo skOrderInfo=new SkOrderInfo();
        skOrderInfo.setDeliveryAddrId(0L);//long类型 private Long deliveryAddrId;   L
        skOrderInfo.setCreateDate(new Date());
        skOrderInfo.setGoodsCount(1);
        skOrderInfo.setGoodsId(skGoods.getId());
        //秒杀物品信息
        skOrderInfo.setGoodsName(skGoods.getGoodsName());
        //秒杀价格
        skOrderInfo.setGoodsPrice(skGoods.getSeckillPrice());
        //订单渠道
        skOrderInfo.setOrderChannel(1);
        //订单状态  ---0-新建未支付  1-已支付  2-已发货  3-已收货
        skOrderInfo.setStatus(0);
        //用户id
        skOrderInfo.setUserId(user.getId());
        //返回orderId
        skOrderInfoDao.insert(skOrderInfo);

        //2.生成SkOrder
        SkOrder skOrder =new SkOrder();
        skOrder.setGoodsId(skGoods.getId());
        //将订单id传给秒杀订单里面的订单orderid
        skOrder.setOrderId(skOrderInfo.getId());
        skOrder.setUserId(user.getId());
        skOrderDao.insert(skOrder);

        return skOrderInfo;
    }

//    /**
//     * 查询多条数据
//     *
//     * @param offset 查询起始位置
//     * @param limit 查询条数
//     * @return 对象列表
//     */
//    @Override
//    public List<SkOrder> queryAllByLimit(int offset, int limit) {
//        return this.skOrderDao.queryAllByLimit(offset, limit);
//    }
//
//    /**
//     * 新增数据
//     *
//     * @param skOrder 实例对象
//     * @return 实例对象
//     */
//    @Override
//    public SkOrder insert(SkOrder skOrder) {
//        this.skOrderDao.insert(skOrder);
//        return skOrder;
//    }
//
//    /**
//     * 修改数据
//     *
//     * @param skOrder 实例对象
//     * @return 实例对象
//     */
//    @Override
//    public SkOrder update(SkOrder skOrder) {
//        this.skOrderDao.update(skOrder);
//        return this.queryById(skOrder.getId());
//    }
//
//    /**
//     * 通过主键删除数据
//     *
//     * @param id 主键
//     * @return 是否成功
//     */
//    @Override
//    public boolean deleteById(Long id) {
//        return this.skOrderDao.deleteById(id) > 0;
//    }
}