package com.lucius.secondkill.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.lucius.secondkill.entity.SkUser;
import com.lucius.secondkill.dao.SkUserDao;
import com.lucius.secondkill.redis.UserKey;
import com.lucius.secondkill.service.SkUserService;
import com.lucius.secondkill.util.RedisUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * (SkUser)表服务实现类
 *
 * @author makejava
 * @since 2020-05-18 13:50:32
 */
@Service("skUserService")
public class SkUserServiceImpl implements SkUserService {
    @Resource
    private SkUserDao skUserDao;

    @Resource
    private RedisUtil redisUtil;

    public static final String COOKIE_NAME_TOKEN = "token";

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public SkUser queryById(Long id) {
        //查缓存
        SkUser skUser=redisUtil.get(UserKey.getById,""+id,SkUser.class);
        if(null!=skUser){
            return skUser;
        }
        //缓存没有就查数据库
        skUser=skUserDao.queryById(Long.parseLong(""+id));
        //查到之后存到缓存中
        if (skUser!=null){
            redisUtil.set(UserKey.getById,""+id,skUser);
        }
        return skUser;
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<SkUser> queryAllByLimit(int offset, int limit) {
        return this.skUserDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param skUser 实例对象
     * @return 实例对象
     */
    @Override
    public SkUser insert(SkUser skUser) {
        this.skUserDao.insert(skUser);
        return skUser;
    }

    /**
     * 修改数据
     *
     * @param skUser 实例对象
     * @return 实例对象
     */
    @Override
    public SkUser update(SkUser skUser) {
        this.skUserDao.update(skUser);
        return this.queryById(skUser.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.skUserDao.deleteById(id) > 0;
    }


    /**
     * 将token做为key，用户信息做为value 存入redis模拟session
     * 同时将token存入cookie，保存登录状态
     */
    @Override
    public void addCookie(HttpServletResponse response, String token, SkUser user) {
        redisUtil.set(UserKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(UserKey.token.expireSeconds());
        cookie.setPath("/");//设置为网站根目录
        response.addCookie(cookie);
    }

    /**
     * 根据token获取用户信息
     */
    @Override
    public SkUser getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        SkUser user = redisUtil.get(UserKey.token, token, SkUser.class);
        //延长有效期，有效期等于最后一次操作+有效期
        if (user != null) {
            addCookie(response, token, user);
        }
        return user;
    }

}