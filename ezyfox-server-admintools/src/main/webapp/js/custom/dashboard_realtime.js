window.onload = function(){
	var accessToken = document.getElementById('accessToken').value;
	realtime(accessToken);
	getChartPlayer(accessToken);
	setInterval(realtime, 3000);
};
	function realtime(accessToken){
	getBank(accessToken);
	getCCU(accessToken);
	totalLastLogin(accessToken);
	getBankCoin(accessToken);
	countnewUser(accessToken);
//	getChartPlayer(accessToken);
	getChartRevenue(accessToken);
	getBankHistory(accessToken);
	}
	function getBank(accessToken) {
		$.ajax({
			type : "GET", 
			url : "api/jackpot_get?accessToken=" + accessToken,
			dataType : "json",
			async: true,
			success : function(result) {
				var i = 0;
				var banks = result["banks"];
				for(var key in banks) {
					if (banks.hasOwnProperty(key)) {
						document.getElementById('bank' + (++i)).innerHTML = "Mức cược " + formatMoney(key) + ": " +  formatMoney(banks[key]);
					}
				}
			}
		});
	}
	function getCCU(accessToken) {
		$.ajax({
			type : "GET", 
			url : "api/ccu_get?accessToken=" + accessToken,
			dataType : "json",
			async: true,
			success : function(result) {
				console.log(result);
				document.getElementById('ccu').innerHTML   = result['ccuCount'];
			}
		});
		
	}
	function totalLastLogin(accessToken) {
		$.ajax({
			type : "GET", 
			url : "api/total_last_login?accessToken=" + accessToken,
			dataType : "json",
			async: true,
			success : function(result) {
				console.log(result);
				document.getElementById('totalLogin').innerHTML   = result['totalLogins'];
			}
		});
		
	}
	function getBankCoin(accessToken) {
		$.ajax({
			type : "GET", 
			url : "api/game_bank_coin?accessToken=" + accessToken,
			dataType : "json",
			async: true,
			success : function(result) {
				console.log(result);
				document.getElementById('gamebank').innerHTML   = formatMoney(result['bankCoin']);
			}
		});
		
	}
	function countnewUser(accessToken){
	$.ajax({
		type : "GET", 
		url : "api/count_new_user?accessToken=" + accessToken,
		dataType : "json",
		async: true,
		success : function(result) {
			document.getElementById('newUser').innerHTML   = result['countNewUser'] +" tài khoản mới";
		}
	});
	}
	
	function cohortPlayer(){
		var keenClient = new Keen({
		    projectId: '59a513d8c9e77c0001bc205e',
		    readKey: '18976A32FB11C037B2BAD3E524A9E824E3BDAC11935DB2CD32B797E48397CCDBDD7FEC93322CFCAFBB16F268B60928F20B79F1BC6BA5066DE4DB3D72C30350D620222077C47172FC1C50D7741D0BD20C9BBF7EF184A9FBFFE01563FC1D1DE7D0'
		  });

		  var cohortChart = new Keen.Dataviz()
		    .el('#new-chart-wrapper')
		    .height(280)
		    .type('line');

		  var cohortTable = new Keen.Dataviz()
		    .el('#new-table-wrapper')
		    .library('cohort-builder')
		    .height(280)
		    .type('matrix');
		    cohortChart.prepare();
		    cohortTable.prepare();

		    // Intervals input field
		    var intervals = 5;

		    // Units select field

		    var dateMatrix = Keen.CohortBuilder.generateDateMatrix("weeks", 7);

		    var queryMatrix = Keen.CohortBuilder.generateCohortQueryMatrix(dateMatrix, function(cohort){
		      return new Keen.Query('funnel', {
		        steps: [
		          {
		            event_collection: 'first_launch',
		            actor_property: 'dpid',
		            filters: [
		              { property_name: 'make', operator: 'eq', property_value: 'Apple' }
		            ],
		            timeframe: cohort.created_at
		          },
		          {
		            event_collection: 'application_opened',
		            actor_property: 'dpid',
		            filters: [
		              { property_name: 'make', operator: 'eq', property_value: 'Apple' }
		            ],
		            timeframe: cohort.current_interval
		          }
		        ]
		      });
		    });

		    Keen.CohortBuilder.fetchCohortDatasets(keenClient, queryMatrix, function(dataset) {
		      cohortChart
		        .data(dataset)
		        .render();

		      cohortTable
		        .data(dataset)
		        .render();
		    });
	}
	function getChartPlayer(accessToken){
		$.ajax({
			type : "GET", 
			url : "api/get_chart_player?accessToken=" + accessToken,
			dataType : "json",
			async: true,
			success : function(result) {
				result = result["chartPlayer"];
				var charts = [];
				for (var key in result){
					var entry = [];
					entry.push(new Date(key),result[key]);
					charts.push(entry);
				}
				charts.sort(function(a,b){
					  return a[0] - b[0];
					});
				var barOptions = {
				        series: {
				            lines: {
				                show: true,
				                lineWidth: 2,
				                fill: true,
				                fillColor: {
				                    colors: [{
				                        opacity: 0.0
				                    }, {
				                        opacity: 0.0
				                    }]
				                }
				            }
				        },
				        xaxis: {
				            mode: "time",
				            timeformat: "%d-%m",
				            ticks: 5
				         },
				        colors: ["##ff80bf"],
				        grid: {
				            color: "#999999",
				            hoverable: true,
				            clickable: true,
				            tickColor: "#D4D4D4",
				            borderWidth:0
				        },
				        legend: {
				            show: false
				        },
				        tooltip: true,
				        tooltipOpts: {
				            content: "Ngày: %x, Giá trị: %y"
				        }
				    };
				    var playerData = {
				        label: "Player-Chart",
				        data: [
				        	charts[0],
				        	charts[1],
				        	charts[2],
				        	charts[3],
				        	charts[4]
				        ]
				    
				    };
				    $.plot($("#flot-line-chart-player"), [playerData], barOptions);
			}
		});
	}
	function getChartRevenue(accessToken) {
		$.ajax({
			type : "GET", 
			url : "api/get_revenue_history?accessToken=" + accessToken,
			dataType : "json",
			async: true,
			success : function(result) {
				
				var revenue = result["revenueHistory"].revenue;
				var bonus = result["revenueHistory"].bonus;
				
				var dataRevenue = [];
				var dataBonus = [];
				pushObjectToArray(revenue,dataRevenue);
				pushObjectToArray(bonus,dataBonus);
				console.log("REVENUE COUNT:"+dataRevenue.length);
				console.log("BONUS COUNT:"+dataBonus.length);
				if(dataRevenue.length == 5 && dataBonus.length==4){
					var entry = [];
					entry.push(dataRevenue[4][0],0);
					dataBonus.push(entry);
				} else if(dataRevenue.length == 4 && dataBonus.length==5){
					var entry = [];
					entry.push(dataBonus[4][0],0);
					dataRevenue.push(entry);
				}
				console.log(dataRevenue);
				console.log(dataBonus);
				var barOptions = {
				        series: {
				            lines: {
				                show: true,
				                lineWidth: 2,
				                fill: true,
				                fillColor: {
				                    colors: [{
				                        opacity: 0.0
				                    }, {
				                        opacity: 0.0
				                    }]
				                }
				            }
				        },
				        xaxis: {
				            mode: "time",
				            timeformat: "%d-%m",
				            ticks: 5
				         },
				        colors: ["#ff80bf","#cc6666"],
				        grid: {
				            color: "#999999",
				            hoverable: true,
				            clickable: true,
				            tickColor: "#D4D4D4",
				            borderWidth:0
				        },
				        legend: {
				            show: false
				        },
				        tooltip: true,
				        tooltipOpts: {
				            content: "Ngày: %x, Giá trị: %y"
				        }
				    };
				    var revenue = {
				        label: "revenue",
				        data: [
				        	dataRevenue[0],
				        	dataRevenue[1],
				        	dataRevenue[2],
				        	dataRevenue[3],
				        	dataRevenue[4]
				        ]
				    
				    };
				    var bonus = {
					        label: "bonus",
					        data: [
					        	dataBonus[0],
					        	dataBonus[1],
					        	dataBonus[2],
					        	dataBonus[3],
					        	dataBonus[4]
					        ]
					    
					    };
				    $.plot($("#flot-line-chart-revenue"), [revenue,bonus], barOptions);
			}
		});
	}
	function getBankHistory(accessToken){
		$.ajax({
			type : "GET", 
			url : "api/get_bank_history?accessToken=" + accessToken,
			dataType : "json",
			async: true,
			success : function(result) {
				console.log("PERCENT:"+result['percentWith5DaysAgo']);
				document.getElementById('bankHistory').innerHTML   = result['percentWith5DaysAgo']+"% so với 5 ngày trước"
			}
		});
	}
	function formatMoney(money){
		var signed = money < 0;
	    var ret = Math.abs(money).toString();
	    for (var i = ret.length - 3; i > 0; i -= 3) {
	        ret = ret.substr(0, i) + "," + ret.substr(i);
	    }
	    signed && (ret = "-" + ret);
	    return ret;
	}
	function pushObjectToArray(object,array){
		for (var key in object){
			var entry = [];
			entry.push(new Date(object[key]._id),object[key].total);
			array.push(entry);
		}
		array.sort(function(a,b){
		  return a[0] - b[0];
		});
	}
	