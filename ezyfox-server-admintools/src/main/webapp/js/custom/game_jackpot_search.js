function searchGameJackpot() {
	var accessToken = document.getElementById('accessToken').value;
	var e = document.getElementById('room');
	var room = e.options[e.selectedIndex].value;
	var date = document.getElementById('daterange').value;
	var from = date.substr(0, date.indexOf('#'));
	var to = date.substr(date.indexOf('#') + 1);
	$.ajax({
		type : "GET", 
		url : "api/search_game_jackpot?accessToken="+ 
		accessToken+"&room="+room+"&from="+from+"&to="+to,
		dataType : "json",
		async: true,
		success : function(result) {
			var list = result['list'];
			var body = "<tbody>";
			for(var key in list){
				body+="<tr class=\"gradeA\"><td>"+key+"</td><td>"+list[key].day+"</td><td>"+list[key].accountName+"</td><td>"+list[key].totalSet+"</td><td>"+list[key].room+"</td><td>"+list[key].jackpotValue+"</td><td>"+list[key].jackpotTime+"</td><td><a href=\"#\">Chi tiết</a></td></tr>"
			}
			$("#body").replaceWith(body+"</tbody>");
		}
	});
}