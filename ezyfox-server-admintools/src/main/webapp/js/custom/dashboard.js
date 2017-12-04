var chartNetwork = null;

var containerThread = null
var maximumThread = null;
var plotThread = null;
var series = null;
var dataThread = [];

var containerCPU = null
var maximumCPU = null;
var plotCPU = null;
var seriesCPU = null;
var dataCPU = [];

var containerNetwork = null
var maximumNetwork = null;
var plotNetwork = null;
var inputNetwork = [];
var outputNetwork = [];
var intervalSecond = 0;

var containerMemory = null
var maximumMemory = null;
var plotMemory = null;
var seriesMemory = null;
var allocatedMemory = [];
var usedMemory = [];
var dataMemory = [];

var interval = 2000;
var intervalGetChart = null;
window.onload = function () {

	initChartNetWork();
	initChartThread();
	initChartMemory();
	initChartCPUusage();

	getAllThread();
	getTrafficDetails();
	
	//default interval is 3sec
	setInterval(getAllThread, 3000);
	setInterval(getTrafficDetails, 3000);
	
	//get data realtime by interval
	intervalGetChart = setInterval(getChartRealtime, interval);
};

$('#interval').change(function() {
	interval = $("#interval").val();
	if(intervalGetChart)  clearInterval(intervalGetChart);
	intervalGetChart = setInterval(getChartRealtime, interval);
})

function getChartRealtime(){
	console.log("update after "+interval+"ms");
	getNetworkStats();
	getMemoryDetail();
	getCPUusage();
	getThreadCount();
}

function getThreadCount() {
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
			var opts = plotThread.getOptions();
			opts.yaxes[0].min = 0;
			opts.yaxes[0].max = result * 2;
			plotThread.setupGrid();
			series[0].data = res;
			plotThread.setData(series);
			plotThread.draw();
		}, error: function (err) {
			console.log("error get thread count:" + JSON.stringify(err));
		}
	});
}
function getCPUusage() {
	$.ajax({
		type: "GET",
		crossDomain: true,
		url: window.location + "/admin/cpu/details",
		cache: false,
		dataType: "text",
		async: true,
		success: function (result) {
			var cpu = JSON.parse(result);
			var systemCpuLoad = (parseFloat(cpu.systemCpuLoad) * 100).toFixed(2);
			var processCpuLoad = (parseFloat(cpu.processCpuLoad) * 100).toFixed(2);
			console.log("systemCpuLoad:" + systemCpuLoad + ",processCpuLoad:" + processCpuLoad)
			$('#cpu').text(systemCpuLoad + "%");
			if (dataCPU.length) {
				dataCPU = dataCPU.slice(1);
			}
			while (dataCPU.length < maximumCPU) {
				dataCPU.push(processCpuLoad);
			}

			var res = [];
			for (var i = 0; i < dataCPU.length; ++i) {
				res.push([i, dataCPU[i]])
			}
			plotCPU.setupGrid();
			seriesCPU[0].data = res;
			plotCPU.setData(seriesCPU);
			plotCPU.draw();
		}, error: function (err) {
			console.log("error get cpu:" + JSON.stringify(err));
		}
	});
}

function getMemoryDetail() {
	$.ajax({
		type: "GET",
		crossDomain: true,
		url: window.location + "/admin/memory/details",
		cache: false,
		dataType: "text",
		async: true,
		success: function (result) {
			var memory = JSON.parse(result);
			var allocate = Math.round((memory.allocatedMemory / 1024) / 1024);
			var used = Math.round(memory.usedMemory / 1024 / 1024);
			$('#max').text("Max " + (((memory.maxMemory / 1024) / 1024) / 1024).toFixed(2) + " GB");
			$('#free').text("Free " + ((memory.freeMemory / 1024) / 1024).toFixed(2) + " MB");
			if (allocatedMemory.length) {
				allocatedMemory = allocatedMemory.slice(1);
				usedMemory = usedMemory.slice(1);
			}
			while (allocatedMemory.length < maximumMemory) {
				allocatedMemory.push(allocate);
				usedMemory.push(used);
			}

			var res1 = [];
			var res2 = [];
			for (var i = 0; i < allocatedMemory.length; ++i) {
				res1.push([i, allocatedMemory[i]]);
				res2.push([i, usedMemory[i]]);
			}
			var opts = plotMemory.getOptions();
			opts.yaxes[0].min = 0;
			var m = Math.abs(allocate - used);
			opts.yaxes[0].max = allocate > used ? allocate + 100 : used + 100;
			plotMemory.setupGrid();
			seriesMemory[0].data = res1;
			seriesMemory[1].data = res2;
			plotMemory.setData(seriesMemory);
			plotMemory.draw();
			console.log("Allocated = " + allocate + ", used =" + used);
		}, error: function (err) {
			console.log("error get memroy detail :" + JSON.stringify(err));
		}
	});
}

function getAllThread() {
	$.ajax({
		type: "GET",
		crossDomain: true,
		url: window.location + "/admin/threads/all",
		cache: false,
		dataType: "text",
		async: true,
		success: function (result) {
			$('#body').empty();
			var data = [];
			var ezyThreads = JSON.parse(result);
			var totalCPUTimes = ezyThreads.totalThreadsCpuTime;
			var threads = ezyThreads.threads;
			for (var i in threads) {
				var cpuTimes = (threads[i].cpuTime / totalCPUTimes) * 100;
				data.push();
				$('#body').append("<tr><td class=\"col-xs-1\">" + threads[i].id + "</td>" +
					"<td class=\"col-xs-8\">" + threads[i].name + "</td>" +
					"<td class=\"col-xs-3\" id=\"cpu\">" + cpuTimes.toFixed(2) + "%</td>" +
					"</tr>");
			}

		}, error: function (err) {
			console.log("error get threads :" + JSON.stringify(err));
		}
	});
}

function getTrafficDetails() {
	$.ajax({
		type: "GET",
		crossDomain: true,
		url: window.location + "/admin/traffic/details",
		cache: false,
		dataType: "text",
		async: true,
		success: function (result) {
			console.log("traffic details :" + JSON.parse(result));
			var trafficDetails = JSON.parse(result);
			$("#sessionTotal").text(trafficDetails.totalSession);
			$("#sessionSplit").text("Socket: " + trafficDetails.socketMaxSession + "| HTTP: " + trafficDetails.webSocketMaxSession);
			$("#sessionMax").text(trafficDetails.maxSession);
			$("#userTotal").text(trafficDetails.totalUser);
			$("#userMax").text(trafficDetails.maxUser);
			$("#transferData").text("Out: " + bytesToSize(trafficDetails.transferredOutputData) + "| In: "
				+ bytesToSize(trafficDetails.transferredOutputData));
			$("#currentData").text("Out: " + bytesToSize(trafficDetails.currentOutputDataTransferRate) + "/s | In: "
				+ bytesToSize(trafficDetails.currentInputDataTransferRate) + "/s");
			$("#averageData").text("0");
			$("#droppedData").text("0");

		}, error: function (err) {
			console.log("error get traffic cause:" + JSON.stringify(err));
		}
	});
}
function getNetworkStats() {
	$.ajax({
		type: "GET",
		crossDomain: true,
		url: window.location + "/admin/network-stats/read-written-bytes-per-second",
		cache: false,
		dataType: "text",
		async: true,
		success: function (result) {
			intervalSecond += interval/1000;
			$('#updatedNetwork').text(secondsToHms(intervalSecond));
			var network = JSON.parse(result);
			var inputRound = (network.inputBytes / 1024).toFixed(1);
			var outputRound = (network.outputBytes / 1024).toFixed(1);
			console.log("input :" + inputRound + ",output:" + outputRound);
			if (inputNetwork.length) {
				inputNetwork = inputNetwork.slice(1);
				outputNetwork = outputNetwork.slice(1);
			}
			while (inputNetwork.length < maximumNetwork) {
				inputNetwork.push(inputRound);
				outputNetwork.push(outputRound);
			}

			var resIn = [];
			var resOut = [];
			for (var i = 0; i < inputNetwork.length; ++i) {
				resIn.push([i, inputNetwork[i]])
				resOut.push([i, outputNetwork[i]]);
			}
			var opts = plotNetwork.getOptions();

			var between = Math.round(Math.abs(inputRound - outputRound));
			opts.yaxes[0].min = 0;
			opts.yaxes[0].max = inputRound > outputRound ?
				Math.round(inputNetwork) + between : Math.round(outputRound) + between;
			plotNetwork.setupGrid();
			plotNetwork.setData([resIn, resOut]);
			plotNetwork.draw();
		}, error: function (err) {
			console.log("error get network stats:" + JSON.stringify(err));
		}
	});
}
function initChartMemory() {
	containerMemory = $("#memoryChart");
	maximumMemory = 5;
	seriesMemory = [{
		data: [0],
		lines: {
			show: true,
			fillColor: { colors: [{ opacity: 0.7 }, { opacity: 0.1 }] },
			fill: true
		},
		points: { show: true }
	},
	{
		data: [0],
		lines: {
			show: true,
			fillColor: { colors: [{ opacity: 0.7 }, { opacity: 0.1 }] },
			fill: true
		},
		points: { show: true }
	}];
	plotMemory = $.plot(containerMemory, seriesMemory, {
		grid: {
			hoverable: true,
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
		colors: ["#3399CC", "#0033CC"],
		xaxis: {
			tickFormatter: function () {
				return "";
			}
		},
		yaxis: {
			min: 0,
			max: 100,
			tickFormatter: function (val, axis) {
				return val + " MB";
			}
		},
		legend: {
			show: true
		}
	});
	hoverFlotChart("memoryChart", " MB");

}
function initChartThread() {
	containerThread = $("#threadChart");
	maximumThread = 5;
	series = [{
		data: [0],
		lines: {
			fill: true,
			show: true
		},
		points: { show: true }
	}];
	plotThread = $.plot(containerThread, series, {
		grid: {
			hoverable: true,
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
		}
	});
	hoverFlotChart("threadChart", "");

}
function initChartCPUusage() {
	containerCPU = $("#cpuUsage");
	maximumCPU = 5;
	seriesCPU = [{
		data: [0],
		lines: {
			fill: true,
			show: true
		},
		points: { show: true }
	}];
	plotCPU = $.plot(containerCPU, seriesCPU, {
		grid: {
			hoverable: true,
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
			max: 100,
			tickFormatter: function (yVal) {
				return yVal.toFixed(2) + "%";
			}
		},
		legend: {
			show: true
		}
	});
	hoverFlotChart("cpuUsage", "%")

}
function initChartNetWork() {
	containerNetwork = $("#networkTraffic");
	maximumNetwork = 5;
	var options = {
		grid: {
			hoverable: true,
			borderColor: '#ccc',
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
			},
			points: { show: true }
		},
		colors: ["#00CC00", "#FF0000"],
		xaxis: { show: false, ticks: 5 },
		yaxis: {
			min: 0, max: 500,
			tickFormatter: function (val, axis) {
				return val + " KB/s";
			}
		},
		legend: {
			show: false
		}
	};
	plotNetwork = $.plot($("#networkTraffic"), [[], []], options);
	hoverFlotChart("networkTraffic", " KB/s");
}
function showTooltip(x, y, color, contents) {
	$('<div id="tooltip">' + contents + '</div>').css({
		position: 'absolute',
		display: 'none',
		top: y - 50,
		left: x - 20,
		border: '2px solid ' + color,
		padding: '5px',
		'font-size': '9px',
		'border-radius': '3px',
		'background-color': '#fff',
		'font-family': 'Verdana, Arial, Helvetica, Tahoma, sans-serif',
		opacity: 0.9
	}).appendTo("body").fadeIn(10);
}
function secondsToHms(d) {
	d = Number(d);
	var h = Math.floor(d / 3600);
	var m = Math.floor(d % 3600 / 60);
	var s = Math.floor(d % 3600 % 60);

	var hDisplay = h > 0 ? h + (h == 1 ? " hour, " : " hours, ") : "";
	var mDisplay = m > 0 ? m + (m == 1 ? " minute, " : " minutes, ") : "";
	var sDisplay = s > 0 ? s + (s == 1 ? " second" : " seconds") : "";
	return "Last " + hDisplay + mDisplay + sDisplay;
}

function bytesToSize(bytes) {
	var sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
	if (bytes == 0) return '0 Byte';
	var i = parseInt(Math.floor(Math.log(bytes) / Math.log(1024)));
	return Math.round(bytes / Math.pow(1024, i), 2) + ' ' + sizes[i];
};
function hoverFlotChart(idChart, unit) {
	var previousPoint = null, previousLabel = null;
	$("#" + idChart).bind("plothover", function (event, pos, item) {
		if (item) {
			if ((previousLabel != item.series.label) || (previousPoint != item.dataIndex)) {
				previousPoint = item.dataIndex;
				previousLabel = item.series.label;
				$("#tooltip").remove();
				var x = item.datapoint[0];
				var y = item.datapoint[1];

				var color = item.series.color;
				showTooltip(item.pageX, item.pageY, color, "<small>" + y + unit + "</small> ");
			} else {
				$("#tooltip").remove();
				previousPoint = null;
			}

		}
	});
}
