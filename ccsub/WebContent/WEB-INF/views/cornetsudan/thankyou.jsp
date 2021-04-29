<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>${cornetConfig.serviceName}</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/bootstrap.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
</head>
<style>
body {
	text-align: center;
	color: #e8f5e9;
	background-color: darkslategrey;
}
</style>
<body>
	<!-- Body Start -->
		<p>Free for 1 day then AED ${cornetConfig.price} per
	 VAT Included</p>
	
	<div class="img-div">
		<img style="height: 200px; width: 80%;"
			src="${pageContext.request.contextPath}/resources/cornetsudan/image/gogames.png"
			class="center-block img-rounded img-banner"
			alt="${cornetConfig.serviceName}">
	</div>
	<h3>${cornetConfig.serviceName}</h3>
		<p>
			<strong>You have been successfully subscribed to
				${cornetConfig.serviceName}</strong>
		</p>
	<br>
	<div class="terms-condition">
		<p>
				<b>We have sent you a confirmation SMS that includes your
					subscription details for your records.</b>
		</p>
		<p>
				<b>Please keep that message for future reference.</b>
		</p>
		<p>
			<b><a href="${portalURL}">Click here</a> to access the
				${cornetConfig.serviceName}
			</b>
		</p>
	</div>
	<!-- Body End -->
</body>
</html>