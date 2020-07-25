package com.simonhu.modules.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.simonhu.common.utils.Query;
import com.simonhu.common.utils.PageUtils;
import com.simonhu.modules.sys.dao.SysUserPwdLogDao;
import com.simonhu.modules.sys.entity.SysUserPwdLogEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("sysUserPwdLogService")
public class SysUserPwdLogService extends ServiceImpl<SysUserPwdLogDao, SysUserPwdLogEntity> {
    @Autowired
    private SysUserPwdLogDao sysUserPwdLogDao;
    
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SysUserPwdLogEntity> page = this.page(
                new Query<SysUserPwdLogEntity>().getPage(params),
                new QueryWrapper<SysUserPwdLogEntity>()
        );
        return new PageUtils(page);
    }
    
    /**
     * @param sysUserPwdLogEntity
     * @return void
     * @Description:保存修改密码日志信息
     * @Author:SimonHu
     * @Date: 2020/6/10 13:46
     */
    public void saveUpdatePwd(SysUserPwdLogEntity sysUserPwdLogEntity) {
        this.save(sysUserPwdLogEntity);
    }
    /**
      * @Description:是否需要更新密码
      * @Author:SimonHu
      * @Date: 2020/6/10 14:57
      * @param userId
      * @return boolean
      */
    public boolean isNeedUpdatePwd(Long userId) {
        Map map = new HashMap(16);
        map.put("userId", userId);
        map.put("days", 15);
        List<SysUserPwdLogEntity> list = sysUserPwdLogDao.selectRencentNdays(map);
        if (null == list || list.isEmpty()) {
            return true;
        }
        return false;
    }
}
