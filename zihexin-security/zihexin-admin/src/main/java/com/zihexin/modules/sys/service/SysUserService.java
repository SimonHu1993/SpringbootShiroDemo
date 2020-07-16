/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */
package com.zihexin.modules.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zihexin.common.annotation.DataFilter;
import com.zihexin.common.utils.*;
import com.zihexin.modules.sys.dao.SysUserDao;
import com.zihexin.modules.sys.dao.SysUserPwdLogDao;
import com.zihexin.modules.sys.entity.SysDeptEntity;
import com.zihexin.modules.sys.entity.SysRoleEntity;
import com.zihexin.modules.sys.entity.SysUserEntity;
import com.zihexin.modules.sys.entity.SysUserPwdLogEntity;
import com.zihexin.modules.sys.shiro.ShiroUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service("sysUserService")
public class SysUserService extends ServiceImpl<SysUserDao, SysUserEntity> {
    @Autowired
    private SysUserPwdLogDao sysUserPwdLogDao;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysDeptService sysDeptService;
    
    public List<Long> queryAllMenuId(Long userId) {
        return baseMapper.queryAllMenuId(userId);
    }
    
    @DataFilter(subDept = true, user = false)
    public PageUtils queryPage(Map<String, Object> params) {
        String username = (String) params.get("username");
        String nickname = (String) params.get("nickname");
        IPage<SysUserEntity> page = this.page(
                new Query<SysUserEntity>().getPage(params),
                new QueryWrapper<SysUserEntity>()
                        .like(StringUtils.isNotBlank(username), "username", username)
                        .like(StringUtils.isNotBlank(nickname), "nickname", nickname)
                        .apply(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER))
        );
        if(page.getRecords().isEmpty()){
            return new PageUtils(page);
        }
        List<Long> list = new ArrayList<>();
        page.getRecords().forEach(v -> {
            list.add(v.getDeptId());
        });
        List<SysDeptEntity> sysDeptEntitys = (List<SysDeptEntity>) sysDeptService.listByIds(list);
        for (SysUserEntity sysUserEntity : page.getRecords()) {
            for (SysDeptEntity sysDeptEntity : sysDeptEntitys) {
                if (sysDeptEntity.getDeptId().equals(sysUserEntity.getDeptId())) {
                    sysUserEntity.setDeptName(sysDeptEntity.getName());
                    break;
                }
            }
        }
        return new PageUtils(page);
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void saveUser(SysUserEntity user) {
        user.setCreateTime(new Date());
        //sha256加密
        String salt = RandomStringUtils.randomAlphanumeric(20);
        user.setSalt(salt);
        user.setPassword(ShiroUtils.sha256(user.getPassword(), user.getSalt()));
        this.save(user);
        //保存用户与角色关系
        sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void update(SysUserEntity user) {
        if (StringUtils.isBlank(user.getPassword())) {
            user.setPassword(null);
        } else {
            SysUserEntity userEntity = this.getById(user.getUserId());
            user.setPassword(ShiroUtils.sha256(user.getPassword(), userEntity.getSalt()));
        }
        this.updateById(user);
        //保存用户与角色关系
        sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
    }
    
    public boolean updatePassword(Long userId, String password, String newPassword) {
        SysUserEntity userEntity = new SysUserEntity();
        userEntity.setPassword(newPassword);
        return this.update(userEntity,
                new QueryWrapper<SysUserEntity>().eq("user_id", userId).eq("password", password));
    }
    /**
      * @Description:校验密码安全度
      * @Author:SimonHu
      * @Date: 2020/6/10 13:02
      * @param
      * @return
      */
    public R valitePwd(String password){
        if(!PwdCheckUtil.checkPasswordLength(password,"6","20")){
            return R.error("密码长度必须为6-20位");
        }else if(PwdCheckUtil.checkLateralKeyboardSite(password,3,false)){
            return R.error("密码不能使用连续数字或连续键盘字母");
        }else if(PwdCheckUtil.checkSequentialSameChars(password,3)){
            return R.error("密码不能使用相同数字");
        }
        return R.ok();
    }
    /**
      * @Description:校验最近三次密码是否一样
      * @Author:SimonHu
      * @Date: 2020/6/10 13:38
      * @param userId
      * @param pwd
      * @return boolean
      */
    public boolean isSameAsRencent3Days(Long userId,String pwd){
        List<SysUserPwdLogEntity> sysUserPwdLogEntitys = sysUserPwdLogDao.selectRencent3Time(userId);
        Set<String> sets = new HashSet<>();
        sysUserPwdLogEntitys.forEach(e->{
            sets.add(e.getPwd());
        });
        if(sets.contains(pwd)){
            return true;
        }
        return false;
    }
    
    
}
