var elem = document.querySelector('.js-switch-isLeaf');
var switchery = new Switchery(elem, {
    color: '#1AB394'
});
var elem = document.querySelector('.js-switch-status');
var switchery = new Switchery(elem, {
    color: '#1AB394'
});
$(function(){
	// 异步加载所有菜单列表
	$("#addform").validate({
		submitHandler : function(form) {// 必须写在验证前面，否则无法ajax提交
			ly.ajaxSubmit(form,{//验证新增是否成功
				type : "post",
				dataType : "json",
				success : function(data) {
					if (data.status == "200") {
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
					type : "POST",
					url : '/system/resources/isExist',
					data : {
						name : function() {
							return $("#name").val();
						}
					}
				}
			},
			"resKey" : {
				remote : { // 异步验证是否存在
					type : "POST",
					url : '/system/resources/isExist',
					data : {
						resKey : function() {
							return $("#resKey").val();
						}
					}
				}
			}
		},
		messages : {
			"name" : {
				required : "菜单名称不能为空",
				remote : "该菜单名称已经存在"
			},
			"resKey" : {
				remote : "该key已经存在"
			}
		},
		errorPlacement : function(error, element) {// 自定义提示错误位置
			$(".l_err").css('display', 'block');
			// element.css('border','3px solid #FFCCCC');
			$(".l_err").html(error.html());
		},
		success : function(label) {// 验证通过后
			$(".l_err").css('display', 'none');
		}
	});
	
	
	
	
	
	
	
	
	
	$("#icon").iconPicker();
	
	var url = '/system/resources/reslists';
	var data = CommonUtil.ajax(url, "get",null,"json");
	if (data != null) {
		var h = "<option parent-ids='' value='0'>------顶级目录------</option>";
		for ( var i = 0; i < data.length; i++) {
			h+="<option parent-ids='" + data[i].parentIds + "' value='" + data[i].id + "'>"+ data[i].name + "</option>";
		}
		$("#parentId").html(h);
	} else {
		layer.msg("获取菜单信息错误，请联系管理员！");
	}
    $('#parentId').select2();
})


function but(v){
	if(v.value==3){
		$('.form-group.icon').hide();
		 showBut();
		 
	}else{
		if(v.value==2){
			$('.form-group.icon').hide();
		}else{
			$('.form-group.icon').show();
		}
		$("#divbut").css("display","none");
		$("#divbutshe").css("display","none")
	}
}
function toBut(b){
	var bt = $(b).children("a");
	$("#btnId").val(bt.attr("id"));
	$("#btnName").val(bt.attr("name"));
	$("#btnClass").val(bt.attr("class"));
	$("#btnValue").val(bt.find('b').html());
}
function showBut(){
	$("#divbut").css("display","block");
	$("#divbutshe").css("display","block");
	$("#but").html(CommonUtil.btnlist());
}
function submit(){
	var clickStatusCheckbox = document.querySelector('.js-switch-status');
	var clickIsLeafCheckbox = document.querySelector('.js-switch-isLeaf');
	if(clickStatusCheckbox.checked){
		$('#status').val(1);
	}else{
		$('#status').val(0)
	}
	
	if(clickIsLeafCheckbox.checked){
		$('#isLeaf').val(1);
	}else{
		$('#isLeaf').val(0)
	}
	
    if($('#divbutshe').css('display') != 'none'){
		$('#btn').val($('#btnId').val() + ',' + $('#btnValue').val() + ',' + $('#btnClass').val() + ',' + $('#btnValue').val() + ',' + $('#btnName').val());
	}
    var parentIds = $('#parentId').find('option:selected').attr('parent-ids');
    if(parentIds != '' && parentIds != undefined && parentIds != null){
    	parentIds += ',' + $('#parentId').val();
    }else{
    	parentIds = 0;
    }
    $('#parentIds').val(parentIds);
	$("#addform").submit();
}