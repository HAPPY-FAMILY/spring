	var elem = document.querySelector('.js-switch');
	var switchery = new Switchery(elem, {
	    color: '#1AB394'
	});
	
	$('#user-selected').multiSelect({
		  selectableHeader: "<input type='text' class='search-input form-control' autocomplete='off' placeholder='可选组'>",
		  selectionHeader: "<input type='text' class='search-input form-control' autocomplete='off' placeholder='已选组'>",
		  afterInit: function(ms){
		    var that = this,
		        $selectableSearch = that.$selectableUl.prev(),
		        $selectionSearch = that.$selectionUl.prev(),
		        selectableSearchString = '#'+that.$container.attr('id')+' .ms-elem-selectable:not(.ms-selected)',
		        selectionSearchString = '#'+that.$container.attr('id')+' .ms-elem-selection.ms-selected';

		    that.qs1 = $selectableSearch.quicksearch(selectableSearchString)
		    .on('keydown', function(e){
		      if (e.which === 40){
		        that.$selectableUl.focus();
		        return false;
		      }
		    });

		    that.qs2 = $selectionSearch.quicksearch(selectionSearchString)
		    .on('keydown', function(e){
		      if (e.which == 40){
		        that.$selectionUl.focus();
		        return false;
		      }
		    });
		  },
		  afterSelect: function(){
		    this.qs1.cache();
		    this.qs2.cache();
		  },
		  afterDeselect: function(){
		    this.qs1.cache();
		    this.qs2.cache();
		  }
	});
	
	
	 $(function() {
		 	$("#addform").validate({
		 		submitHandler : function(form) {//必须写在验证前面，否则无法ajax提交
		 			ly.ajaxSubmit(form,{//验证新增是否成功
		 				type : "post",
		 				dataType:"json",
		 				success : function(data) {
		 					if (data.status == 200) {
		 						layer.msg('添加成功!');
		 						$("#grid-table").jqGrid("setGridParam",{
		 							postData:$('#searchForm').serializeJson()
		        				}).trigger("reloadGrid");
								layer.close(pageii);
		 					} else {
		 						layer.alert('添加失败！', {icon : 2});
		 						console.log(data.msg);
		 					}
		 				}
		 			});
		 		},
		 		rules : {
					"name" : {
						required : true,
						remote : { // 异步验证是否存在
							type : "GET",
							url : '/system/role/isExist',
							data : {
								name : function(){
									return $.trim($("#name").val());
								}
							}
						}
					},
					"roleKey" : {
						required : true,
						remote : { // 异步验证是否存在
							type : "GET",
							url : '/system/role/isExist',
							data : {
								roleKey : function(){
									return $.trim($("#roleKey").val());
								}
							}
						}
					}
				},
				messages : {
					"name" : {
						required : "请输入角色名",
						remote : "该角色名已经存在"
					},
					"roleKey" : {
						required : "请输入roleKey",
						remote : "roleKey已经存在"
					}
				
				},
		 		errorPlacement : function(error, element) {//自定义提示错误位置
		 			$(".l_err").css('display','block');
		 			//element.css('border','3px solid #FFCCCC');
		 			$(".l_err").html(error.html());
		 		},
		 		success: function(label) {//验证通过后
		 			$(".l_err").css('display','none');
		 		}
		 	});
		 });
	
	
	function submit(){
		var clickCheckbox = document.querySelector('.js-switch');
		if(clickCheckbox.checked){
			$('#status').val(1);
		}else{
			$('#status').val(0)
		}
		$('#txtUserSelect').val($('#user-selected').val());
		$("#addform").submit();
	}