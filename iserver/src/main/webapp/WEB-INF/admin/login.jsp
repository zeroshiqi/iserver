<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6 lt8"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7 lt8"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8 lt8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!-->
<html lang="en" class="no-js">
<!--<![endif]-->
<head>
<!-- <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">  -->
<meta charset="UTF-8" />
<title>Title</title>
<script src="${app}/js/jquery-1.8.0.min.js"></script>
<link rel="stylesheet" type="text/css" href="${app}/css/demo.css" />
<link rel="stylesheet" type="text/css" href="${app}/css/style4.css" />
<link rel="stylesheet" type="text/css"
	href="${app}/css/animate-custom.css" />
</head>
<body>
	<div class="container">
		<header>
			<nav class="codrops-demos"></nav>
		</header>
		<section>
			<div id="container_demo">
				<a class="hiddenanchor" id="toregister"></a> <a class="hiddenanchor"
					id="tologin"></a>
				<div id="wrapper">
					<div id="login" class="animate form">
						<form id="myform" action="${app}/admin/signin" autocomplete="on"
							method="post">
							<h1>Login</h1>
							<p>
								<label for="username" class="uname" data-icon="u"> Your
									account </label> <input id="account" name="account" required="required"
									value="" type="text" placeholder="My Account" />
							</p>
							<p>
								<label for="password" class="youpasswd" data-icon="p">
									Your password </label> <input id="password" name="password"
									required="required" value="" type="password"
									placeholder="My Password" />
							</p>
							<p class="login button">
								<input type="submit" id="button" value="Login" />
							</p>
							<p class="change_link" id="change_link">${message}</p>
						</form>
					</div>
				</div>
			</div>
		</section>
	</div>
</body>
<script type="text/javascript">
	$(document).ready(function(){
		
	});
</script>
</html>