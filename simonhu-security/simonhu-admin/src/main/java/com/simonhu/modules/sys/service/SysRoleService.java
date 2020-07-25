/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */
package com.simonhu.modules.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.simonhu.common.utils.Constant;
import com.simonhu.common.utils.Query;
import com.simonhu.common.annotation.DataFilter;
import com.simonhu.common.utils.PageUtils;
import com.simonhu.modules.sys.dao.SysRoleDao;
import com.simonhu.modules.sys.entity.SysDeptEntity;
import com.simonhu.modules.sys.entity.SysRoleEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 角色
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service("sysRoleService")
public class SysRoleService extends ServiceImpl<SysRoleDao, SysRoleEntity> {
    @Autowired
    private SysRoleMenuService sysRoleMenuService;
    @Autowired
    private SysRoleDeptService sysRoleDeptService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysDeptService sysDeptService;
    
    @DataFilter(subDept = true, user = false)
    public PageUtils queryPage(Map<String, Object> params) {
        String roleName = (String) params.get("roleName");
        IPage<SysRoleEntity> page = this.page(
                new Query<SysRoleEntity>().getPage(params),
                new QueryWrapper<SysRoleEntity>()
                        .like(StringUtils.isNotBlank(roleName), "role_name", roleName)
                        .apply(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER))
        );
        if(page.getRecords().isEmpty()){
            return new PageUtils(page);
        }
        List<Long> list = new ArrayList<>();
        page.getRecords().forEach(v->{
            list.add(v.getDeptId());
        });
        List<SysDeptEntity> sysDeptEntitys = (List<SysDeptEntity>) sysDeptService.listByIds(list);
        for (SysRoleEntity sysRoleEntity : page.getRecords()) {
            for(SysDeptEntity sysDeptEntity:sysDeptEntitys){
                    if(sysDeptEntity.getDeptId().equals(sysRoleEntity.getDeptId())){
                        sysRoleEntity.setDeptName(sysDeptEntity.getName());
                        break;
                    }
            }
        }
        return new PageUtils(page);
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void saveRole(SysRoleEntity role) {
        role.setCreateTime(new Date());
        this.save(role);
        //保存角色与菜单关系
        sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());
        //保存角色与部门关系
        sysRoleDeptService.saveOrUpdate(role.getRoleId(), role.getDeptIdList());
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void update(SysRoleEntity role) {
        this.updateById(role);
        //更新角色与菜单关系
        sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());
        //保存角色与部门关系
        sysRoleDeptService.saveOrUpdate(role.getRoleId(), role.getDeptIdList());
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Long[] roleIds) {
        //删除角色
        this.removeByIds(Arrays.asList(roleIds));
        //删除角色与菜单关联
        sysRoleMenuService.deleteBatch(roleIds);
        //删除角色与部门关联
        sysRoleDeptService.deleteBatch(roleIds);
        //删除角色与用户关联
        sysUserRoleService.deleteBatch(roleIds);
    }
}
