/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Lucius
 * @create 2020/5/30
 * @since 1.0.0
 */


package com.lucius.secondkill.dao;


import com.lucius.secondkill.entity.SkUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SkUserDao {

    SkUser queryUserById(long id);

}
