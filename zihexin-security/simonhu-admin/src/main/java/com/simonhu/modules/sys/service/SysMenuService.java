/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */
package com.simonhu.modules.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.simonhu.common.utils.Constant;
import com.simonhu.modules.sys.dao.SysMenuDao;
import com.simonhu.modules.sys.entity.SysMenuEntity;
import com.simonhu.modules.sys.entity.SysRoleMenuEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("sysMenuService")
public class SysMenuService extends ServiceImpl<SysMenuDao, SysMenuEntity> {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysRoleMenuService sysRoleMenuService;
    @Autowired
    private SysMenuDao sysMenuDao;
    
    /**
     * @return java.util.List<SysMenuEntity>
     * @Description:获取所有menu
     * @Author:SimonHu
     * @Date: 2020/6/3 9:56
     */
    @Cacheable(value = "Menus", unless = "#result == null or #result.size() == 0")
    public List<SysMenuEntity> queryAllMenu() {
        return sysMenuDao.selectList(null);
    }
    
    public List<SysMenuEntity> queryListParentId(Long parentId, List<Long> menuIdList) {
        List<SysMenuEntity> menuList = queryListParentId(parentId);
        if (menuIdList == null) {
            return menuList;
        }
        List<SysMenuEntity> userMenuList = new ArrayList<>();
        for (SysMenuEntity menu : menuList) {
            if (menuIdList.contains(menu.getMenuId())) {
                userMenuList.add(menu);
            }
        }
        return userMenuList;
    }
    
    /**
     * @param parentId
     * @return java.util.List<SysMenuEntity>
     * @Description:根据父id查询所有菜单
     * @Author:SimonHu
     * @Date: 2020/6/3 9:30
     */
    @Cacheable(value = "ListParentId", key = "'ListParentId_isShow_'+#parentId", unless = "#result == null or #result.size() == 0")
    public List<SysMenuEntity> queryListParentId(Long parentId) {
        return baseMapper.queryListParentId(parentId);
    }
    
    @Cacheable(value = "NotButtonList", unless = "#result == null or #result.size() == 0")
    public List<SysMenuEntity> queryNotButtonList() {
        return baseMapper.queryNotButtonList();
    }
    
    @Cacheable(value = "UserMenuList", key = "'UserMenuList_'+#userId", unless = "#result == null or #result.size() == 0")
    public List<SysMenuEntity> getUserMenuList(Long userId) {
        //系统管理员，拥有最高权限
        if (userId == Constant.SUPER_ADMIN) {
            return getAllMenuList(null);
        }
        //用户菜单列表
        List<Long> menuIdList = sysUserService.queryAllMenuId(userId);
        return getAllMenuList(menuIdList);
    }
    
    /**
     * 获取所有菜单列表
     */
    private List<SysMenuEntity> getAllMenuList(List<Long> menuIdList) {
        //查询根菜单列表
        List<SysMenuEntity> menuList = queryListParentId(0L, menuIdList);
        //递归获取子菜单
        getMenuTreeList(menuList, menuIdList);
        return menuList;
    }
    
    /**
     * 递归
     */
    private List<SysMenuEntity> getMenuTreeList(List<SysMenuEntity> menuList, List<Long> menuIdList) {
        List<SysMenuEntity> subMenuList = new ArrayList<SysMenuEntity>();
        for (SysMenuEntity entity : menuList) {
            //目录
            if (entity.getType() == Constant.MenuType.CATALOG.getValue()) {
                entity.setList(getMenuTreeList(queryListParentId(entity.getMenuId(), menuIdList), menuIdList));
            }
            subMenuList.add(entity);
        }
        return subMenuList;
    }
    
    /**
     * @param
     * @return java.util.List<SysMenuEntity>
     * @Description: 获取所有菜单列表
     * @Author:SimonHu
     * @Date: 2020/5/25 12:19
     */
    @Cacheable(value = "AllMenuList", unless = "#result == null or #result.size() == 0")
    public List<SysMenuEntity> getAllMenuListSetParentName() {
        //获取所有菜单
        List<SysMenuEntity> menuList = this.list();
        List<SysMenuEntity> parentList = menuList;
        //设置菜单父菜单名称
        for (int i = 0; i < menuList.size(); i++) {
            long parentId = menuList.get(i).getParentId();
            for (int j = 0; j < parentList.size(); j++) {
                long menuId = parentList.get(j).getMenuId();
                if (parentId == menuId) {
                    menuList.get(i).setParentName(parentList.get(j).getName());
                    break;
                }
            }
        }
        return menuList;
    }
    
    /**
     * @param menu
     * @return void
     * @Description:修改菜单
     * @Author:SimonHu
     * @Date: 2020/6/2 10:33
     */
    @Caching(evict = {
            @CacheEvict(value = "ListParentId", allEntries = true),
            @CacheEvict(value = "UserMenuList", allEntries = true),
            @CacheEvict(value = "AllMenuList", allEntries = true),
            @CacheEvict(value = "Menus", allEntries = true),
            @CacheEvict(value = "NotButtonList", allEntries = true)
    })
    public void updateByIdImpl(SysMenuEntity menu) {
        this.updateById(menu);
    }
    
    /**
     * @param menu
     * @return void
     * @Description:新建菜单
     * @Author:SimonHu
     * @Date: 2020/6/2 10:35
     */
    @Caching(evict = {
            @CacheEvict(value = "ListParentId", allEntries = true),
            @CacheEvict(value = "UserMenuList", allEntries = true),
            @CacheEvict(value = "AllMenuList", allEntries = true),
            @CacheEvict(value = "Menus", allEntries = true),
            @CacheEvict(value = "NotButtonList", allEntries = true)
    })
    public void saveMenu(SysMenuEntity menu) {
        this.save(menu);
    }
    
    @Caching(evict = {
            @CacheEvict(value = "ListParentId", allEntries = true),
            @CacheEvict(value = "UserMenuList", allEntries = true),
            @CacheEvict(value = "AllMenuList", allEntries = true),
            @CacheEvict(value = "Menus", allEntries = true),
            @CacheEvict(value = "NotButtonList", allEntries = true)
    })
    public void delete(Long menuId) {
        //删除菜单
        this.removeById(menuId);
        //删除菜单与角色关联
        sysRoleMenuService.remove(new QueryWrapper<SysRoleMenuEntity>().eq("menu_id", menuId));
    }
}
