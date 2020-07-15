package com.zihexin.modules.sys.service;

import com.zihexin.common.utils.*;
import com.zihexin.modules.sys.entity.SysMenuEntity;
import com.zihexin.modules.sys.entity.SysUserEntity;
import com.zihexin.modules.sys.shiro.ShiroUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 登录逻辑
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service
public class SysLoginService {
    private static final Logger log = LoggerFactory.getLogger(SysLoginService.class);
    @Autowired
    private RedisUtils redisUtils;
    @Value("${zihexin.globalSessionTimeout}")
    private long globalSessionTimeout;
    @Value("${site.tokenKey}")
    private String tokenKey;
    @Autowired
    private SysRoleMenuService sysRoleMenuService;
    @Autowired
    private SysMenuService sysMenuService;
    
    /**
     * @param request
     * @return void
     * @Description:单点登录
     * @Author:SimonHu
     * @Date: 2020/5/21 17:12
     */
    public void ssoLogin(HttpServletRequest request, HttpServletResponse response) {
        SysUserEntity userEntity = ShiroUtils.getUserEntity();
        Set<String> permsSet = this.getUserPermis(userEntity);
        userEntity.setPermisSet(permsSet);
        String token = null;
        try {
            token = UUID.randomUUID().toString().replaceAll("-", "") + "&" + userEntity.getUserId();
            redisUtils.set(token, JsonUtils.toJsonNf(userEntity), globalSessionTimeout);
            //存在cookie中token进行加密
            token = AESSecurityUtil.encrypt(token, tokenKey);
            System.out.println(JsonUtils.toJsonNf(userEntity));
            CookieUtil.writeLoginToken(response, token);
        } catch (Exception e) {
            log.error("---------", e);
        }
    }
    
    /**
     * @return Set<String>
     * @Description:获取用户信息（添加权限标识）用于返回客户端用户的permis
     * @Author:SimonHu
     * @Date: 2020/7/10 19:50
     */
    public Set<String> getUserPermis(SysUserEntity userEntity) {
        long userId = userEntity.getUserId();
        List<String> permsList;
        //系统管理员，拥有最高权限
        if (userId == Constant.SUPER_ADMIN) {
            List<SysMenuEntity> menuList = sysMenuService.queryAllMenu();
            permsList = new ArrayList<>(menuList.size());
            for (SysMenuEntity menu : menuList) {
                permsList.add(menu.getPerms());
            }
        } else {
            permsList = sysRoleMenuService.queryAllperm(userId);
        }
        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for (String perms : permsList) {
            if (StringUtils.isBlank(perms)) {
                continue;
            }
            perms = perms.replaceAll("\\s*", "");
            if (StringUtils.isBlank(perms)) {
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.split(",")));
        }
        return permsSet;
    }
    
    /**
     * @param request
     * @param response
     * @return void
     * @Description:退出登录
     * @Author:SimonHu
     * @Date: 2020/5/28 15:37
     */
    public void ssoLogOut(HttpServletRequest request, HttpServletResponse response) {
        String token = CookieUtil.readLoginToken(request);
        try {
            token = AESSecurityUtil.decrypt(token, tokenKey);
        } catch (Exception e) {
            log.info("-中台---token--解密失败-----");
        }
        //清除cookie
        CookieUtil.delLoginToken(request, response);
        redisUtils.delete(token);
    }
}
