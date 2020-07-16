/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */
package com.zihexin.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zihexin.modules.sys.dao.SysRoleMenuDao;
import com.zihexin.modules.sys.dao.SysUserDao;
import com.zihexin.modules.sys.entity.SysRoleMenuEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色与菜单对应关系
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service("sysRoleMenuService")
public class SysRoleMenuService extends ServiceImpl<SysRoleMenuDao, SysRoleMenuEntity> {
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysRoleMenuDao sysRoleMenuDao;
    
    @Caching(evict = {@CacheEvict(value = "AllpermUser", allEntries = true),
            @CacheEvict(value = "UserMenuList", allEntries = true)})
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(Long roleId, List<Long> menuIdList) {
        //先删除角色与菜单关系
        deleteBatch(new Long[]{roleId});
        if (menuIdList.size() == 0) {
            return;
        }
        List<SysRoleMenuEntity> list = new ArrayList<>();
        //保存角色与菜单关系
        for (Long menuId : menuIdList) {
            SysRoleMenuEntity sysRoleMenuEntity = new SysRoleMenuEntity();
            sysRoleMenuEntity.setMenuId(menuId);
            sysRoleMenuEntity.setRoleId(roleId);
            list.add(sysRoleMenuEntity);
        }
        //批量insert角色与菜单关系
        sysRoleMenuDao.saveRoleMenuBatch(list);
    }
    
    public List<Long> queryMenuIdList(Long roleId) {
        return baseMapper.queryMenuIdList(roleId);
    }
    
    @Caching(evict = {@CacheEvict(value = "AllpermUser", allEntries = true),
            @CacheEvict(value = "UserMenuList", allEntries = true)})
    public int deleteBatch(Long[] roleIds) {
        return baseMapper.deleteBatch(roleIds);
    }
    
    /**
     * @param userId
     * @return java.util.List<java.lang.String>
     * @Description:查询用户的所有权限
     * @Author:SimonHu
     * @Date: 2020/6/3 10:24
     */
    @Cacheable(value = "AllpermUser", key = "'userId_'+#userId", unless = "#result == null or #result.size() == 0")
    public List<String> queryAllperm(Long userId) {
        List<String> permsList = sysUserDao.queryAllPerms(userId);
        return permsList;
    }
}
