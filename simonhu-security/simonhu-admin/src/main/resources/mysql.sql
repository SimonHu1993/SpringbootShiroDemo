/*
 Navicat Premium Data Transfer

 Source Server         : 阿里云
 Source Server Type    : MySQL
 Source Server Version : 50727
 Source Host           :
 Source Schema         : simon_jia_dev

 Target Server Type    : MySQL
 Target Server Version : 50727
 File Encoding         : 65001

 Date: 15/07/2020 16:03:17
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `dept_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '上级部门ID，一级部门为0',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `order_num` int(11) NULL DEFAULT NULL COMMENT '排序',
  `del_flag` tinyint(4) NULL DEFAULT 0 COMMENT '是否删除  -1：已删除  0：正常',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门管理' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (1, 0, '[zhx]控股集团', 0, 0);
INSERT INTO `sys_dept` VALUES (2, 1, '北京总公司', 1, 0);

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `operation` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户操作',
  `method` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求方法',
  `params` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求参数',
  `time` bigint(20) NOT NULL COMMENT '执行时长(毫秒)',
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_log_id`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统日志' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_log
-- ----------------------------
INSERT INTO `sys_log` VALUES (1, 'admin001', '用户登录', 'SysLoginController.login()', '[\"admin001\",\"1jBNSk+qvlLKlVK0kV7Ytw\\u003d\\u003d\",\"8\"]', 450, '10.0.13.24', '2020-07-15 01:50:23');
INSERT INTO `sys_log` VALUES (2, 'admin001', '修改个人资料', 'SysUserController.updatePerson()', '{\"userId\":1,\"username\":\"admin001\",\"nickname\":\"炒鸡管理员\",\"icon\":\"http://114.255.22.47:8081/appimages/admin_head/20200711/66515902378_640.jpg\",\"salt\":\"P2ZYHoH358tjRsy4m6Hs\",\"email\":\"admin001@qq.com\",\"mobile\":\"13688888888\",\"status\":1,\"roleIdList\":[],\"createTime\":\"Nov 12, 2016 1:11:11 AM\",\"deptId\":1,\"updatePwd\":false,\"permisSet\":[\"sys:menu:update\",\"sys:menu:delete\",\"sys:dept:update\",\"sys:dept:info\",\"sys:menu:list\",\"sys:menu:perms\",\"sys:dept:delete\",\"sys:dept:list\",\"sys:user:delete\",\"sys:dept:select\",\"sys:user:update\",\"sys:role:list\",\"sys:menu:info\",\"sys:menu:select\",\"sys:role:select\",\"sys:user:list\",\"sys:menu:save\",\"sys:role:save\",\"sys:role:info\",\"sys:role:update\",\"sys:user:info\",\"sys:role:delete\",\"sys:user:save\",\"sys:dept:save\",\"sys:log:list\"],\"delFlag\":0}', 556, '10.0.13.24', '2020-07-15 01:50:47');
INSERT INTO `sys_log` VALUES (3, 'admin001', '用户登录', 'SysLoginController.login()', '[\"admin001\",\"1jBNSk+qvlLKlVK0kV7Ytw\\u003d\\u003d\",\"-4\"]', 1757, '222.129.19.10', '2020-07-15 02:33:44');
INSERT INTO `sys_log` VALUES (4, 'admin001', '用户登录', 'SysLoginController.login()', '[\"admin001\",\"1jBNSk+qvlLKlVK0kV7Ytw\\u003d\\u003d\",\"2\"]', 1631, '222.129.19.10', '2020-07-15 02:51:52');
INSERT INTO `sys_log` VALUES (5, 'admin001', '修改个人资料', 'SysUserController.updatePerson()', '{\"userId\":1,\"username\":\"admin001\",\"nickname\":\"炒鸡管理员\",\"icon\":\"http://www.simonjia.top:8082/appimages/admin_head/20200715/12723871712_640.jpg\",\"salt\":\"P2ZYHoH358tjRsy4m6Hs\",\"email\":\"admin001@qq.com\",\"mobile\":\"13688888888\",\"status\":1,\"roleIdList\":[],\"createTime\":\"Nov 12, 2016 1:11:11 AM\",\"deptId\":1,\"updatePwd\":false,\"permisSet\":[\"sys:menu:update\",\"sys:menu:delete\",\"sys:dept:update\",\"sys:dept:info\",\"sys:menu:list\",\"sys:menu:perms\",\"sys:dept:delete\",\"sys:dept:list\",\"sys:user:delete\",\"sys:dept:select\",\"sys:user:update\",\"sys:role:list\",\"sys:menu:info\",\"sys:menu:select\",\"sys:role:select\",\"sys:user:list\",\"sys:menu:save\",\"sys:role:save\",\"sys:role:info\",\"sys:role:update\",\"sys:user:info\",\"sys:role:delete\",\"sys:user:save\",\"sys:dept:save\",\"sys:log:list\"],\"delFlag\":0}', 487, '222.129.19.10', '2020-07-15 02:52:09');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单URL',
  `perms` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type` int(11) NULL DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `order_num` int(11) NULL DEFAULT NULL COMMENT '排序',
  `del_flag` tinyint(4) NULL DEFAULT 0 COMMENT '删除状态：-1删除 0未删除',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 254 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单管理' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 0, '系统管理', NULL, NULL, 0, 'fa fa-cog', 1, 0);
INSERT INTO `sys_menu` VALUES (2, 1, '管理员管理', 'modules/sys/user.html', NULL, 1, 'fa fa-user', 2, 0);
INSERT INTO `sys_menu` VALUES (3, 1, '角色管理', 'modules/sys/role.html', NULL, 1, 'fa fa-user-secret', 3, 0);
INSERT INTO `sys_menu` VALUES (4, 1, '菜单管理', 'modules/sys/menu.html', NULL, 1, 'fa fa-th-list', 4, 0);
INSERT INTO `sys_menu` VALUES (15, 2, '查看', NULL, 'sys:user:list,sys:user:info', 2, NULL, 15, 0);
INSERT INTO `sys_menu` VALUES (16, 2, '新增', NULL, 'sys:user:save,sys:role:select', 2, NULL, 16, 0);
INSERT INTO `sys_menu` VALUES (17, 2, '修改', NULL, 'sys:user:update,sys:role:select', 2, NULL, 17, 0);
INSERT INTO `sys_menu` VALUES (18, 2, '删除', NULL, 'sys:user:delete', 2, NULL, 18, 0);
INSERT INTO `sys_menu` VALUES (19, 3, '查看', NULL, 'sys:role:list,sys:role:info', 2, NULL, 19, 0);
INSERT INTO `sys_menu` VALUES (20, 3, '新增', NULL, 'sys:role:save,sys:menu:perms', 2, NULL, 20, 0);
INSERT INTO `sys_menu` VALUES (21, 3, '修改', NULL, 'sys:role:update,sys:menu:perms', 2, NULL, 21, 0);
INSERT INTO `sys_menu` VALUES (22, 3, '删除', NULL, 'sys:role:delete', 2, NULL, 22, 0);
INSERT INTO `sys_menu` VALUES (23, 4, '查看', NULL, 'sys:menu:list,sys:menu:info', 2, NULL, 23, 0);
INSERT INTO `sys_menu` VALUES (24, 4, '新增', NULL, 'sys:menu:save,sys:menu:select', 2, NULL, 24, 0);
INSERT INTO `sys_menu` VALUES (25, 4, '修改', NULL, 'sys:menu:update,sys:menu:select', 2, NULL, 25, 0);
INSERT INTO `sys_menu` VALUES (26, 4, '删除', NULL, 'sys:menu:delete', 2, NULL, 26, 0);
INSERT INTO `sys_menu` VALUES (29, 1, '系统日志', 'modules/sys/log.html', 'sys:log:list', 1, 'fa fa-file-text-o', 29, 0);
INSERT INTO `sys_menu` VALUES (31, 1, '部门管理', 'modules/sys/dept.html', NULL, 1, 'fa fa-file-code-o', 31, 0);
INSERT INTO `sys_menu` VALUES (32, 31, '查看', NULL, 'sys:dept:list,sys:dept:info', 2, NULL, 32, 0);
INSERT INTO `sys_menu` VALUES (33, 31, '新增', NULL, 'sys:dept:save,sys:dept:select', 2, NULL, 33, 0);
INSERT INTO `sys_menu` VALUES (34, 31, '修改', NULL, 'sys:dept:update,sys:dept:select', 2, NULL, 34, 0);
INSERT INTO `sys_menu` VALUES (35, 31, '删除', NULL, 'sys:dept:delete', 2, NULL, 35, 0);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `del_flag` tinyint(4) NULL DEFAULT 0 COMMENT '删除状态：-1删除 0未删除',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 54 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '管理员', NULL, 1, '2020-07-09 08:45:09', 0);

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门ID',
  `del_flag` tinyint(4) NULL DEFAULT 0 COMMENT '删除状态：-1删除 0未删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 43 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色与部门对应关系' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------


-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NULL DEFAULT NULL COMMENT '菜单ID',
  `del_flag` tinyint(4) NULL DEFAULT 0 COMMENT '删除状态：-1删除 0未删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 941 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色与菜单对应关系' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `salt` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '盐',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '状态  0：禁用   1：正常',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `del_flag` tinyint(4) NULL DEFAULT 0 COMMENT '删除状态：-1删除 0未删除',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 398 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin001', '炒鸡管理员', 'http://www.simonjia.top:8082/appimages/admin_head/20200715/12723871712_640.jpg', '4ff42505d4694308c09d41604219831e489d3e049d3a46bab2070a5fc51112c0', 'P2ZYHoH358tjRsy4m6Hs', 'admin001@qq.com', '13688888888', 1, 1, '2016-11-11 11:11:11', 0);

-- ----------------------------
-- Table structure for sys_user_pwd_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_pwd_log`;
CREATE TABLE `sys_user_pwd_log`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(10) NOT NULL,
  `pwd` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_time` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_log_pwd_userid`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '管理员密码日志' ROW_FORMAT = COMPACT;


DROP TABLE IF EXISTS `bg_request_log`;
CREATE TABLE `bg_request_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `request_path` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `request_key` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `user_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `user_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `login_unit_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `unit_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `params` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `request_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `request_date` datetime(0) NULL DEFAULT NULL,
  `login_unit_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `login_unit_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
)

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
  `del_flag` tinyint(4) NULL DEFAULT 0 COMMENT '删除状态：-1删除 0未删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户与角色对应关系' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
