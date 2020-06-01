/**
 * Copyright (C), Lucius
 * FileName: SkUserServiceImpl
 * Author:
 * Date:     2020/5/30 19:47
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.lucius.secondkill.service.impl;

import com.lucius.secondkill.dao.SkUserDao;
import com.lucius.secondkill.entity.SkUser;
import com.lucius.secondkill.service.SkUserService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service("skUserService")
public class SkUserServiceImpl implements SkUserService {


    @Resource
    SkUserDao skUserDao;


    @Override
    public SkUser queryUserById(long id) {
        return skUserDao.queryUserById(id);
    }
}
