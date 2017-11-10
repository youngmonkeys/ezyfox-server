<!DOCTYPE html>
<#include "shared/navigator.ftl">
<html>

<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Ezyfox Admintools</title>

<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="font-awesome/css/font-awesome.css" rel="stylesheet">
 <!-- <link rel='stylesheet' type='text/css' href='https://fonts.googleapis.com/css?family=Raleway' /> -->
 
<link href="css/animate.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet">
 <link href="https://d26b395fwzu5fz.cloudfront.net/keen-dataviz-1.0.4.css" rel="stylesheet" />
 <link rel="stylesheet" href="css/plugins/kenio/keen-static.css" />
 <link rel="stylesheet" href="css/plugins/kenio/keen-cohort-builder.css">
<style type="text/css">
	h4{
		text-align: center;
	}
	#h5{
		border: 1px solid black;
		padding: 10px
	}
	#right{
		background: transparent;
		color: black;
		font-size: 2.5rem;
	}
</style>
</head>

<body>

	<div id="wrapper">
		<#include "shared/navigation.ftl">
		<div id="page-wrapper" class="gray-bg">
			<div class="row border-bottom">
				<nav class="navbar navbar-static-top white-bg" role="navigation"
					style="margin-bottom: 0">
					<div class="navbar-header">
						<a class="navbar-minimalize minimalize-styl-2 btn btn-primary "
							href="#"><i class="fa fa-bars"></i> </a>
						<form role="search" class="navbar-form-custom" method="post"
							action="#">
							<div class="form-group"></div>
						</form>
					</div>
					<ul class="nav navbar-top-links navbar-right">
						<li><@s.a href="%{logout}"><i class="fa fa-sign-out"></i>Logout</@s.a>
						</li>
					</ul>
				</nav>
			</div>
			<div class="row">
				<div class="col-lg-4">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<span class="label label-primary pull-right">Account</span>
							<h5>Tổng số tài khoản</h5>
						</div>
						<div class="ibox-title">
							<span class="label label-primary pull-right" style="font-weight: bold;font-size: 13px;margin-top: 20px" id="countNewUser">10 tài khoản mới</span>
							<h1 style="font-weight: bold;" id="totalLogin">100</h1>
						</div>
						<div class="ibox-content">
							<div class="flot-chart">
                            	<div class="flot-chart-content" id="flot-line-chart-login"></div>
                        	</div>
						</div>
					</div>
				</div>
				<div class="col-lg-4">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<span class="label label-danger pull-right">Online</span>
							<h5>Số tài khoản đang chơi</h5>
						</div>
						<div class="ibox-title">
							<span class="label label-danger pull-right" style="font-weight: bold;font-size: 13px;margin-top: 20px" id="percentCCU7Days">Tăng 2%</span>
							<h1 style="font-weight: bold;color: #3498DB" id="ccuCounts">2</h1>
						</div>
						<div class="ibox-content">
							<div class="flot-chart">
                            	<div class="flot-chart-content" id="flot-line-chart-ccu"></div>
                        	</div>
						</div>
					</div>
				</div>
				<div class="col-lg-4">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<span class="label label-success pull-right">Total</span>
							<h5>Lợi nhuận</h5>
						</div>
						<div class="ibox-title">
							<span class="label label-success pull-right" style="font-weight: bold;font-size: 13px;margin-top: 20px" id="percentFeeGame7Days">Tăng 5% </span>
							<h1 style="font-weight: bold;color: #E74C3C" id="feeGameTotal">100,000,000</h1>
						</div>
						<div class="ibox-content">
								<div class="flot-chart">
                            		<div class="flot-chart-content" id="flot-line-chart-fee"></div>
                        		</div>
						</div>
					</div> 
				</div>
			</div>	
		</div>
		<!-- Mainly scripts --> <script src="js/jquery-3.1.1.min.js"></script>
		<script src="js/bootstrap.min.js"></script> <script
			src="js/plugins/metisMenu/jquery.metisMenu.js"></script> <script
			src="js/plugins/slimscroll/jquery.slimscroll.min.js"></script> <!-- Custom and plugin javascript -->
		<script src="js/inspinia.js"></script> <script
			src="js/plugins/pace/pace.min.js"></script>
			<script
			src="js/plugins/flot/jquery.flot.js"></script> <script
			src="js/plugins/flot/jquery.flot.tooltip.min.js"></script> 
			<script src="js/plugins/flot/jquery.flot.resize.js"></script>
			<script src="js/plugins/flot/jquery.flot.pie.js"></script>
			<script src="js/plugins/flot/jquery.flot.time.js"></script>
			  <!-- <script src="js/plugins/flot/flot-demo.js"></script> -->
</body>

</html>
