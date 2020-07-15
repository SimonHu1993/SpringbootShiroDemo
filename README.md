# SpringbootShiroDemo
SpringbootShiroDemo

- 基于人人开源框架（[人人开源](https://www.renren.io)：https://www.renren.io ）进行修改》
-1.所有菜单操作添加缓存@Cacheable，避免频繁数据库操作；
-2.优化项目内sql循环查询，频繁数据库io操作；
-3.登录次数错误限制；
-4.登录密码前台AES加密操作，避免明文密码登录；
-5.SSO单点登录，基于redis和本地cookie，没有权限的项目，简单进行拦截器配置，获取本地cookie中的token，从redis服务获取用户信息，可在没有权限的项目进行登录权限的校验；
-6.Shiro并发登录限制，不允许同一账号多个地方登录；
-7.初始账号强制修改密码，账号密码强度校验（不允许连续键盘和数字），指定时间强制修改密码；
-8.@Cacheable缓存过期时间 配置；
-9.JsonFormatter页面查询显示（日志请求参数）；
-10.物理删除修改为逻辑删除；
-11.动态修改个人信息

-演示地址：http://www.simonjia.top:8080/admin/index.html
