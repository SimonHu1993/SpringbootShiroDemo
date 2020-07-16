/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */
package com.zihexin.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zihexin.modules.sys.dao.SysRoleDeptDao;
import com.zihexin.modules.sys.entity.SysRoleDeptEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色与部门对应关系
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service("sysRoleDeptService")
public class SysRoleDeptService extends ServiceImpl<SysRoleDeptDao, SysRoleDeptEntity> {
    @Autowired
    private SysRoleDeptDao sysRoleDeptDao;
    
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(Long roleId, List<Long> deptIdList) {
        //先删除角色与部门关系
        deleteBatch(new Long[]{roleId});
        if (deptIdList.size() == 0) {
            return;
        }
        List<SysRoleDeptEntity> list = new ArrayList<>();
        //保存角色与部门关系
        for (Long deptId : deptIdList) {
            SysRoleDeptEntity sysRoleDeptEntity = new SysRoleDeptEntity();
            sysRoleDeptEntity.setDeptId(deptId);
            sysRoleDeptEntity.setRoleId(roleId);
            list.add(sysRoleDeptEntity);
        }
        //批量insert角色与部门关系
        sysRoleDeptDao.saveRoleDeptBatch(list);
    }
    
    public List<Long> queryDeptIdList(Long[] roleIds) {
        return baseMapper.queryDeptIdList(roleIds);
    }
    
    public int deleteBatch(Long[] roleIds) {
        return baseMapper.deleteBatch(roleIds);
    }
}
