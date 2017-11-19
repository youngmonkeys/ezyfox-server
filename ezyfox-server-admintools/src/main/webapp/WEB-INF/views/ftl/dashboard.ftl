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
								<div class="col-sm-8" id="title">
									<h5>Global status</h5>
								</div>
								<div class="col-sm-2" id="title">
									<h5>Interval</h5>
								</div>
								<div class="col-sm-2" id="interval">
									<select name="interval">
										<option value="2">2 sec</option>
										<option value="4">4 sec</option>
										<option value="6">6 sec</option>
									</select>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-6">
						<div class="ibox float-e-margins">
							<div class="ibox-title">
								<h5>Network traffic</h5>
								<div class="pull-right" id="note">  Outgoing  <i class="fa fa-square" style="color:#FF0000"></i></div>
								<div class="pull-right" id="note">  Incoming  <i class="fa fa-square" style="color:#00CC00"></i></div>
							</div>
							<div class="ibox-content">
								<div class="flot-chart" style="height: 200px !important">
									<div class="flot-chart-content" id="cpu-chart"></div>
									<span style="text-align: center" id="updated"></span>
								</div>
							</div>
						</div>
					</div>
					<div class="col-lg-6">
						<div class="ibox float-e-margins">
							<div class="ibox-title">
								<span class="label label-danger pull-right" id="threadCount"></span>
								<h5>Thread count</h5>
							</div>
							<div class="ibox-content">
								<div class="flot-chart">
									<div class="flot-chart-content" id="flot-line-chart-moving"></div>
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
				<!-- d3 and c3 charts -->
				<script src="js/plugins/d3/d3.min.js"></script>
				<script src="js/plugins/c3/c3.min.js"></script>
</body>

</html>