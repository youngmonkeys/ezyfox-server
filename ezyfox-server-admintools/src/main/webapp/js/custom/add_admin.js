var ck_username = /^[A-Za-z0-9_-]{4,15}$/;
var ck_password =  /^[A-Za-z0-9!@#$%^&*()_]{6,20}$/;
var pt_phone = /^\(?([0-9]{3})\)?[-. ]?([0-9]{3})[-. ]?([0-9]{4})$/;
toastr.options = {
        closeButton: false,
        progressBar: false,
        showMethod: 'slideDown',
        timeOut: 1500
  };
function validateusername(){
	var username = document.getElementById("username").value;
	if(!ck_username.test(username)){
		toastr.error('Tên đăng nhập không hợp lệ');
	}
}
function validatePass(){
	var password = document.getElementById("password").value;
	if(!ck_password.test(password)){
		toastr.error('Mật khẩu không hợp lệ');
	}
}
//$('#add_admin').submit(function(e){
//		e.preventDefault();
//		this.submit();
//		toastr.success('Thêm mới admin thành công');
//});
function validateForm() {
	var password;
	var confirm_password;
	password = document.getElementById("password").value;
	confirm_password = document.getElementById("confirm_password").value;
	if (password != confirm_password) {
		toastr.error('Mật khẩu không khớp');
	}
}
function checkphone(){
	var phone = document.getElementById("phone").value;
	if(!pt_phone.test(phone)){
		toastr.error('Số điện thoại không hợp lệ');
	}
}
$(function () {
    $("#username, #password, #confirm_password, #permission").bind("change keyup",
function () {      
  if ($("#username").val()  == "" || $("#password").val() == "" || $("#confirm_password").val() == "" || 
		  ($("#password").val() != $("#confirm_password").val()) || 
		  !(/^[A-Za-z0-9_-]{4,15}$/.test($("#username").val())) ||
		  $("#permission").val()== "---Loại tài khoản---")
	  $(this).closest("form").find(":submit").attr("disabled", "disabled");
  else 
	  $(this).closest("form").find(":submit").removeAttr("disabled");  
    });
  });