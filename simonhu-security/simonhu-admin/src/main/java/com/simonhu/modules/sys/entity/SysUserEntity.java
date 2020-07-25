/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */
package com.simonhu.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.simonhu.common.validator.group.AddGroup;
import com.simonhu.common.validator.group.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@TableName("sys_user")
public class SysUserEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 用户ID
     */
    @TableId
    private Long userId;
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String username;
    /**
     *昵称
     */
    @NotBlank(message = "昵称不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String nickname;
    /**
     * 头像
     */
    private String icon;
    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空", groups = AddGroup.class)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    /**
     * 盐
     */
    private String salt;
    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Email(message = "邮箱格式不正确", groups = {AddGroup.class, UpdateGroup.class})
    private String email;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 状态  0：禁用   1：正常
     */
    private Integer status;
    /**
     * 角色ID列表
     */
    @TableField(exist = false)
    private List<Long> roleIdList;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 部门ID
     */
    @NotNull(message = "部门不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private Long deptId;
    /**
     * 部门名称
     */
    @TableField(exist = false)
    private String deptName;
    /**
     * 需要更新密码
     */
    @TableField(exist = false)
    private boolean updatePwd;
    /**
     * 权限集合
     */
    @TableField(exist = false)
    private Set<String> permisSet;
    @TableLogic
    private Integer delFlag;
    
    @Override
    public String toString() {
        return "{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", icon='" + icon + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", status=" + status +
                ", roleIdList=" + roleIdList +
                ", permisSet=" + permisSet +
                ", createTime=" + createTime +
                ", deptId=" + deptId +
                ", delFlag=" + delFlag +
                ", updatePwd=" + updatePwd +
                ", deptName='" + deptName + '\'' +
                '}';
    }
}
