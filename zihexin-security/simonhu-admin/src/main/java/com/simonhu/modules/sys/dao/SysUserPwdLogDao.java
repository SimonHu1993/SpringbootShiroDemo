package com.simonhu.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.simonhu.modules.sys.entity.SysUserPwdLogEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 管理员密码日志
 *
 * @author SimonHu
 * @date 2020-06-10 13:23:09
 */
@Mapper
public interface SysUserPwdLogDao extends BaseMapper<SysUserPwdLogEntity> {
    /**
     * @param userId
     * @return
     * @Description:查询最近三次更新得密码
     * @Author:SimonHu
     * @Date: 2020/6/10 13:33
     */
    List<SysUserPwdLogEntity> selectRencent3Time(Long userId);
    /**
      * @Description:查询最近n天是否有修改密码
      * @Author:SimonHu
      * @Date: 2020/6/10 14:53
      * @param param
      * @return java.util.List<SysUserPwdLogEntity>
      */
    List<SysUserPwdLogEntity> selectRencentNdays(Map param);
}
