window.onload = function() {
	var accessToken = document.getElementById('accessToken').value;
	toastr.options = {
		closeButton : false,
		progressBar : false,
		showMethod : 'slideDown',
		timeOut : 1500
	};
	getChartJackpot(accessToken);
}
function getChartJackpot(accessToken) {
	var e = document.getElementById('room');
	var room = e.options[e.selectedIndex].value;
	var f = document.getElementById('date');
	var date = f.options[f.selectedIndex].value;
	$("#chart")
			.replaceWith(
					"<div id=\"chart\" class=\"flot-chart\" ><div class=\"flot-chart-content\" id=\"morris-area-chart\"></div></div>");
	$.ajax({
		type : "GET",
		url : "api/get_chart_report_jackpot?accessToken=" + accessToken
				+ "&date=" + date + "&room=" + room,
		dataType : "json",
		async : true,
		success : function(result) {
			var respone = result['dataChart'];
			var data = [];
			for ( var key in respone) {
				data.push({
					day : key,
					count : respone[key]
				});
			}
			Morris.Area({
				element : 'morris-area-chart',
				data : data,
				xkey : 'day',
				ykeys : [ 'count' ],
				labels : [ 'Giá trị' ],
				pointSize : 2,
				hideHover : 'auto',
				resize : true,
				lineColors : [ '#87d6c6', '#54cdb4', '#1ab394' ],
				lineWidth : 2,
				pointSize : 1,
			});
		}
	});
}