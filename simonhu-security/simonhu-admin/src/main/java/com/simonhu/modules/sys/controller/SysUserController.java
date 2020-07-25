/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */
package com.simonhu.modules.sys.controller;

import com.simonhu.common.annotation.SysLog;
import com.simonhu.common.utils.ImageUtil;
import com.simonhu.common.utils.PageUtils;
import com.simonhu.common.utils.R;
import com.simonhu.common.validator.Assert;
import com.simonhu.common.validator.ValidatorUtils;
import com.simonhu.common.validator.group.AddGroup;
import com.simonhu.common.validator.group.UpdateGroup;
import com.simonhu.modules.sys.entity.SysUserEntity;
import com.simonhu.modules.sys.entity.SysUserPwdLogEntity;
import com.simonhu.modules.sys.service.SysUserPwdLogService;
import com.simonhu.modules.sys.service.SysUserRoleService;
import com.simonhu.modules.sys.service.SysUserService;
import com.simonhu.modules.sys.shiro.ShiroUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {
    @Autowired
    private SysUserPwdLogService sysUserPwdLogService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Value("${site.homePicPathHead}")
    private String homePicPathHead;
    @Value("${site.showhomePicPathHead}")
    private String showhomePicPathHead;
    
    /**
     * 所有用户列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:user:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = sysUserService.queryPage(params);
        return R.ok().put("page", page);
    }
    
    /**
     * 获取登录的用户信息
     */
    @RequestMapping("/info")
    public R info() {
        return R.ok().put("user", getUser());
    }
    
    /**
     * 修改登录用户密码
     */
    @RequestMapping("/password")
    public R password(String password, String newPassword) {
        Assert.isBlank(newPassword, "新密码不为能空");
        R r = sysUserService.valitePwd(newPassword);
        if (!r.get("code").equals(0)) {
            return r;
        }
        Long userId = getUserId();
        //原密码
        password = ShiroUtils.sha256(password, getUser().getSalt());
        //新密码
        newPassword = ShiroUtils.sha256(newPassword, getUser().getSalt());
        if (sysUserService.isSameAsRencent3Days(userId, newPassword)) {
            return R.error("不能和最近使用密码一样");
        };
        //更新密码
        boolean flag = sysUserService.updatePassword(userId, password, newPassword);
        if (!flag) {
            return R.error("原密码不正确");
        }
        //插入密码修改记录
        SysUserPwdLogEntity sysUserPwdLogEntity = new SysUserPwdLogEntity();
        sysUserPwdLogEntity.setUserId(userId);
        sysUserPwdLogEntity.setPwd(newPassword);
        sysUserPwdLogEntity.setCreateTime(new Date());
        sysUserPwdLogService.saveUpdatePwd(sysUserPwdLogEntity);
        return R.ok();
    }
    
    /**
     * 用户信息
     */
    @RequestMapping("/info/{userId}")
    @RequiresPermissions("sys:user:info")
    public R info(@PathVariable("userId") Long userId) {
        SysUserEntity user = sysUserService.getById(userId);
        //获取用户所属的角色列表
        List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
        user.setRoleIdList(roleIdList);
        return R.ok().put("user", user);
    }
    
    /**
     * 保存用户
     */
    @SysLog("保存用户")
    @RequestMapping("/save")
    @RequiresPermissions("sys:user:save")
    public R save(@RequestBody SysUserEntity user) {
        ValidatorUtils.validateEntity(user, AddGroup.class);
        R r = sysUserService.valitePwd(user.getPassword());
        if (!r.get("code").equals(0)) {
            return r;
        }
        sysUserService.saveUser(user);
        return R.ok();
    }
    
    /**
     * 修改用户
     */
    @SysLog("修改用户")
    @RequestMapping("/update")
    @RequiresPermissions("sys:user:update")
    public R update(@RequestBody SysUserEntity user) {
        if (StringUtils.isNotEmpty(user.getPassword())) {
            R r = sysUserService.valitePwd(user.getPassword());
            if (!r.get("code").equals(0)) {
                return r;
            }
        }
        ValidatorUtils.validateEntity(user, UpdateGroup.class);
        sysUserService.update(user);
        return R.ok();
    }
    
    /**
     * 删除用户
     */
    @SysLog("删除用户")
    @RequestMapping("/delete")
    @RequiresPermissions("sys:user:delete")
    public R delete(@RequestBody Long[] userIds) {
        if (ArrayUtils.contains(userIds, 1L)) {
            return R.error("系统管理员不能删除");
        }
        if (ArrayUtils.contains(userIds, getUserId())) {
            return R.error("当前用户不能删除");
        }
        sysUserService.removeByIds(Arrays.asList(userIds));
        return R.ok();
    }
    
    /**
     * 修改用户
     */
    @SysLog("修改个人资料")
    @RequestMapping("/updatePerson")
    public R updatePerson(@RequestBody SysUserEntity user) {
        ValidatorUtils.validateEntity(user, UpdateGroup.class);
        sysUserService.update(user);
        //更新登录信息
        setUser(user);
        return R.ok();
    }
    
    @RequestMapping(value = "/fileUpload")
    public R fileUpload(@RequestParam(value = "file") MultipartFile pic, HttpServletRequest request) {
        if (pic.isEmpty()) {
            System.out.println("文件为空");
        }
        String path = homePicPathHead;
        //生成当日文件夹名
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String todayFile = sdf.format(new Date());
        path = path + todayFile + "/";
        String fileName = pic.getOriginalFilename();
        String newFileName = String.valueOf(Math.random()).substring(2, 13) + fileName.substring(fileName.indexOf("."), fileName.length());
        String picStr640 = "";
        try {
            File targetFile = new File(path, newFileName);
            if (!targetFile.exists()) {
                targetFile.mkdirs();
            }
            pic.transferTo(targetFile);
            picStr640 = ImageUtil.getPicStr(newFileName, "640");
            ImageUtil.zoom(path + newFileName, path + picStr640, 0.667f);
            //删除原图，保存小图
            targetFile.delete();
        } catch (Exception e) {
            logger.error("--ImageUtil error--", e);
        }
//        String picStr1080 = ImageUtil.getPicStr(newFileName, "1080");
//          ImageUtil.zoom(path + newFileName, path + picStr1080, 1f);
        String pathPic = showhomePicPathHead + todayFile + "/" + picStr640;
        return R.ok().put("pathPic", pathPic);
    }
}
