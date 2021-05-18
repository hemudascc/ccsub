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

.langchange {
	padding: 5px;
	text-align: right;
	color: blue;
}
</style>
<script type="text/javascript">
function sendToCG(){
	
	var cid = document.getElementById("token").value; 
	window.location.href ="http://test.zaindsp.com:3033/?p=6826526825&cid="+cid
}
</script>
<body>
	<%-- <form action="${pageContext.request.contextPath}/cnt/cornet/send-pin" method="POST"> --%>
	<!-- <form action="http://test.zaindsp.com:3033/?p=787899799" method="GET"> -->
		
		<!-- Hidden Parameters Start -->
		<input id="token" name="token" value="${token}" type="hidden">
		<input id="accessToken" name="accessToken" value="${accessToken}" type="hidden">
		<!-- Hidden Parameters End -->
		<!-- Body Start -->
		<div class="img-div" style="margin-top: 15px;">
			<img style="height: 200px; width: 80%;"
				src="${pageContext.request.contextPath}/resources/cornetsudan/image/gogames.png"
				class="center-block img-rounded img-banner"">
		</div>
		 <h3>${cornetConfig.serviceName}</h3> 
		
		<%-- 	<c:if test="${status==0}">
				<p>Enter your ${cornetConfig.operatorName} Mobile number to receive OTP</p>
			</c:if>
			<c:if test="${status==102}">
				<p>Please Enter a valid mobile number</p>
			</c:if>
			<c:if test="${status==2}">
				<p>You have exceded your limit.</p>
			</c:if>
			<c:if test="${status==3}">
				<p>Could not send pin please try again</p>
			</c:if>
		<input type="text" name="msisdn" id="msisdn" placeholder="249XXXXXXXX"
			style="width: 200px; height: 30px; margin: 0px 0px 5px 0px; color: black;"> --%>
		<br>
			<button id="subscribe" type="submit" class="btn btn-success" onclick="sendToCG();">Subscribe</button>
			<!-- <button id="exit" type="button" onclick="exit();"
				class="btn btn-danger">Exit</button> -->
	<!-- </form> -->
	<div class="terms-condition">
		<p>
					<b>Terms and Conditions:</b>
			
		</p>
		<%-- <p>
			<b>-</b>
			After 1 day free trial, you will be charged AED 
			${altruistServiceConfig.price}/${altruistServiceConfig.durationDescription} automatically.
			
		</p>
			<p>
			<b>-</b>
				<span>No commitment you can cancel anytime by sending
					${altruistServiceConfig.unsubKey} to
					${altruistServiceConfig.shortCode}</span>
			
		</p>
		 --%>
			<p>
				<b>-</b>
				<span>For support please contact  tech.d2c@collectcent.com</span>
			
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
	
	$("#exit").click(function() {
		window.location.href = "http://www.gogames.tv/?dcb=csz"
	});
</script>
</html>