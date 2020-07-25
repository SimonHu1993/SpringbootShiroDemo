package com.simonhu.modules.sys.service;

import com.simonhu.common.utils.IPUtils;
import com.simonhu.common.utils.R;
import com.simonhu.modules.sys.dao.BgRequestLogDao;
import com.simonhu.modules.sys.shiro.ShiroUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service("bgRequestLogService")
public class BgRequestLogService {
    private static final Logger log = LoggerFactory.getLogger(BgRequestLogService.class);
    @Autowired
    private BgRequestLogDao bgRequestLogDao;
    
    /**
     * @param userName
     * @param password
     * @param isSuccess 是否登录成功
     * @param request
     * @return R
     * @Description:记录登录日志
     * @Author:SimonHu
     * @Date: 2020/6/10 17:54
     */
    @Transactional
    public R loginLog(String userName, String password, boolean isSuccess, HttpServletRequest request) {
        password = ShiroUtils.sha256(userName, password);
        log.info("==================password===" + password);
        Map map = new HashMap();
        map.put("userName", userName);
        map.put("password", password);
        map.put("params", "userName=" + userName + ",password=" + password);
        map.put("request_ip", IPUtils.getIpAddr(request));
        int count = bgRequestLogDao.selOptErrLog(map);
        //错误5次超限
        if (count > 4) {
            return R.error(18, "登录错误超限，请30分钟后再试！");
        }
        //插入登录日志
        if (isSuccess) {
            map.put("unit_no", "success");
            map.put("login_unit_no", "0");
            bgRequestLogDao.insertBGRequsetLog(map);
            //登录成功之后重新统计错误次数
            bgRequestLogDao.updateBGRequsetLog(map);
            return R.ok();
        } else {
            map.put("password", password);
            map.put("unit_no", "fail");
            map.put("login_unit_no", "1");
            bgRequestLogDao.insertBGRequsetLog(map);
            return R.error(999, "账号或密码不正确！");
        }
    }
}
