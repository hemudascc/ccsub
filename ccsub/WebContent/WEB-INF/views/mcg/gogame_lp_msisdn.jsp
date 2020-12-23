<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
<title>Go-Games</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
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
</style>


</head>
<body
	style="background-color: #44ccfc; background-size: cover; font-family: 'Montserrat', sans-serif;">


	<div class="ng-scope" ng-app="app">
		<!-- ngView: -->
		<ng-view class="ng-scope">
		<link class="ng-scope" rel="stylesheet" href="css/main.css">
		<div class="product_name">
			<img src="${pageContext.request.contextPath}/resources/mcg/gogame/product.png"
			 class="img-responsive">
		</div>
		<div class="text-center ng-scope"
			ng-init="whoIAm = 'ch-bc-perso1'; countryId = 41">
			<img src="${pageContext.request.contextPath}/resources/mcg/gogame/go_game_logo.jpg"
				class="img-responsive cardBG">
		</div>
		
		<form action="http://mob.ccd2c.com/CH/Go-Games/" method="post">
		<div class="text-center ng-scope"
			ng-init="whoIAm = 'ch-bc-perso1'; countryId = 41">
			<input type="number" name="msisdn" required="required">
		</div>
		<br/>
		<div class="btn-wrapper text-center ng-scope">
			<div class="" ng-show="step == 1">
			    <input type="image" src="${pageContext.request.contextPath}/resources/mcg/gogame/btn_img.png" alt="Submit" >				
			</div>
		</div>		
			<div class="page_wrap__content__under_form" style="text-align:center;">
				<p align="center">
					<strong>Der Abonnementservice kostet CHF  5 pro Woche.</strong>
				</p>
			</div>
        </form>
		<div class="addBody text-center textP text-white ng-scope">
			<p class="textP" style="color: #000;">
				"Klicken Sie oben und geben Sie Ihre MSISDN ein. Sie erhalten eine
				SMS. Antworten Sie START ABO GOGAMES 321, um unbegrenzte Spiele von Go-Games zu
				genie�en." <br /> <br /> Der Go-Games ist ein Abonnement-Service,
				der von der Collectcent DIgital Media Private Limited Adresse: N
				14/36, FIrst Floor, DLF Phase 2, Gurgaon Haryana, 122002,India.
				Registrierungsnummer:___ und Status Durch Klicken auf den Button
				stimmen Sie den Allgemeinen Gesch�ftsbedingungen zu. Sie k�nnen
				jederzeit von Ihrer Kontoseite abbrechen oder einen STOP an die
				_321____ senden. Wenn Sie Hilfe ben�tigen, kontaktieren Sie uns bitte,
				indem Sie eine E-Mail an care@collectcent.com senden. oder indem Sie
				CH 080 022 (3018) aufrufen. <br> <a style="color: #000;"
					href="terms.jsp">Gesch�ftsbedingungen</a> - <a style="color: #000;"
					href="#/contact">Kontaktiere</a>
			</p>
		</div>
		</ng-view>
	</div>

	<script src="js/angular.js"></script>

	<script src="js/angular-route.js"></script>

	<script src="js/ng-alertify"></script>
</body>
</html>
