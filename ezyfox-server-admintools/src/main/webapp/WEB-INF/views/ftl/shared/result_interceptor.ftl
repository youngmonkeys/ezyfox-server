<!DOCTYPE html>
<#include "navigator.ftl">
<html>

<head>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Giới hạn quyền truy cập</title>

<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="font-awesome/css/font-awesome.css" rel="stylesheet">

<link href="css/animate.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet">
<link rel="stylesheet" href="css/plugins/toastr/toastr.min.css" />
</head>

<body>
	<div id="wrapper">
		<#include "navigation.ftl">
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
				<div class="col-lg-12">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
 							<h1 style="color: #1ABB9C">Giới hạn quyền truy cập</h1>
						</div>
						<div class="ibox-content">
							 <div class="alert alert-danger" style="text-align: center">
							 	<button class="btn btn-warning dim" type="button"><i class="fa fa-warning"></i></button>
                                Bạn không có quyền truy cập vào chức năng này
                            </div>
						</div>


					</div>
				</div>
				<#include "footer.ftl">
			</div>
		</div>
		<!-- Mainly scripts --> <script src="js/jquery-3.1.1.min.js"></script>
		<script src="js/bootstrap.min.js"></script> <script
			src="js/plugins/metisMenu/jquery.metisMenu.js"></script> <script
			src="js/plugins/slimscroll/jquery.slimscroll.min.js"></script> <!-- Custom and plugin javascript -->
		<script src="js/inspinia.js"></script>
		<script src="js/plugins/toastr/toastr.min.js"></script> <script
			src="js/plugins/pace/pace.min.js"></script> 
</body>

</html>