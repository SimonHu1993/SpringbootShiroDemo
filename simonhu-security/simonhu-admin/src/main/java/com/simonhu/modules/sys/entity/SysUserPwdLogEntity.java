package com.simonhu.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 管理员密码日志
 *
 * @author SimonHu
 * @date 2020-06-10 13:23:09
 */
@Data
@TableName("sys_user_pwd_log")
public class SysUserPwdLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Integer id;
	/**
	 *
	 */
	private Long userId;
	/**
	 *
	 */
	private String pwd;
	/**
	 *
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

}
