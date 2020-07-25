
$(function() {
    $(window).on('resize', function () {
        var $content = $('#larry-tab .layui-tab-content');
        $content.height($(this).height() - 140);
        $content.find('iframe').each(function () {
            $(this).height($content.height());
        });
    }).resize();
})

//生成菜单
var menuItem = Vue.extend({
    name: 'menu-item',
    props: {item: {}},
    template: [
        '<li class="layui-nav-item" >',
        '<a v-if="item.type === 0" href="javascript:;">',
        '<i v-if="item.icon != null" :class="item.icon"></i>',
        '<span :title="item.name">{{item.name}}</span>',
        '<em class="layui-nav-more"></em>',
        '</a>',
        '<dl v-if="item.type === 0" class="layui-nav-child">',
        '<dd v-for="item in item.list" >',
        '<a v-if="item.type === 1" href="javascript:;" :data-url="item.url"><i v-if="item.icon != null" :class="item.icon" :data-icon="item.icon"></i> <span :title="item.name">{{item.name}}</span></a>',
        '</dd>',
        '</dl>',
        '<a v-if="item.type === 1" href="javascript:;" :data-url="item.url"><i v-if="item.icon != null" :class="item.icon" :data-icon="item.icon"></i> <span  :title="item.name">{{item.name}}</span></a>',
        '</li>'
    ].join('')
});

//注册菜单组件
Vue.component('menuItem', menuItem);
isquery=true;
var vm = new Vue({
    el: '#layui_layout',
    data: {
        user: {},
        year: {},
        menuList: {},
        password: '',
        newPassword: '',
        navTitle: "首页"
    },
    methods: {
        getYear:function(){
            var myDate = new Date();
            this.year=myDate.getFullYear();
        },
        getMenuList: function () {
            $.getJSON("sys/menu/nav", function (r) {
                vm.menuList = r.menuList;
            });
        },
        getUser: function () {
            $.getJSON("sys/user/info?_" + $.now(), function (r) {
                vm.user = r.user;
            });
        },
        updatePassword: function () {
            layer.open({
                type: 1,
                skin: 'layui-layer-molv',
                title: "修改密码",
                area: ['550px', '270px'],
                shadeClose: false,
                closeBtn:0,
                content: jQuery("#passwordLayer"),
                btn: ['修改', '取消'],
                btn1: function (index) {
                    var data = "password=" + vm.password + "&newPassword=" + vm.newPassword;
                    $.ajax({
                        type: "POST",
                        url: "sys/user/password",
                        data: data,
                        dataType: "json",
                        success: function (result) {
                            if (result.code == 0) {
                                layer.close(index);
                                layer.alert('修改成功', function (index) {
                                    location.reload();
                                });
                            } else {
                                layer.alert(result.msg);
                            }
                        }
                    });
                }
            });
        },
        needUpdatePwd: function () {
            layer.open({
                type: 1,
                skin: 'layui-layer-molv',
                title: "修改密码",
                area: ['550px', '270px'],
                shadeClose: false,
                closeBtn:0,
                content: jQuery("#passwordLayer"),
                btn: ['修改', '退出'],
                success:function(layero,index){
                    layero.find('.layui-layer-btn').css('text-align', 'center'); //改变位置按钮居中，left左
                },
                btn1: function (index) {
                    var data = "password=" + vm.password + "&newPassword=" + vm.newPassword;
                    $.ajax({
                        type: "POST",
                        url: "sys/user/password",
                        data: data,
                        dataType: "json",
                        success: function (result) {
                            if (result.code == 0) {
                                layer.close(index);
                                layer.msg('修改成功，即将退出重新登录', {
                                    time: 2000
                                },function (index) {
                                    location.href = "logout";
                                });
                            } else {
                                layer.alert(result.msg);
                            }
                        }
                    });
                },
                btn2:function () {
                    location.href = "logout";
                }
            });
        },
        updateHeader: function () {
            layer.open({
                type: 1,
                skin: 'layui-layer-molv',
                title: "修改资料",
                area: ['570px', '450px'],
                shadeClose: false,
                content: jQuery("#headerLayer"),
                btn: ['修改', '取消'],
                btn1: function (index) {
                   var data =JSON.stringify(vm.user);
                    $.ajax({
                        type: "POST",
                        url: "sys/user/updatePerson",
                        data: data,
                        contentType: "application/json;charset=UTF-8",
                        success: function (result) {
                            if (result.code == 0) {
                                layer.close(index);
                                layer.alert('修改成功', function (index) {
                                    location.reload();
                                });
                            } else {
                                layer.alert(result.msg);
                            }
                        }
                    });
                },
                btn2:function () {
                    location.reload();
                }
            });
            layui.use( 'upload', function () {
                var layer = layui.layer,upload = layui.upload, $ = layui.$;
                //普通图片上传
                var uploadInst = upload.render({
                    elem: '#userFace',
                    url: 'sys/user/fileUpload',//这里填写你的上传地址
                    method: 'post',
                    unwrap:true,
                    before: function (obj) {
                        /*如果您不想用上面的URL 进行上传,也可以在这里,添加你的上传方式*/
                        obj.preview(function (index, file, result) {
                            $('#userFace').attr('src', result);//图片链接（base64）
                        });
                    }, done: function (res) {
                        if (res.code == 0) {//长传成功(具体返回code 看后台是怎么处理的)
                            console.log(res)
                            vm.user.icon = res.pathPic;
                            return layer.msg('图片上传成功');
                            // demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-xs demo-reload">重试</a>');
                        } else {
                            layer.msg('上传失败!');
                        }
                    },
                    error: function () {
                        demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-xs demo-reload">重试</a>');
                    }
                });
                //上传失败监听重试按钮的操作
               /* demoText.find('.demo-reload').on('click', function () {
                    uploadInst.upload();
                });*/
            });
        },
        donate: function () {
            layer.open({
                type: 2,
                title: false,
                area: ['806px', '467px'],
                closeBtn: 1,
                shadeClose: false,
                content: ['http://cdn.renren.io/donate.jpg', 'no']
            });
        }
    },
    created: function () {
        this.getMenuList();
        this.getUser();
        this.getYear();
    },
    updated:function(){

        if($("#larry-side .layui-nav-item>a").length==0 || !isquery){
            return;
        }
        console.log("执行")
        isquery=false;
        layui.config({
            base: 'statics/js/',
        }).use(['navtab','layer'], function(){
            window.jQuery = window.$ = layui.jquery;
            window.layer = layui.layer;
            var element = layui.element;
            var  navtab = layui.navtab({
                elem: '.larry-tab-box',
                closed:false
            });
            $('#larry-nav-side').children('ul').find('li').each(function () {
                var $this = $(this);
                if ($this.find('dl').length > 0) {

                    var $dd = $this.find('dd').each(function () {
                        $(this).on('click', function () {
                            var $a = $(this).children('a');
                            var href = $a.data('url');
                            var icon = $a.children('i:first').data('icon');
                            var title = $a.children('span').text();
                            var data = {
                                href: href,
                                icon: icon,
                                title: title
                            }
                            navtab.tabAdd(data);
                        });
                    });
                } else {

                    $this.on('click', function () {
                        var $a = $(this).children('a');
                        var href = $a.data('url');
                        var icon = $a.children('i:first').data('icon');
                        var title = $a.children('span').text();
                        var data = {
                            href: href,
                            icon: icon,
                            title: title
                        }
                        navtab.tabAdd(data);
                    });
                }
            });
            $('.larry-side-menu').click(function () {
                var sideWidth = $('#larry-side').width();
                if (sideWidth === 200) {
                    $('#larry-body').animate({
                        left: '0'
                    });
                    $('#larry-footer').animate({
                        left: '0'
                    });
                    $('#larry-side').animate({
                        width: '0'
                    });
                } else {
                    $('#larry-body').animate({
                        left: '200px'
                    });
                    $('#larry-footer').animate({
                        left: '200px'
                    });
                    $('#larry-side').animate({
                        width: '200px'
                    });
                }
            });
            //判断是否需要修改密码
            if(vm.user.updatePwd == true){
                setTimeout(function (){
                    //延迟加载，使layui样式加载完毕后修改，否则按钮会向下偏移
                    vm.needUpdatePwd();
                }, 100);
            }

        });
    }

});