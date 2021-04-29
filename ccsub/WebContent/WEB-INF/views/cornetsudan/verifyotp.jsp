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
	<form action="${pageContext.request.contextPath}/cnt/cornet/verify-pin"
		method="post">
		<!-- Hidden Parameters Start -->
		<input id="token" name="token" value="${token}" type="hidden">
		<input id="msisdn" name="msisdn" value="${msisdn}" type="hidden">
		<input id="accessToken" name="accessToken" value="${accessToken}" type="hidden">
		<!-- Hidden Parameters End -->
		<!-- Body Start -->
		<div class="img-div" style="margin-top: 15px;">
			<img style="height: 200px; width: 80%;"
				src="${pageContext.request.contextPath}/resources/cornetsudan/image/gogames.png"
				class="center-block img-rounded img-banner"
				alt="${cornetConfig.serviceName}">
		</div>
		<h3>${cornetConfig.serviceName}</h3>
			<c:if test="${status==110}">
				<p>Please Enter a valid pin to activate your subscription</p>
			</c:if>
			<c:if test="${status==1}">
				<p>Please Enter the pin you received to activate your
					subscription</p>
			</c:if>
			<c:if test="${status==2}">
				<p>Some error occurred while processing your request.</p>
			</c:if>
		<input type="text" name="pin" id="pin" placeholder="XXXX"
			style="width: 200px; height: 30px; margin: 0px 0px 5px 0px; color: black;">
		<br>
			<button id="subscribe" type="submit" class="btn btn-success">Subscribe</button>
			<!-- <button id="exit" type="button" onclick="exit();" class="btn btn-danger">Exit</button> -->
		
	</form>
	<%-- <div>
			<p>Free for 1 day then AED ${altruistServiceConfig.price} per
				${altruistServiceConfig.durationDescription} VAT Included</p>
		
	</div> --%>
	<div class="terms-condition">
		<p>
					<b>Terms and Conditions:</b>
		</p>
			
		
			<p>
				<b>-</b>
				<span>For support please contact to tech.d2c@collectcent.com</span>
		</p>
		
		
		<p>
			<b>-</b>
				<span>For complete T's &amp;C's <a
					href="${pageContext.request.contextPath}/cnt/cornet/tc">click
						here</a></span>
		</p>
	</div>
	<!-- Body End -->
</body>
<script>
$("#exit").click(function(){
	window.location.href = "http://www.gogames.tv/?dcb=csz"
});
</script>
</html>