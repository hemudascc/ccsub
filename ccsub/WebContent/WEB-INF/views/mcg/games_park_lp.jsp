<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
<title>Games-Park</title>
<link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/mcg/gamespark/gamespark_favicon.png"> 
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<link rel = "stylesheet" href = "https://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css">  
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/style.css">
<style type="text/css">
input[type=text], select {
  width: 60%;
  padding: 12px 20px;
  margin: 8px 0;
  display: inline-block;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
  text-align: left; 
  font-size:30px;
}
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
</style>


</head>
<body
	style="background-color: white; background-size: cover; font-family: 'Montserrat', sans-serif;">


	<div class="ng-scope" ng-app="app">
		<!-- ngView: -->
		<ng-view class="ng-scope">
		<link class="ng-scope" rel="stylesheet" href="css/main.css">
		<div class="product_name">
			<img src="${pageContext.request.contextPath}/resources/mcg/gamespark/product.png"
			 class="img-responsive">
		</div>
		<div class="text-center ng-scope"
			ng-init="whoIAm = 'ch-bc-perso1'; countryId = 41">
			<img src="${pageContext.request.contextPath}/resources/mcg/gamespark/games_park_logo.jpg"
				class="img-responsive cardBG">
		</div>
		
		
		<form action="${pageContext.request.contextPath}/cnt/mcg/lp"  method="post">
		<input type="hidden" name="token" value="${token}" >
		 <p style="text-align:center;font-size:25px;">
					<strong>15CHF Sunrise / 3 x 5CHF  Swisscom & Salt pro Woche</strong>
				</p>
		<div class="text-center ng-scope"
			ng-init="whoIAm = 'ch-bc-perso1'; countryId = 41">
			<input maxlength="11" name="msisdn" class="page_wrap__content__form__phone"
			 placeholder="Enter number" value="07" type="text"> 
		</div>
	
		     
		<div align="center">
			
			    <input type="image" src="${pageContext.request.contextPath}/resources/mcg/gamespark/btn_img.png" 
			    alt="Submit" width="45%" >				
			</div>
		</div>
			<div class="btn-wrapper text-center ng-scope">
			<div class="" ng-show="step == 1">
		 Sie knnen
				jederzeit von Ihrer Kontoseite abbrechen oder einen <b>STOP an die
				 311 </b>senden. Wenn Sie Hilfe bentigen, kontaktieren Sie uns bitte,
				indem Sie eine E-Mail an care@collectcent.com senden. oder indem Sie
				CH 080 022 (3018) aufrufen.
				</div>
				</div>
		<div class="btn-wrapper text-center ng-scope">
			<div class="" ng-show="step == 1">
			    <p>Dies ist ein abonnementbasiertes Online-Gaming-Portal für mobile Benutzer, das preiodisch mit neuen Spielen aktualisiert wird und der Benutzer kann unbegrenzt online Spiele bezahlen
</p>			
			</div>
		</div>
		
        </form>
		<div class="addBody text-center textP text-white ng-scope">
			
			<p class="textP" style="color: #000;">
				"Klicken Sie oben und geben Sie Ihre MSISDN ein. Sie erhalten eine
				SMS. Antworten Sie START ABO GAMESPARK 311, um unbegrenzte Spiele von Games-Park zu
				genieen." <br /> <br /> Der Games-Park ist ein Abonnement-Service,
				der von der Collectcent DIgital Media Private Limited Adresse: N
				14/36, FIrst Floor, DLF Phase 2, Gurgaon Haryana, 122002,India.
				Registrierungsnummer: und Status Durch Klicken auf den Button
				stimmen Sie den Allgemeinen Gesch ftsbedingungen zu. <br> 
				<a href="${pageContext.request.contextPath}/cnt/mcg/termcondtion?serviceid=${mcgServiceConfig.serviceId}"><strong>Terms &amp; Bedingungen</strong></a>
				 
			</p>
		</div>
		</ng-view>
	</div>

	<script src="js/angular.js"></script>

	<script src="js/angular-route.js"></script>

	<script src="js/ng-alertify"></script>
</body>
</html>
