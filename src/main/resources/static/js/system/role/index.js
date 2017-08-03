	var grid_selector = "#grid-table";
	var pager_selector = "#grid-pager";
    $(document).ready(function () {
        $.jgrid.defaults.styleUI = 'Bootstrap';
        $(grid_selector).jqGrid({
        	url : '/system/role/list',
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
                	label: '角色名',
                    name: 'name',
                    width: 90
                },
                {
                    label: 'roleKey',
                    name: 'roleKey',
                    width: 100
                },
                {
                    label: '是否启用',
                    name: 'status',
                    width: 80,
                    align: 'center',
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
                    label: '描述',
                    name: 'description',
                    width: 100
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
                }
            ],
            pager: pager_selector,
            viewrecords: true,
            multiselect: true,
            hidegrid: false,
          	//排序
            onSortCol: function (index, colindex, sortorder){
                $(grid_selector).jqGrid("setGridParam",{
                	postData:{
                		sortOrder : index + " " + sortorder,
                		accountName : $.trim($("#search_roleName").val())
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
        //添加权限
        $("#permissionFun").click(function(){
        	var cbox = $(grid_selector).jqGrid('getGridParam','selarrrow');
        	if(cbox == ''){
        		layer.alert("请选择一个对象!", {
					icon : 0
				});
        		return;
        	}else if(cbox.length > 1){
        		layer.alert("只能选择一个对象!", {
					icon : 0
				});
        		return;
        	}
        	var url = '/system/resources/permissions?roleId='+cbox;
        	pageii = layer.open({
        		title : "分配权限",
        		type : 1,
        		btn: ['保存', '取消'],
        		area : [ '250px', '80%' ],
        		content : CommonUtil.ajax(url,'get'),
        		yes:function(index){
        			submit();
        		}
        	});
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
    	var url = "/system/role/addUI";
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
    
    function edit(){
    	var cbox = $(grid_selector).jqGrid('getGridParam','selarrrow');	
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
    	var url = '/system/role/editUI?id=' + cbox;
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
    
    function del(){
    	var cbox = $(grid_selector).jqGrid('getGridParam','selarrrow');	
    	if(cbox == ''){
    		layer.alert('请选择删除项！', {
				icon : 0
			});
    		return;
    	}
    	layer.confirm('是否删除？',function(index){
    		var url = "/system/role/delete";
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
    			layer.alert('删除失败！' + s.msg, {
					icon : 2
				});
    		}
    	});
    }
    
    function changeStatus(e){
    	var checked = $(e).prop('checked');
    	var id = $(e).attr('id').replace('checked-','');
    	var url = '/system/role/updateStatus';
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