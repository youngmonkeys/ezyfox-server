<!DOCTYPE html>
<html>

<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">

	<title>Ezy Admintool</title>

	<link href="css/bootstrap.min.css" rel="stylesheet">
	<link href="font-awesome/css/font-awesome.css" rel="stylesheet">

	<link href="css/animate.css" rel="stylesheet">
	<link href="css/style.css" rel="stylesheet">
	<!-- Custom -->
	<link href="css/custom/dashboard.css" rel="stylesheet">
	<!-- c3 Charts -->
	<link href="css/plugins/c3/c3.min.css" rel="stylesheet">
	<style>
		#title {
			padding: 0px;
		}
	</style>
</head>

<body>

	<div id="wrapper">
		<#include "shared/navigation.ftl">
			<div id="page-wrapper" class="gray-bg">
				<div class="row border-bottom">
					<nav class="navbar navbar-static-top white-bg" role="navigation" style="margin-bottom: 0">
						<div class="navbar-header">
							<a class="navbar-minimalize minimalize-styl-2 btn btn-primary " href="#">
								<i class="fa fa-bars"></i>
							</a>
							<form role="search" class="navbar-form-custom" method="post" action="#">
								<div class="form-group"></div>
							</form>
						</div>
						<ul class="nav navbar-top-links navbar-right">
							<li>
								<a href="#">
									<i class="fa fa-sign-out"></i>Logout</a>
							</li>
						</ul>
					</nav>
				</div>
				<div class="row">
					<div class="col-lg-12">
						<div class="ibox float-e-margins">
							<div class="ibox-title">
								<div class="row m-b-sm m-t-sm">
									<div class="col-md-3">
										<h5>Global server status</h5>
									</div>
									<div class="col-md-8" style="text-align:right;font-size: 12px">
										Interval
									</div>
									<div class="col-md-1">
										<select id="interval" name="interval">
											<option value="2000" selected>2 sec</option>
											<option value="4000">4 sec</option>
											<option value="6000">6 sec</option>
										</select>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-6">
						<div class="ibox float-e-margins">
							<div class="ibox-title">
								<span class="label label-danger pull-right" id="cpu">0.00%</span>
								<h5>CPUs usage</h5>
							</div>
							<div class="ibox-content">
								<div class="flot-chart">
									<div class="flot-chart-content" id="cpuUsage"></div>
								</div>
								<hr>
								<div class="row m-b-sm m-t-sm">
									<div class="col-md-3">
										<h5>Memory usaged</h5>
									</div>
									<div class="col-md-2">
										<small id="max">Max 0 GB</small>
									</div>
									<div class="col-md-3">
										<small id="free">Free 0 MB</small>
									</div>
									<div class="col-md-2">
										<small>Allocated
											<i class="fa fa-square" style="color:#3399CC"></i>
										</small>
									</div>
									<div class="col-md-2">
										<small>Used
											<i class="fa fa-square" style="color:#0033CC"></i>
										</small>
									</div>
								</div>
								<div class="flot-chart" style="height: 200px !important;text-align:center">
									<div class="flot-chart-content" id="memoryChart" style="margin-top: 20px"></div>
								</div>
								<hr>
								<div class="row m-b-sm m-t-sm" style="margin-top: 20px">
									<div class="col-md-3">
										<h5>Network traffic</h5>
									</div>
									<div class="col-md-2">
										<small id="max"></small>
									</div>
									<div class="col-md-3">
										<small id="free"></small>
									</div>
									<div class="col-md-2">
										<small>Incoming
											<i class="fa fa-square" style="color:#00CC00"></i>
										</small>
									</div>
									<div class="col-md-2">
										<small>Outgoing
											<i class="fa fa-square" style="color:#FF0000"></i>
										</small>
									</div>
								</div>
								<div class="flot-chart" style="height: 200px !important;text-align:center">
									<div class="flot-chart-content" id="networkTraffic"></div>
									<small style="text-align: center" id="updatedNetwork"></small>
								</div>
							</div>
						</div>
					</div>
					<div class="col-lg-6">
						<div class="ibox float-e-margins">
							<div class="ibox-title">
								<span class="label label-danger pull-right" id="threadCount">0</span>
								<h5>Thread count</h5>
							</div>
							<div class="ibox-content">
								<div class="flot-chart">
									<div class="flot-chart-content" id="threadChart"></div>
								</div>
								<div>
									<div class="panel panel-default">
										<div class="panel-heading">
											<h4>
												Active threads
											</h4>
										</div>
										<table class="table table-fixed">
											<thead>
												<tr>
													<th class="col-xs-1">Id</th>
													<th class="col-xs-8">Name</th>
													<th class="col-xs-3">CPU time</th>
												</tr>
											</thead>
											<tbody id="body">

											</tbody>
										</table>
									</div>

								</div>
								<!-- Traffic -->
								<div>
									<div class="panel panel-default">
										<div class="panel-heading">
											<h4>
												Traffic detail
											</h4>
										</div>
										<table class="table">
											<tbody style="overflow-y: none !important">
												<tr>
													<td class="col-xs-5" id="traffic-title">Sessions (total)</td>
													<td class="col-xs-7" id="sessionTotal">0</td>
												</tr>
												<tr>
													<td class="col-xs-5" id="traffic-title">Sessions (split)</td>
													<td class="col-xs-7" id="sessionSplit">Socket:0 | HTTP: 0</td>
												</tr>
												<tr>
													<td class="col-xs-5" id="traffic-title">Sessions (maximum)</td>
													<td class="col-xs-7" id="sessionMax">0</td>
												</tr>
												<tr>
													<td class="col-xs-5" id="traffic-title">Users (total)</td>
													<td class="col-xs-7" id="userTotal">0</td>
												</tr>
												<tr>
													<td class="col-xs-5" id="traffic-title">Users (maximum)</td>
													<td class="col-xs-7" id="userMax">0</td>
												</tr>
												<tr>
													<td class="col-xs-5" id="traffic-title">Transferred data</td>
													<td class="col-xs-7" id="transferData">Out: 0 Kb | In: 0 Kb</td>
												</tr>
												<tr>
													<td class="col-xs-5" id="traffic-title">Current data transfer rate</td>
													<td class="col-xs-7" id="currentData">Out: 0 KB/s | In: 0 Kb/s</td>
												</tr>
												<tr>
													<td class="col-xs-5" id="traffic-title">Average data transfer rate</td>
													<td class="col-xs-7" id="averageData">Out: 0 KB/s | In: 0 Kb/s</td>
												</tr>
												<tr>
													<td class="col-xs-5" id="traffic-title">Dropped packets</td>
													<td class="col-xs-7" id="droppedData">Out: 0 (0%) | In: 0 (0%)</td>
												</tr>
											</tbody>
										</table>
									</div>

								</div>
							</div>
						</div>
					</div>
					<#include "shared/footer.ftl">
				</div>

				<!-- Mainly scripts -->
				<script src="js/jquery-3.1.1.min.js"></script>
				<script src="js/bootstrap.min.js"></script>
				<script src="js/plugins/metisMenu/jquery.metisMenu.js"></script>
				<script src="js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
				<!-- Custom and plugin javascript -->
				<script src="js/inspinia.js"></script>
				<script src="js/plugins/pace/pace.min.js"></script>
				<script src="js/custom/dashboard.js"></script>
				<!-- ChartJs -->
				<script src="js/plugins/chartJs/Chart.min.js"></script>
				<!-- Flot -->
				<script src="js/plugins/flot/jquery.flot.js"></script>
				<script src="js/plugins/flot/jquery.flot.tooltip.min.js"></script>
				<script src="js/plugins/flot/jquery.flot.resize.js"></script>
				<script src="js/plugins/flot/jquery.flot.pie.js"></script>
				<script src="js/plugins/flot/jquery.flot.time.js"></script>
</body>

</html>