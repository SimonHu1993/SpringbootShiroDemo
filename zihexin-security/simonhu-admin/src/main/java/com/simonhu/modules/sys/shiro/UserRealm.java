/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */
package com.simonhu.modules.sys.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.simonhu.modules.sys.entity.SysUserEntity;
import com.simonhu.modules.sys.service.SysLoginService;
import com.simonhu.modules.sys.service.SysUserPwdLogService;
import com.simonhu.modules.sys.service.SysUserRoleService;
import com.simonhu.modules.sys.dao.SysUserDao;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * 认证
 *
 * @author Mark sunlightcs@gmail.com
 */
@Component
public class UserRealm extends AuthorizingRealm {
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    /**
     * 避免Spring Caching注解失效
     */
    @Lazy
    private SysLoginService sysLoginService;
    @Autowired
    @Lazy
    private SysUserRoleService sysUserRoleService;
    @Autowired
    @Lazy
    private SysUserPwdLogService sysUserPwdLogService;
    
    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SysUserEntity user = (SysUserEntity) principals.getPrimaryPrincipal();
        //用户权限列表
        Set<String> permsSet;
        permsSet = sysLoginService.getUserPermis(user);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        return info;
    }
    
    /**
     * 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        //查询用户信息
        SysUserEntity user = sysUserDao.selectOne(new QueryWrapper<SysUserEntity>().eq("username", token.getUsername()));
        //校验密码是否需要修改
        boolean isUpdate = sysUserPwdLogService.isNeedUpdatePwd(user.getUserId());
        user.setUpdatePwd(isUpdate);
        //获取用户所属的角色列表
        List<Long> roleIdList = sysUserRoleService.queryRoleIdList(user.getUserId());
        user.setRoleIdList(roleIdList);
        //账号不存在
        if (user == null) {
            throw new UnknownAccountException("账号或密码不正确");
        }
        //账号锁定
        if (user.getStatus() == 0) {
            throw new LockedAccountException("账号已被锁定,请联系管理员");
        }
        //若存在，将此用户存放到登录认证info中，无需自己做密码对比Shiro会为我们进行密码对比校验
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(user.getSalt()), getName());
        return info;
    }
    
    /**
     * @param credentialsMatcher
     * @return void
     * @Description:设置自定义认证加密方式
     * @Author:SimonHu
     * @Date: 2020/6/11 9:16
     */
    @Override
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        HashedCredentialsMatcher shaCredentialsMatcher = new HashedCredentialsMatcher();
        shaCredentialsMatcher.setHashAlgorithmName(ShiroUtils.hashAlgorithmName);
        shaCredentialsMatcher.setHashIterations(ShiroUtils.hashIterations);
        super.setCredentialsMatcher(shaCredentialsMatcher);
    }
}
