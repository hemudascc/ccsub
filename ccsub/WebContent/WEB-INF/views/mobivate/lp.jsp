<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
<title>${mobivateServiceConfig.serviceName}</title>
<link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/mcg/gogame/gogame_favicon.png"> 
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<link rel = "stylesheet" href = "https://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css">  
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/style.css">
<style type="text/css">
@charset "UTF-8";
[ng\:cloak],[ng-cloak],[data-ng-cloak],[x-ng-cloak],.ng-cloak,.x-ng-cloak,.ng-hide:not(.ng-hide-animate){
   display:none !important;
}
ng\:form {
	display: block;
}

.ng-animate-shim {
	visibility: hidden;
}

.ng-anchor {
	position: absolute;
}

.product_name {
	text-align: center;
	margin-bottom: 0%;
}
.text-center {
    text-align: center;
    margin-top: 1%;
}
.carousel-inner>.item>a>img, .carousel-inner>.item>img, .img-responsive,
	.thumbnail a>img, .thumbnail>img {
	display: inline-block;
	max-width: 100%;
	height: auto;
}
.button {
  padding: 15px 25px;
  font-size: 24px;
  text-align: center;
  cursor: pointer;
  outline: none;
  color: #fff;
  background-color: red;
  border: none;
  border-radius: 15px;
  box-shadow: 0 9px #999;
}
</style>


</head>
<body
	style="background-color: white; background-size: cover; font-family: 'Montserrat', sans-serif;">


	<div class="ng-scope" ng-app="app">
		<!-- ngView: -->
		<ng-view class="ng-scope">
		<link class="ng-scope" rel="stylesheet" href="css/main.css">
		<div class="product_name">
			
		</div>
		
		<div class="addBody text-center textP text-white ng-scope">
		
		</div>
		
		<div class="text-center ng-scope"
			ng-init="whoIAm = 'ch-bc-perso1'; countryId = 41">
			<img src="${pageContext.request.contextPath}/resources/mobivate/${mobivateServiceConfig.lpImg}"
				class="img-responsive cardBG">
		</div>
		
		<div class="text-center ng-scope"
			ng-init="whoIAm = 'ch-bc-perso1'; countryId = 41">
			<p>Click to access ${mobivateServiceConfig.serviceName} </p>
		</div>
		
		
		<input type="hidden" name="token" value="${token}" >
		
		
		
	<div class="ok" style="max-width: 100%;padding-bottom: 15px;text-align:center;">
			
				<input type="submit"  class="button" style="font-size: 30px;" value="CONTINUE" 
				 />
				 <br><br>
				 <p >
			<strong>Subscription ${mobivateServiceConfig.billingAmountDesc}
			 
					/ ${mobivateServiceConfig.validityDesc}</strong>
				</p>
			 </div>
		
		<div class="footer-disclaimer">
			<div class="small blocktext">
    <div class="color_footer">   
    <p class="privacy" style="text-align:center;padding: 15px;text-align:center;" >    
        Collectcent
    <a href="#" >Terms and Conditions</a>. 
    For users 18 years or over. Support @ 0861131009 
    </p>
    </div>
		
       
		
		</ng-view>
	</div>

	<script src="js/angular.js"></script>

	<script src="js/angular-route.js"></script>

	<script src="js/ng-alertify"></script>
</body>
</html>
