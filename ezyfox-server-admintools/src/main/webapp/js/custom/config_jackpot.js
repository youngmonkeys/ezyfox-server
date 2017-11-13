toastr.options = {
	        closeButton: false,
	        progressBar: false,
	        showMethod: 'slideDown',
	        timeOut: 1500
	  };
function ratioWin(id){
	var ratio = document.getElementById('win'+id).value;
	if(ratio <= 0 || ratio >=1) {
		toastr.error("Tỉ lệ giới hạn  từ 0 đến 1");
	}
}
function ratioLost(id){
	var ratio = document.getElementById('lost'+id).value;
	console.log(document.getElementById('lost'+id).value);
	if(ratio <=0 || ratio >=1) {
		toastr.error("Tỉ lệ giới hạn  từ 0 đến 1");
	}
}