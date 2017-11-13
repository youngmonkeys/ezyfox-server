var data = [];
toastr.options = {
        closeButton: false,
        progressBar: false,
        showMethod: 'slideDown',
        timeOut: 500
  };
function write_per(id){
	
	document.getElementById('cbRead'+id).checked = document.getElementById('cbRead'+id).checked ||
											document.getElementById('cbWrite'+id).checked;
	var x = document.getElementById('cbWrite'+id).checked;
	var y = document.getElementById('cbRead'+id).checked
	if(x){
		savePermisson(id,"WRITE");
	}else if(y){
		savePermisson(id,"READ");
	} else {
		savePermisson(id,"");
	}
	
}
function read_per(id){
		document.getElementById('cbRead'+id).checked = document.getElementById('cbRead'+id).checked;
		var x = document.getElementById('cbWrite'+id).checked;
		var y = document.getElementById('cbRead'+id).checked;
		if(x){
			savePermisson(id,"WRITE");
		} else if(y){
			savePermisson(id,"READ");
		}else {
			savePermisson(id,"");
		}
		
}
function savePermisson(featureId,value){
	var id = document.getElementById('id').value;
	var username = document.getElementById('username').value;
	var accessToken = document.getElementById('accessToken').value;
	$.ajax({
		type : "GET", 
		url : "api/update_permission?accessToken=" + accessToken+"&id="+id+
							"&featureid="+featureId+"&value="+value+"&editor="+username+"&write=true",
		dataType : "json",
		async: true,
		success : function(result) {
			toastr.success("Gán quyền thành công");
		},error:function(err){
			toastr.error("Bạn không có quyền truy cập chức năng này");
		}
	});
}
