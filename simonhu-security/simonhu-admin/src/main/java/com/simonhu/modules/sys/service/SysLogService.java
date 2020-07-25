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
import com.simonhu.common.utils.Query;
import com.simonhu.common.utils.PageUtils;
import com.simonhu.modules.sys.dao.SysLogDao;
import com.simonhu.modules.sys.entity.SysLogEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("sysLogService")
public class SysLogService extends ServiceImpl<SysLogDao, SysLogEntity> {
    public PageUtils queryPage(Map<String, Object> params) {
        String key = (String) params.get("key");
        IPage<SysLogEntity> page = this.page(
                new Query<SysLogEntity>().getPage(params,"id",false),
                new QueryWrapper<SysLogEntity>().like(StringUtils.isNotBlank(key), "username", key)
        );
        return new PageUtils(page);
    }
}
