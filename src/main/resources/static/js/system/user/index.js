   	var grid_selector = "#grid-table";
	var pager_selector = "#grid-pager";
    $(document).ready(function () {
        $.jgrid.defaults.styleUI = 'Bootstrap';
        $(grid_selector).jqGrid({
        	url : '/system/user/list',
            datatype : "json",
            height: 450,
            autowidth: true,
            shrinkToFit: true,
            rowNum: 20,
            rowList: [10, 20, 30],
            colModel: [
                {
                	label: 'id',
                    name: 'id',
                    width: 60,
                    hidden: true
                },
                {
                	label: '用户名',
                    name: 'accountName',
                    width: 90
                },
                {
                    label: '手机号',
                    name: 'mobile',
                    width: 100
                },
                {
                    label: '邮箱',
                    name: 'email',
                    width: 100
                },
                {
                    label: '所属角色',
                    name: 'roleName',
                    width: 100
                },
                {
                    label: '是否启用',
                    name: 'locked',
                    width: 80,
                    align: 'center',
                    formatter:function(value,options,rows){
                    	var flag = '';
                    	if (value == 0) {
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
                    label: '创建时间',
                    name: 'createDate',
                    width: 150
                },
                {
                    label: '更新时间',
                    name: 'updateDate',
                    width: 150
                },
                {
                    label: '备注',
                    name: 'remark',
                    width: 150
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
            pager: pager_selector,
            viewrecords: true,
            multiselect: true,
            hidegrid: false,
            multiboxonly : true,
          	//排序
            onSortCol: function (index, colindex, sortorder){
                $(grid_selector).jqGrid("setGridParam",{
                	postData:{
                		sortOrder : index + " " + sortorder,
                		accountName : $.trim($("#search_accountName").val()),
                		email : $.trim($("#search_email").val()),
                		mobile : $.trim($("#search_mobile").val()),
                		roleName : $.trim($("#search_roleName").val()),
                	},
                	datatype: "json"
                }).trigger("reloadGrid");
            },
            //查询条件
            postData:$('#searchForm').serializeJson()
        }).navGrid(pager_selector,{
	       	 add: false,
	         edit: false,
	         del: false,
	         search: false,
	         refresh: true
	    });

        // Add responsive to jqGrid
        $(window).bind('resize', function () {
            var width = $('.jqGrid_wrapper').width();
            $(grid_selector).setGridWidth(width);
        });
        
        
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
        
    });
    
    function search(){
    	$(grid_selector).jqGrid("setGridParam",{
    		page:1,
        	postData:$('#searchForm').serializeJson(),
        	datatype: "json"
        }).trigger("reloadGrid");
    }
    
    function add(){
    	var url = "/system/user/addUI";
    	pageii = layer.open({
    		title: "新增",
    		id: 'insert-form',
    	   	type:1,
    		btn: ['保存', '取消'],
    		area:["600px","90%"],
    		content:CommonUtil.ajax(url,"get"),
    		yes:function(index){
    			submit();
    		}
    	});
    }
    
    function edit(id){
    	var cbox;
    	if(id != '' && id != null && id != undefined){
    		cbox = [id];
    	}else{
    		cbox = $(grid_selector).jqGrid('getGridParam','selarrrow');	
    	}
    	if(cbox == ""){
    		layer.alert("请选择编辑项！", {
				icon : 0
			});
    		return;
    	}
    	if (cbox.length > 1 || cbox == "") {
    		layer.alert("只能选择一个！", {
				icon : 0
			});
    		return;
    	}
    	var url = '/system/user/editUI?id=' + cbox;
    	pageii = layer.open({
    		title : "编辑",
    		id: 'update-form',
    	   	type:1,
    		btn: ['保存', '取消'],
    		area : [ "600px", "80%" ],
    		content : CommonUtil.ajax(url),
    		yes:function(index){
    			submit();
    		}
    	});	
    }
    
    function del(id){
    	var cbox;
    	if(id != '' && id != null && id != undefined){
    		cbox = [id];
    	}else{
    		cbox = $(grid_selector).jqGrid('getGridParam','selarrrow');	
    	}
    	if(cbox == ''){
    		layer.alert('请选择删除项！', {
				icon : 0
			});
    		return;
    	}
    	layer.confirm('是否删除？',function(index){
    		var url = "/system/user/delete";
    		var s = CommonUtil.ajax(url, 
    								"post",
    								{ids: cbox},
    								"json");
    		if(s.status == 200){
    			layer.msg('删除成功！');
    			var reccount = $(grid_selector).getGridParam('reccount');//当前页行数
    			if(reccount == cbox.length){
    				var prevPage = $(grid_selector).getGridParam('page') - 1;
    				$(grid_selector).jqGrid('setGridParam',{
    					page:prevPage <= 0?1:prevPage,
    					postData:$('#searchForm').serializeJson()
    				}).trigger('reloadGrid');
    			}else{
    				$(grid_selector).trigger('reloadGrid');
    			}
    		}else{
    			layer.alert('删除失败！', {
					icon : 2
				});
    		}
    	});
    }
    
    function changeStatus(e){
    	var checked = $(e).prop('checked');
    	var id = $(e).attr('id').replace('checked-','');
    	var url = '/system/user/updateStatus';
    	var json;
    	if(checked){
    		json = CommonUtil.ajax(url,"post",{'id':id,'locked':0},'json')
    	}else{
    		json = CommonUtil.ajax(url,"post",{'id':id,'locked':1},'json')
    	}
    	if(json.status == 200){
    		layer.msg('修改成功！');
    	}else{
    		layer.alert('修改失败！', {
				icon : 2
			});
    	}
    }