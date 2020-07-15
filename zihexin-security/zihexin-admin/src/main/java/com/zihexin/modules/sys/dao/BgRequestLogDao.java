package com.zihexin.modules.sys.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * @author SimonHu
 * @email 877101804@qq.com
 * @date 2020-06-10 17:45:38
 */
@Mapper
public interface BgRequestLogDao {
    /**
      * @Description:插入中台登录日志
      * @Author:SimonHu
      * @Date: 2020/6/10 17:48
      * @param param
      * @return int
      */
    int insertBGRequsetLog(Map param);
    /**
      * @Description:更新中台登录日志
      * @Author:SimonHu
      * @Date: 2020/6/10 17:48
      * @param param
      * @return int
      */
    int updateBGRequsetLog(Map param);
    /**
      * @Description:查询错误日志
      * @Author:SimonHu
      * @Date: 2020/6/10 17:48
      * @param param
      * @return int
      */
    int selOptErrLog(Map param);
}
