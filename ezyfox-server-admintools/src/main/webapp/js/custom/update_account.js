var pt_phone = /^\(?([0-9]{3})\)?[-. ]?([0-9]{3})[-. ]?([0-9]{4})$/;
toastr.options = {
	closeButton : false,
	progressBar : false,
	showMethod : 'slideDown',
	timeOut : 1500
};
function checkname() {
	var fullname = document.getElementById("fullname").value;
	if (fullname == "") {
		toastr.error("Tên đăng nhập không được để trống");
	}
}
function checkphone() {
	var phone = document.getElementById("phone").value;
	if (!pt_phone.test(phone)) {
		toastr.error("Số điện thoại không hợp lệ");
	}
}
function checkaddress() {
	var address = document.getElementById("address").value;
	if (address == "") {
		toastr.error("Địa chỉ liên hệ không được để trống");
	}
}
function update() {
	var accessToken = document.getElementById("accessToken").value;
	var fullname = document.getElementById("fullname").value;
	var phone = document.getElementById("phone").value;
	var address = document.getElementById("address").value;
	$.ajax({
		type : "GET",
		url : "api/account_update?accessToken=" + accessToken + "&fullname="
				+ fullname + "&phone=" + phone + "&address=" + address,
		dataType : "json",
		async : true,
		success : function(result) {
			var callback = parseInt(result['callback']);
			switch (callback) {
			case 1:
				toastr.success("Cập nhật thông tin thành công");
				break;
			case -1:
				toastr.error("Cập nhật thông tin thất bại");
			default:
				break;
			}
		}
	});
}