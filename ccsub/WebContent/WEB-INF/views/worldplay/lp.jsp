<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="stage" value="development">
<meta name="suffix" value="html5-th-dtac-th-default">
<meta name="creative" value="StreetRacingMania">
<meta name="page-id" value="2158">
<meta name="service" value="VIP Games">
<meta name="platform" value="html5">
<meta name="operator" value="_">
<meta name="one-file" value="false">
<meta name="language" value="th">
<meta name="jira-task-id" value="WAP-7803">
<meta name="country" value="th">
<meta name="test-campaign" value="19214">
<meta content="telephone=no" name="format-detection">
<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=0, minimum-scale=1, maximum-scale=1, minimal-ui">
<title data-c-role="title">${worldplayServiceConfig.serviceName}</title>
<meta name="postfix" value="default">
<link href="${pageContext.request.contextPath}/resources/css/index.css" rel="stylesheet"
	type="text/css">
<script src="https://code.jquery.com/jquery-1.10.2.js"></script>
<script type="text/javascript">
	/* $(document).ready(function(){
	    document.getElementById("subscribe").click();
	}); */
</script>
</head>

<body >

	<div id="container" class="show-default-state qa-html5" style="background-color: white">
	 <p  style="text-align:center;
                      font-size:30px;font-weight:bold; margin:0px;background-color: #B95C66;">
                     ${worldplayServiceConfig.serviceName}	
            </p>
		
			<img src="${pageContext.request.contextPath}/resources/worldplay/${worldplayServiceConfig.lpImg}"
				style="height: 80%; width: 100%"">
		
		

		<!-- SUBSCRIBE BUTTON -->
		<div id="default-state">
		
			<div  style="padding-top: 10px;text-align:center;"> 
				<b style="color: black; font-size: 30px;"> R 
				<fmt:formatNumber value="${worldplayServiceConfig.pricePoint}"
				 minFractionDigits="0" maxFractionDigits="0"/> /  Day Subscription</b>
			</div>
			<br> 
				<span
				data-x-role="subscribe-now"> <a href="${cgUrl}"
					class="btn10"></a></span>
			
		</div>
		<div class="footer-disclaimer">
			<div class="small blocktext">
    <div class="color_footer">   
    <p class="privacy" style="text-align:center;">    
        
    <a href="${termConditionPage}" >Terms and Conditions</a>. 
    For users 18 years or over. Support @ 0861131009 
    </p>
    </div>
</div>
	</div>

</body>
</html>