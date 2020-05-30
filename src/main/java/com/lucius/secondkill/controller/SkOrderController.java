package com.lucius.secondkill.controller;

import com.lucius.secondkill.entity.SkOrder;
import com.lucius.secondkill.service.SkOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (SkOrder)表控制层
 *
 * @author makejava
 * @since 2020-05-29 20:44:52
 */
@Controller
@RequestMapping("skOrder")
public class SkOrderController {
    /**
     * 服务对象
     */
    @Resource
    private SkOrderService skOrderService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public SkOrder selectOne(Long id) {
        return this.skOrderService.queryById(id);
    }

}