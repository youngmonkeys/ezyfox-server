toastr.options = {
	closeButton : false,
	progressBar : false,
	showMethod : 'slideDown',
	timeOut : 1500
};
var ck_password = /^[A-Za-z0-9!@#$%^&*()_]{6,20}$/;
function check_pass() {
	// var oldpass = document.getElementById("currentPassword").value;
	// var pass = document.getElementById("md5Password").value;
	// if (oldpass == pass) {
	// toastr.success("Mật khẩu khớp");
	// } else {
	// toastr.error("Mật khẩu không khớp");
	// }
}
function resetPass() {
	var accessToken = document.getElementById("accessToken").value;
	var password = document.getElementById("password").value;
	var newpassword = document.getElementById("new_pass").value;
	var confirm_password = document.getElementById("confirm_pass").value;
	if (!ck_password.test(password)) {
		toastr.error('Mật khẩu không hợp lệ');
	} else {
		$.ajax({
			type : "GET",
			url : "api/reset_password?accessToken=" + accessToken
					+ "&password=" + password + "&new_pass=" + newpassword
					+ "&confirm_pass=" + confirm_password,
			dataType : "json",
			async : true,
			success : function(result) {
				var callback = parseInt(result['callback']);
				console.log("Callback:" + callback);
				switch (callback) {
				case -2:
					toastr.error("Mật khẩu vừa nhập không khớp");
					break;
				case -1:
					toastr.error("Mật khẩu cũ không khớp");
					break;
				case 1:
					toastr.success("Cập nhật mật khẩu thành công");
					clearData();
					break;
				default:
					break;
				}
			}
		});
	}

}
function clearData() {
	document.getElementById("password").value = "";
	document.getElementById("new_pass").value = "";
	document.getElementById("confirm_pass").value = "";
}