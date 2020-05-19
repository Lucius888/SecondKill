package com.lucius.secondkill.controller;

import com.lucius.secondkill.service.SkUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * (SkUser)表控制层
 *
 * @author makejava
 * @since 2020-05-18 13:50:35
 */
@Controller
public class SkUserController {
    /**
     * 服务对象
     */
    @Resource
    private SkUserService skUserService;

    /*
    登录跳转页面
     */
    @GetMapping({"/", "/login","/toLogin"})
    public String login(HttpSession session) {
        session.setAttribute("msg", "Please Login");
        return "login";
    }

    /*
    登录验证，shiro验证成功重定向到后台首页，否则返回登录页面
    */
    @RequestMapping({"/login"})
    public String adminIndex(
            HttpSession session,
            @RequestParam("username") String username,
            @RequestParam("password") String password) {
        // 获取Subject对象
        Subject subject = SecurityUtils.getSubject();

        //封装用户
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        try {
            subject.login(token);
            return "redirect:/index";
        } catch (UnknownAccountException e) {
            session.setAttribute("msg", "用户名或密码错误");
            return "login";
        } catch (IncorrectCredentialsException e) {//密码错误
            session.setAttribute("msg", "用户名或密码错误");
            return "login";
        }
    }

    /*
后台首页
 */
    @GetMapping({"/index", "/toIndex"})
    public String index(HttpServletResponse response) {
        //登录成功后要对session做分布式管理
        // 获取当前Subject对象
        Subject subject = SecurityUtils.getSubject();
        //获取绑定在当前subjuct的session
        Session session = subject.getSession();
        return "redirect:/goods/to_list";
    }

}