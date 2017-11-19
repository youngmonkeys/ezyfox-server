var chartNetwork = 	null;

var containerThread = null
var maximumThread = null;
var plotThread = null;
var series = null;
var dataThread = [];

var containerNetwork = null
var maximumNetwork = null;
var plotNetwork = null;
var inputNetwork = [];
var outputNetwork = [];
var intervalSecond = 0;
window.onload = function () {
	
	initChartNetWork();
	initChartThread();

	getDataRealTime();

	setInterval(getDataRealTime, 3000);
	setInterval(getNetworkStats, 2000);
};

function getDataRealTime(){
	getThreadCount();
	getAllThread();
	getTrafficDetails();
}

function getThreadCount(){
	$.ajax({
		type: "GET",
		crossDomain: true,
		url: window.location + "/admin/threads/count",
		cache: false,
		dataType: "text",
		async: true,
		success: function (result) {
			$('#threadCount').text(result);
			if (dataThread.length) {
				dataThread = dataThread.slice(1);
			}
			while (dataThread.length < maximumThread) {
				dataThread.push(result);
			}
		
			var res = [];
			for (var i = 0; i < dataThread.length; ++i) {
				res.push([i, dataThread[i]])
			}
			series[0].data = res;
			plotThread.setData(series);
			plotThread.draw();
		}, error: function (err) {
			console.log("error get thread count:" + JSON.stringify(err));
		}
	});
}

function getAllThread(){
	$.ajax({
		type: "GET",
		crossDomain: true,
		url: window.location + "/admin/threads/all",
		cache: false,
		dataType: "text",
		async: true,
		success: function (result) {
			$('#body').empty();
			var data  = [];
			var ezyThreads =  JSON.parse(result);
			var totalCPUTimes = ezyThreads.totalThreadsCpuTime;
			var threads = ezyThreads.threads;
			for(var i in threads){
				var cpuTimes = (threads[i].cpuTime/totalCPUTimes)*100;
				data.push();
				$('#body').append("<tr><td class=\"col-xs-1\">" +threads[i].id +"</td>"+
								"<td class=\"col-xs-8\">" +threads[i].name +"</td>"+
								"<td class=\"col-xs-3\" id=\"cpu\">" +cpuTimes.toFixed(2) +"%</td>"+			
								"</tr>");
			}
			
		}, error: function (err) {
			console.log("error get threads :" + JSON.stringify(err));
		}
	});
}

function getTrafficDetails(){
	$.ajax({
		type: "GET",
		crossDomain: true,
		url: window.location + "/admin/traffic/details",
		cache: false,
		dataType: "text",
		async: true,
		success: function (result) {
			console.log("traffic details :"+JSON.parse(result));
			var trafficDetails = JSON.parse(result);
			$("#sessionTotal").text(trafficDetails.totalSession);
			$("#sessionSplit").text("Socket: " +trafficDetails.socketMaxSession+"| HTTP: "+trafficDetails.webSocketMaxSession);
			$("#sessionMax").text(trafficDetails.maxSession);
			$("#userTotal").text(trafficDetails.totalUser);
			$("#userMax").text(trafficDetails.maxUser);
			$("#transferData").text("Out: "+trafficDetails.transferredOutputData +" byte | In: "
									+(trafficDetails.transferredOutputData/1024).toFixed(2) +" byte");
			$("#currentData").text("Out: "+trafficDetails.currentOutputDataTransferRate +"byte/s | In: "
							+trafficDetails.currentInputDataTransferRate +"byte/s");
			$("#averageData").text("0");
			$("#droppedData").text("0");
			
		}, error: function (err) {
			console.log("error get traffic cause:" + JSON.stringify(err));
		}
	});
}
function getNetworkStats(){
	$.ajax({
		type: "GET",
		crossDomain: true,
		url: window.location + "/admin/network-stats/read-written-bytes-per-second",
		cache: false,
		dataType: "text",
		async: true,
		success: function (result) {
			console.log("network stats :"+JSON.stringify(result));
			intervalSecond += 2;
			$('#updated').text(secondsToHms(intervalSecond));
			var network = JSON.parse(result);
			if (inputNetwork.length) {
				inputNetwork = inputNetwork.slice(1);
				outputNetwork = outputNetwork.slice(1);
			}
			while (inputNetwork.length < maximumNetwork) {
				inputNetwork.push(network.inputBytes);
				outputNetwork.push(network.outputBytes);
			}
		
			var resIn = [];
			var resOut = [];
			for (var i = 0; i < inputNetwork.length; ++i) {
				resIn.push([i, inputNetwork[i]])
				resOut.push([i,outputNetwork[i]]);
			}
			var opts = plotNetwork.getOptions();
			opts.yaxes[0].min = 0;
			opts.yaxes[0].max = network.inputBytes > network.outputBytes ?
										 network.inputBytes + 100:network.outputBytes + 100;
			plotNetwork.setupGrid();
			plotNetwork.setData([resIn, resOut]);
			plotNetwork.draw();
		}, error: function (err) {
			console.log("error get network stats:" + JSON.stringify(err));
		}
	});
}
function showChartNetWork(input, output){
	console.log(input+"\n"+output)
	this.chartNetwork.flow({
			columns: [
				input,
				output
			]
	});
}
function initChartThread(){
	containerThread = $("#flot-line-chart-moving");
	maximumThread =  5;
	series = [{
		data: [0],
		lines: {
			fill: true
		}
	}];
	plotThread = $.plot(containerThread, series, {
		grid: {

			color: "#999999",
			tickColor: "#D4D4D4",
			borderWidth: 0,
			minBorderMargin: 20,
			labelMargin: 10,
			backgroundColor: {
				colors: ["#ffffff", "#ffffff"]
			},
			margin: {
				top: 8,
				bottom: 20,
				left: 20
			},
			markings: function (axes) {
				var markings = [];
				var xaxis = axes.xaxis;
				for (var x = Math.floor(xaxis.min); x < xaxis.max; x += xaxis.tickSize * 2) {
					markings.push({
						xaxis: {
							from: x,
							to: x + xaxis.tickSize
						},
						color: "#fff"
					});
				}
				return markings;
			}
		},
		colors: ["#1ab394"],
		xaxis: {
			tickFormatter: function () {
				return "";
			}
		},
		yaxis: {
			min: 0,
			max: 110
		},
		legend: {
			show: true
		},
		tooltip: true
	});

}
function initChartNetWork(){
	containerNetwork = $("#cpu-chart");
	maximumNetwork = 5;
	var options = {
		grid:{
			borderColor:'#ccc',
			markings: function (axes) {
				var markings = [];
				var xaxis = axes.xaxis;
				for (var x = Math.floor(xaxis.min); x < xaxis.max; x += xaxis.tickSize * 2) {
					markings.push({
						xaxis: {
							from: x,
							to: x + xaxis.tickSize
						},
						color: "#fff"
					});
				}
				return markings;
			}
		},
        series:{
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
        colors: ["#00CC00","#FF0000"],
		xaxis:{show:true,ticks: 5},
		yaxis:{min:0, max:500},
		legend: {
			show: false
		}
	  };
	  plotNetwork = $.plot($("#cpu-chart"), [ [],[] ], options);
}
function secondsToHms(d) {
    d = Number(d);
    var h = Math.floor(d / 3600);
    var m = Math.floor(d % 3600 / 60);
    var s = Math.floor(d % 3600 % 60);

    var hDisplay = h > 0 ? h + (h == 1 ? " hour, " : " hours, ") : "";
    var mDisplay = m > 0 ? m + (m == 1 ? " minute, " : " minutes, ") : "";
    var sDisplay = s > 0 ? s + (s == 1 ? " second" : " seconds") : "";
    return "Last update "+hDisplay + mDisplay + sDisplay; 
}

