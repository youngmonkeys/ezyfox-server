toastr.options = {
        closeButton: false,
        progressBar: false,
        showMethod: 'slideDown',
        timeOut: 1500
  };
function update(){
	var accessToken = document.getElementById('accessToken').value;
	var value = document.getElementById('value').value;
	$.ajax({
		type : "GET", 
		url : "api/limit_jackpot_win_update?accessToken=" + accessToken+"&value="+value+"&write=true",
		dataType : "json",
		async: true,
		success : function(result) {
			var callback = parseInt(result['callback']);
			switch (callback) {
			case 1:
				toastr.success("Cập nhật thành công");
				break;
			case -1:
				toastr.error("Cập nhật thất bại");
				break;
			default:
				break;
			}
		},
		error:function(err){
			toastr.error("Bạn không có quyền truy cập chức năng này");
		}
	});
}