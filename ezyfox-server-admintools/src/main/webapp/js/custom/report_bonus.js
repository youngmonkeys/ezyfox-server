window.onload = function(){
	var accessToken = document.getElementById('accessToken').value;
	getChartBonus(accessToken);
}
function getChartBonus(accessToken){
	var date = document.getElementById('daterange').value;
	var e = document.getElementById('room');
	var room = e.options[e.selectedIndex].value;
	var from = date.substr(0, date.indexOf('#'));
	var to = date.substr(date.indexOf('#') + 1);
	console.log("DATE:"+from+"and "+to+"ROOM:"+room);
	$("#chart").replaceWith("<div id=\"chart\" class=\"flot-chart\" ><div class=\"flot-chart-content\" id=\"morris-area-chart\"></div></div>");
	$.ajax({
		type : "GET", 
		url : "api/get_chart_report_bonus?accessToken="+ accessToken+"&from="+from+"&to="+to+"&room="+room,
		dataType : "json",
		async: true,
		success : function(result) {
			var respone = result['dataChart'];
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