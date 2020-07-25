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
import com.simonhu.modules.sys.dao.SysDictDao;
import com.simonhu.modules.sys.entity.SysDictEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("sysDictService")
public class SysDictService extends ServiceImpl<SysDictDao, SysDictEntity> {
    public PageUtils queryPage(Map<String, Object> params) {
        String name = (String) params.get("name");
        IPage<SysDictEntity> page = this.page(
                new Query<SysDictEntity>().getPage(params),
                new QueryWrapper<SysDictEntity>()
                        .like(StringUtils.isNotBlank(name), "name", name)
        );
        return new PageUtils(page);
    }
}
