package com.lucius.secondkill.controller;

import com.lucius.secondkill.entity.SkUser;
import com.lucius.secondkill.redis.RedisService;
import com.lucius.secondkill.redis.UserKey;
import com.lucius.secondkill.service.SkUserService;
import com.lucius.secondkill.util.RedisUtil;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * (SkUser)表控制层
 *
 * @author makejava
 * @since 2020-05-18 13:50:35
 */
@Controller
public class SkUserController {
    private static Logger logger = Logger.getLogger(SkUserController.class);
    /**
     * 服务对象
     */


    @Resource
    private RedisService redisService;

    @Autowired
    private SkUserService skUserService;

    /*
    登录跳转页面
     */
    @GetMapping({"/", "/login", "/toLogin"})
    public String login(HttpSession session) {
        session.setAttribute("msg", "Please Login");
        return "login";
    }

    /*
    登录验证，shiro验证成功重定向到后台首页，否则返回登录页面
    */
    @RequestMapping({"/login"})
    public String adminIndex(
            HttpServletRequest request,
            Model model,
            @RequestParam("username") String username,
            @RequestParam("password") String password) {
        // 获取Subject对象
        Subject subject = SecurityUtils.getSubject();

        //封装用户
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        try {
            subject.login(token);
            // 通过用户名到数据库查询用户信息
            SkUser skUser = skUserService.queryUserById(Long.parseLong(username));
            HttpSession session = request.getSession();
            //把用户信息放到session中
            session.setAttribute("User", skUser);
            //存缓存实现session共享
            redisService.set(UserKey.Session , session.getId(), session);
            return "redirect:/index";
        } catch (UnknownAccountException e) {
            model.addAttribute("msg", "用户名或密码错误");
            return "login";
        } catch (IncorrectCredentialsException e) {//密码错误
            model.addAttribute("msg", "用户名或密码错误");
            return "login";
        }
    }

    /*
后台首页
 */
    @GetMapping({"/index", "/toIndex"})
    public String index(HttpServletRequest request, Model model) {
        //登录成功后要对session做分布式管理
        //获取session中的User
        SkUser skUser = (SkUser) request.getSession().getAttribute("User");
        if (skUser != null) {
            //redis有此人登录信息,正常浏览
            return "redirect:/goods/to_list";
        } else {
            //redis查无此人,重新登录
            model.addAttribute("msg", "用户信息失效，请重新登录");
            return "login";
        }

    }

}