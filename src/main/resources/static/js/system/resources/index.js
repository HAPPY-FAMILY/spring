	var grid_selector = "#grid-table";
	$(document).ready(function(){
	   $.jgrid.defaults.styleUI = 'Bootstrap';
       jQuery(grid_selector).jqGrid({
    	   treeGrid: true,  
    	   ajaxGridOptions: { contentType: 'application/json; charset=utf-8' },
           treeGridModel: 'adjacency', //treeGrid模式，跟json元数据有关 ,adjacency/nested  
           ExpandColumn : 'username',  
           mtype: 'GET',
           autowidth: true,
           datatype: 'json',
           ExpandColClick: true,  
           url:'/system/resources/list',//后台action的地址
           dataType:'json',
         
           colModel:[

            {
            	label:'菜单名',
            	name:'name',
            	width: 200,
                sortable: false
            },
            {
            	label:'url',
            	name:'url',
            	width: 200,
                sortable: false
            },
            {
            	label:'是否启用',
            	name:'status',
                sortable: false,
                align: 'center',
                width: 80,
                formatter:function(value,options,rows){
                	var flag = '';
                	if (value == 1) {
                		flag = 'checked';
                	}
                	var html = '<div class="onoffswitch">'
	                            + '<input type="checkbox" ' + flag + ' class="onoffswitch-checkbox" id="checked-'+ rows.id +'" onchange="changeStatus(this)">'
	                            + '<label class="onoffswitch-label" for="checked-'+ rows.id +'">'
	                            +    '<span class="onoffswitch-inner"></span>'
	                            +    '<span class="onoffswitch-switch"></span>'
	                            + '</label>'
	                        + '</div>';
                    return html;
                }
            },
            {	
            	label:'resKey',
            	name:'resKey',
            	width: 200,
                sortable: false
            },
            {
            	label:'菜单类型',
            	name:'type',
            	width: 100,
            	align: 'center',
                sortable: false,
                formatter: function(value,options,rows){
                	if(value == 0){
                		return '<span class="label label-info">模块</span>';
                	}else if(value == 1){
                		return '<span class="label label-info">目录</span>';
                	}else if(value == 2){
                		return '<span class="label label-info">菜单</span>';
                	}else if(value == 3){
                		return '<span class="label label-info">按钮</span>';
                	}else{
                		return '<span class="label label-info">其他</span>';
                	}
                }
            },
            {
            	label: '操作',
            	name: 'id',
            	width: 200,
            	align: 'center',
            	formatter:function(value,options,rows){
            		var html = '';
            		var edit = $('#editFun');
            		var del = $('#delFun');
            		if(edit.length == 1){
            			html += '<button class="btn btn-primary btn-xs" type="button" onclick="edit(\'' + value + '\')"><i class="fa fa-edit"></i>&nbsp;修改</button> &nbsp;';
            		}
					if(del.length == 1){
            			html += '<button class="btn btn-danger btn-xs" type="button" onclick="del(\'' + value + '\')"><i class="fa fa-remove"></i>&nbsp;删除</button>';
            		}
            		return html;
            	}
            }
            ],
         page:false,
         height:'auto',
         //viewrecords:true,
      jsonReader:{
            root:'rows',
            repeatitems:true
            },
      pager: "false",  
     })
     
     $('#btn_search').click(function(){
         search();
     });
     $('#btn_reset').click(function(){
         $('#searchForm')[0].reset()
     });
     //新增
     $('#addFun').click(function(){
    	 add();
     });

   	$('#editFun').click(function(){
  		edit();
  	});
     
   	$('#delFun').click(function(){
  		del();
  	});
})
  
function search(){
	$(grid_selector).jqGrid("setGridParam",{
		page:1,
    	postData:$('#searchForm').serializeJson(),
    	datatype: "json"
    }).trigger("reloadGrid");
}
   
function add() {
 	var url = '/system/resources/addUI';
 	pageii = layer.open({
 		title : "新增",
 		type : 1,
 		btn: ['保存', '取消'],
 		area : [ "600px", "90%" ],
 		content : CommonUtil.ajax(url),
 		yes:function(index){
			submit();
		}
 	});
}
   
function edit(cbox){
	var url = '/system/resources/editUI?id=' + cbox;
	pageii = layer.open({
		title : "编辑",
		id: 'update-form',
	   	type:1,
		btn: ['保存', '取消'],
		area : [ "600px", "80%" ],
		content : CommonUtil.ajax(url,'get'),
		yes:function(index){
			submit();
		}
	});	
}

function del(cbox){
	layer.confirm('是否删除？',function(index){
		var url = "/system/resources/delete";
		var s = CommonUtil.ajax(url, 
								"post",
								{ids: cbox},
								"json");
		if(s.status == 200){
			layer.msg('删除成功！');
			$(grid_selector).delRowData(cbox);
		}else{
			layer.alert('删除失败！' + s.msg, {
				icon : 2
			});
		}
	});
}
function changeStatus(e){
   	var checked = $(e).prop('checked');
   	var id = $(e).attr('id').replace('checked-','');
   	var url = '/system/resources/updateStatus';
   	var json;
   	if(checked){
   		json = CommonUtil.ajax(url,"post",{'id':id,'status':1},'json')
   	}else{
   		json = CommonUtil.ajax(url,"post",{'id':id,'status':0},'json')
   	}
   	if(json.status == 200){
   		layer.msg('修改成功！');
   	}else{
   		layer.alert('修改失败！', {
				icon : 2
			});
   	}
}   
