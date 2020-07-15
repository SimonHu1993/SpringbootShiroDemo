package com.zihexin.common.interceptor;

import com.zihexin.common.utils.CookieUtil;
import com.zihexin.common.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: SimonHu
 * @Date: 2020/5/22 16:39
 * @Description:
 */
@Component
@Slf4j
public class RedisSessionInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisUtils redisUtils;
    @Value("${zihexin.globalSessionTimeout}")
    private long globalSessionTimeout;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = CookieUtil.readLoginToken(request);
        String requestUrl = request.getServletPath();
        log.info("-------------requestUrl---------"+requestUrl);
        redisUtils.expire(token, globalSessionTimeout);
        CookieUtil.writeLoginToken(response, token);
        return true;
    }
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
