function validateDescription(){
	toastr.options = {
	        closeButton: false,
	        progressBar: false,
	        showMethod: 'slideDown',
	        timeOut: 1500
	  };
	var description = document.getElementById("description").value;
	if(description.length < 6 ){
		toastr.error("Lý do phải lớn hơn 6 kí tự");
	}
}