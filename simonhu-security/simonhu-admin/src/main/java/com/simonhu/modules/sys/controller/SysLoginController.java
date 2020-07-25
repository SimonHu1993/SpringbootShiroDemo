/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */
package com.simonhu.modules.sys.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.simonhu.common.utils.AesUtil;
import com.wf.captcha.ArithmeticCaptcha;
import com.simonhu.common.annotation.SysLog;
import com.simonhu.common.utils.R;
import com.simonhu.modules.sys.service.BgRequestLogService;
import com.simonhu.modules.sys.service.SysLoginService;
import com.simonhu.modules.sys.shiro.ShiroUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录相关
 *
 * @author Mark sunlightcs@gmail.com
 */
@Controller
public class SysLoginController extends AbstractController {
    @Autowired
    private Producer producer;
    @Autowired
    private SysLoginService sysLoginService;
    @Autowired
    private BgRequestLogService bgRequestLogService;
    
    @RequestMapping("captcha.jpg")
    public void captcha(HttpServletResponse response) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        // 算术类型 https://gitee.com/whvse/EasyCaptcha
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(111, 36);
        // 几位数运算，默认是两位
        captcha.setLen(2);
        // 获取运算的结果
        String text = captcha.text();
        //保存到shiro session
        ShiroUtils.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY, text);
        ServletOutputStream out = response.getOutputStream();
        captcha.out(out);
    }
    
    /**
     * 登录
     */
    @SysLog("用户登录")
    @ResponseBody
    @RequestMapping(value = "/sys/login", method = RequestMethod.POST)
    public R login(HttpServletRequest request, HttpServletResponse response, String username, String password, String captcha) {
        String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
        if (!captcha.equalsIgnoreCase(kaptcha)) {
            return R.error("验证码不正确！");
        }
        try {
            //检查要求前台传来密码也要加密
            password = AesUtil.decrypt(password);
        } catch (Exception e) {
            logger.error("==解密密码失败==", e);
        }
        try {
            Subject subject = ShiroUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            subject.login(token);
        } catch (UnknownAccountException e) {
            return R.error(e.getMessage());
        } catch (IncorrectCredentialsException e) {
            R r = bgRequestLogService.loginLog(username, password, false, request);
            return r;
        } catch (LockedAccountException e) {
            return R.error("账号已被锁定,请联系管理员！");
        } catch (AuthenticationException e) {
            return R.error("账户验证失败！");
        }
        R r = bgRequestLogService.loginLog(username, password, true, request);
        if (!r.get("code").equals(0)) {
            return r;
        }
        //单点登录
        sysLoginService.ssoLogin(request, response);
        return R.ok();
    }
    
    /**
     * 退出
     */
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response, String kickout) {
        sysLoginService.ssoLogOut(request, response);
        ShiroUtils.logout();
        if (StringUtils.isNotEmpty(kickout)) {
            return "redirect:login.html?kickout=" + kickout;
        }
        return "redirect:login.html";
    }
}
