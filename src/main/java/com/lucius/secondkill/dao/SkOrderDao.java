package com.lucius.secondkill.dao;

import com.lucius.secondkill.entity.SkOrder;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (SkOrder)表数据库访问层
 *
 * @author makejava
 * @since 2020-05-29 20:44:39
 */
 @Mapper
public interface SkOrderDao {

    /**
     * 通过UserId和GoodsId查询单条数据
     * 当出现同一个userid中出现两个相同goodsid时说明重复秒杀。
     *
     */
    SkOrder queryOrderByUserIdAndGoodsId(long userId, long goodsId);



        /**
     * 新增数据
     *
     * @param skOrder 实例对象
     * @return 影响行数
     */
    int insert(SkOrder skOrder);

//    /**
//     * 查询指定行数据
//     *
//     * @param offset 查询起始位置
//     * @param limit 查询条数
//     * @return 对象列表
//     */
//    List<SkOrder> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);
//
//
//    /**
//     * 通过实体作为筛选条件查询
//     *
//     * @param skOrder 实例对象
//     * @return 对象列表
//     */
//    List<SkOrder> queryAll(SkOrder skOrder);
//

//
//    /**
//     * 修改数据
//     *
//     * @param skOrder 实例对象
//     * @return 影响行数
//     */
//    int update(SkOrder skOrder);
//
//    /**
//     * 通过主键删除数据
//     *
//     * @param id 主键
//     * @return 影响行数
//     */
//    int deleteById(Long id);

}