window.onload = function(){
	var accessToken = document.getElementById('accessToken').value;
	getChartReportPlayer(accessToken);
}
function getChartReportPlayer(accessToken) {
	var e = document.getElementById('filter');
	var filter = e.options[e.selectedIndex].value;
	console.log("FIL"+filter)
	$("#chart").replaceWith("<div id=\"chart\" class=\"flot-chart\" ><div class=\"flot-chart-content\" id=\"morris-area-chart\"></div></div>");
	$.ajax({
		type : "GET", 
		url : "api/get_chart_report_player?accessToken=" + accessToken+"&filter="+filter,
		dataType : "json",
		async: true,
		success : function(result) {
			var respone = result['result'];
			var data = []; 
			for(var key in respone){
				data.push({day:key,count:respone[key]});
			}
			Morris.Area({
	        element: 'morris-area-chart',
	        data: data,
	        xkey: 'day',
	        ykeys: ['count'],
	        labels: ['count'],
	        pointSize: 2,
	        hideHover: 'auto',
	        resize: true,
	        lineColors: ['#87d6c6', '#54cdb4','#1ab394'],
	        lineWidth:2,
	        pointSize:1,
	    });
		}
	});
}

