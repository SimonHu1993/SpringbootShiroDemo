/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */
package com.zihexin.modules.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zihexin.modules.sys.dao.SysUserRoleDao;
import com.zihexin.modules.sys.entity.SysUserRoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户与角色对应关系
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service("sysUserRoleService")
public class SysUserRoleService extends ServiceImpl<SysUserRoleDao, SysUserRoleEntity> {
    @Autowired
    private SysUserRoleDao sysUserRoleDao;
    
    @Caching(evict = {@CacheEvict(value = "AllpermUser", allEntries = true),
            @CacheEvict(value = "UserMenuList", allEntries = true)})
    public void saveOrUpdate(Long userId, List<Long> roleIdList) {
        //先删除用户与角色关系
        this.remove(new QueryWrapper<SysUserRoleEntity>().eq("user_id", userId));
        if (roleIdList == null || roleIdList.size() == 0) {
            return;
        }
        List<SysUserRoleEntity> list = new ArrayList<>();
        //保存用户与角色关系
        for (Long roleId : roleIdList) {
            SysUserRoleEntity sysUserRoleEntity = new SysUserRoleEntity();
            sysUserRoleEntity.setUserId(userId);
            sysUserRoleEntity.setRoleId(roleId);
            list.add(sysUserRoleEntity);
        }
        //批量insert用户与角色关系
        sysUserRoleDao.saveRoleUserBatch(list);
    }
    
    public List<Long> queryRoleIdList(Long userId) {
        return baseMapper.queryRoleIdList(userId);
    }
    
    @Caching(evict = {@CacheEvict(value = "AllpermUser", allEntries = true),
            @CacheEvict(value = "UserMenuList", allEntries = true)})
    public int deleteBatch(Long[] roleIds) {
        return baseMapper.deleteBatch(roleIds);
    }
}
