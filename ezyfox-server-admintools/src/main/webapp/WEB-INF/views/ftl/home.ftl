<!DOCTYPE html>
<html>

<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Ezyfox Dashboard</title>

<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="font-awesome/css/font-awesome.css" rel="stylesheet">

<link href="css/animate.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet">
<meta charset="UTF-8" />
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
						<li><a href="#"><i class="fa fa-sign-out"></i>Logout</a>
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
						<div class="ibox-content">
							<h1 id="totalLogin" class="no-margins" style="font-weight: bold;">10000</h1>
							<small id="newUser">200 tài khoản mới</small>
						</div>
					</div>
				</div>
				<div class="col-lg-4">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<span class="label label-danger pull-right">Online</span>
							<h5>Số tài khoản đang chơi</h5>
						</div>
						<div class="ibox-content">
							<h1 id="ccu" class="no-margins" style="font-weight: bold; color: #3498DB">1000</h1>
							<small>Thời gian tính trong 5 phút</small>
						</div>
					</div>
				</div>
				<div class="col-lg-4">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<span class="label label-success pull-right">Total</span>
							<h5>Giá trị game bank</h5>
						</div>
						<div class="ibox-content">
							<h1 id="gamebank" class="no-margins" style="font-weight: bold; color: #E74C3C" >200,000,000</h1>
							<small id="bankHistory"><i class="fa fa-sort-up"></i>10% so với 5 ngày
								trước</small>
						</div>
					</div> 
				</div>
			</div>
			<div class="row">
				<div class="col-lg-12">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h5>
								Biểu đồ người chơi <small>Tính 5 ngày trở lại</small>
							</h5>
						</div>
						<div class="ibox-content">
							<div class="flot-chart">
								<div class="flot-chart-content" id="flot-line-chart-player"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-12">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h5>
								Biểu đồ doanh thu lợi nhuận <small>Tính 5 ngày trở lại</small>
							</h5>
						</div>
						<div class="ibox-content">
							<div class="flot-chart">
								<div class="flot-chart-content" id="flot-line-chart-revenue"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-12">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<div>
								<table class="table">
									<tbody>
										<tr>
											<td>
												<button type="button" class="btn btn-danger m-r-sm" style="background-color: #ff80bf">
												</button> Doanh thu
											</td>
										</tr>
										<tr>
											<td>
												<button type="button" class="btn btn-info m-r-sm" style="background-color: #cc6666">
												</button> Trả thưởng
											</td>
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

		<!-- Mainly scripts --> <script src="js/jquery-3.1.1.min.js"></script>
		<script src="js/bootstrap.min.js"></script> <script
			src="js/plugins/metisMenu/jquery.metisMenu.js"></script> <script
			src="js/plugins/slimscroll/jquery.slimscroll.min.js"></script> <!-- Custom and plugin javascript -->
		<script src="js/inspinia.js"></script> <script
			src="js/plugins/pace/pace.min.js"></script> 
			<script
			src="js/plugins/flot/jquery.flot.js"></script> <script
			src="js/plugins/flot/jquery.flot.tooltip.min.js"></script> <script
			src="js/plugins/flot/jquery.flot.resize.js"></script>
			<script
			src="js/plugins/flot/jquery.flot.pie.js"></script>
			<script
			src="js/plugins/flot/jquery.flot.time.js"></script>
</body>

</html>
