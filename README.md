# SpringbootShiroDemo


- 管理后台基于人人开源框架（[人人开源](https://www.renren.io)：https://www.renren.io ）进行修改》

很值得推荐的权限基础框架（无业务耦合），针对原有框架进行优化，后期业务再此进行拓展即可。

技术栈：

前端：Vue2.x,Layui,Ztree,TreeTable,Jqgrid;

服务端：Springboot2.x,Shiro,Redis,MybatisPlus,Ehcache。
> 1.所有菜单操作添加缓存@cacheable，避免频繁数据库操作；
> 
> 2.优化项目内sql循环查询，频繁数据库io操作；
> 
> 3.登录次数错误限制；
> 
> 4.登录密码前台aes加密操作，避免明文密码登录；
> 
> 5.sso单点登录，基于redis和本地cookie，没有权限的项目，简单进行拦截器配置，获取本地cookie中的token，从redis服务获取用户信息，可在没有权限的项目进行登录权限的校验；
> 
> 6.shiro并发登录限制，不允许同一账号多个地方登录；
> 
> 7.初始账号强制修改密码，账号密码强度校验（不允许连续键盘和数字），指定时间强制修改密码；
> 
> 8.@cacheable缓存过期时间 配置；
> 
> 9.jsonformatter页面查询显示（日志请求参数）；
> 
> 10.物理删除修改为逻辑删除；
> 
> 11.动态修改个人信息，不用下线退出。

----------

- **其他模块没有做修改，自动代码生成，可以作为本地开发辅助工具，提高敲码效率。**

演示地址：[https://www.simonjia.top/admin/login.html](https://www.simonjia.top/admin/index.html) 
账号：admin001
密码：admin001

<font size=5 color=red>**请勿修改密码：防止他人不能登录。**</font>

    阿里云服务器带宽有限，虽然使用缓存，还是会不那么顺滑- -
示例：
![](http://www.simonjia.top/appimages/admin_head/%E5%9B%BE%E7%89%875.png)
![](http://www.simonjia.top/appimages/admin_head/%E5%9B%BE%E7%89%876.png)
![](http://www.simonjia.top/appimages/admin_head/%E5%9B%BE%E7%89%871.png)
![](http://www.simonjia.top/appimages/admin_head/%E5%9B%BE%E7%89%872.png)
![](http://www.simonjia.top/appimages/admin_head/%E5%9B%BE%E7%89%873.png)
![](http://www.simonjia.top/appimages/admin_head/%E5%9B%BE%E7%89%874.png)
