$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/log/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', width: 30, key: true },
			{ label: '用户名', name: 'username', width: 50 }, 			
			{ label: '用户操作', name: 'operation', width: 50 },
			{ label: '请求方法', name: 'method', width: 200 },
			{ label: '请求参数', name: 'params', width: 40,formatter: function(value,row,index){
                    var str= '<a class="label label-success" href="javascript:void(0)" onclick="showParams('+JSON.stringify(value).replace(/\"/g,"'")+')">点击查看</a>'
                    return str;
                }},
            { label: '执行时长', name: 'time', width: 30,formatter: function(value,row,index){
                return value+"ms";
                } },
			{ label: 'IP地址', name: 'ip', width: 40 },
			{ label: '创建时间', name: 'createDate', width: 90 }			
        ],
		viewrecords: true,
        height: 500,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: false,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });

});
function showParams(value){
    $("#jsonContainer").empty();
    var options = {dom : document.getElementById('jsonContainer')};
    window.jf = new JsonFormatter(options);
    jf.doFormat(value);
    layer.open({
        type: 1,
        title: false,
        closeBtn: 0,
        area: '516px',
        shadeClose: true,
        content: $('#jsonContainer')
    });
}
var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			key: null
		},
	},
	methods: {
		query: function () {
			vm.reload(true);
		},
		reload: function (isSearch) {
			var page = $("#jqGrid").jqGrid('getGridParam','page');
            if(isSearch){
                page = 1;
            }
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData:{'key': vm.q.key},
                page:page
            }).trigger("reloadGrid");
		}
	}
});