toastr.options = {
	closeButton : false,
	progressBar : false,
	showMethod : 'slideDown',
	timeOut : 1500
};
function update(){
	var accessToken = document.getElementById("accessToken").value;
	var BIG_WIN = document.getElementById("BIG_WIN").value;
	var MEGA_WIN = document.getElementById("MEGA_WIN").value;
	var PIC_WIN = document.getElementById("PIC_WIN").value;
	$.ajax({
		type : "GET", 
		url : "api/config_bigwin_mode?accessToken=" + accessToken+
				"&BIG_WIN="+BIG_WIN +"&MEGA_WIN="+MEGA_WIN+"&PIC_WIN="+PIC_WIN+"&write=true",
		dataType : "json",
		async: true,
		success : function(result) {
			var callback = parseInt(result['callback']);
			switch (callback) {
			case 1:
				toastr.success("Cập nhật thắng lớn thành công");
				break;
			case -1:
				toastr.error("Giá trị nhập vào phải lớn hơn 0");
			case -2:
				toastr.error("Giá trị nhập vào không hợp lệ");
			default:
				break;
			}
		},
		error:function(err){
			toastr.error("Bạn không có quyền truy cập chức năng này");
		}
	});
}