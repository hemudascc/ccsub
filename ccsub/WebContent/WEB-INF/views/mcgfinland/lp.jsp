<!DOCTYPE html>
<html lang="en">
<head>
<title>Games-Park</title>
<meta charset="utf-8">

<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<meta http-equiv="X-UA-Compatible" content="ie=edge">
<link rel = "stylesheet" href = "https://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css">  
<link rel = "stylesheet" href = "${pageContext.request.contextPath}/resources/mcgfinland/css/style.css">  
</head>
<body
	style="background-color: white; background-size: cover; font-family: 'Montserrat', sans-serif;">


	<div class="ng-scope" ng-app="app">
		<!-- ngView: -->
		<ng-view class="ng-scope">
		<link class="ng-scope" rel="stylesheet" href="css/main.css">
		<div class="product_name">
			<img src="${pageContext.request.contextPath}/resources/mcgfinland/images/product.png"
			 class="img-responsive">
		</div>
		<div class="text-center ng-scope"
			ng-init="whoIAm = 'ch-bc-perso1'; countryId = 41">
			<img src="${pageContext.request.contextPath}/resources/mcgfinland/images/games_park_logo.jpg"
				class="img-responsive cardBG">
		</div>
		
		
		<form action=""  method="post">
		<input type="hidden" name="token" value="1603284255146c93662952c251" >
		 <p style="text-align:center;font-size:25px;">
					<strong>5 Euros / Viikko</strong>
				</p>
		<div class="text-center ng-scope"
			ng-init="whoIAm = 'ch-bc-perso1'; countryId = 41">
			<input maxlength="11" name="msisdn" class="page_wrap__content__form__phone"
			 placeholder="Enter number" value="" type="text"> 
		</div>
	
		     
		<div align="center">
			
			    <input type="image" src="${pageContext.request.contextPath}/resources/mcgfinland/images/btn_img.png" 
			    alt="Submit" width="45%" >				
			</div>
		</div>
			<div class="btn-wrapper text-center ng-scope">
			<div class="" ng-show="step == 1">
		    Voit peruuttaa tilisivusi milloin tahansa tai l�hett�� <b>STOP-numeron 0000 </b>Jos tarvitset apua, ota meihin yhteytt� l�hett�m�ll� s�hk�postia osoitteeseen 
			<b>care@collectcent.com</b> m. tai soittamalla numeroon FI
				<b>080 022 (3018). </b>
				</div>
				</div>
		<div class="btn-wrapper text-center ng-scope">
			<div class="" ng-show="step == 1">
			    <p>T�m� on mobiilik�ytt�jien tilauspohjainen online-peliportaali, joka p�ivitet��n prejodisesti uusilla peleill� ja k�ytt�j� voi maksaa rajattomasti online-pelej�
</p>			
			</div>
		</div>
		
        </form>
		<div class="addBody text-center textP text-white ng-scope">
			
			<p class="textP" style="color: #000;">
				<!--"Klicken Sie oben und geben Sie Ihre MSISDN ein. Sie erhalten eine
				SMS. Antworten Sie START ABO GAMESPARK 311, um unbegrenzte Spiele von Games-Park zu
				genieen." <br /> <br /> -->
Games Park on tilauspalvelu, jota yll�pit�� Collectcent DIgital Media Private Limited. Osoite: N 14/36, FIrst Floor, DLF Phase 2, Gurgaon Haryana, 122002, Intia. Rekister�intinumero: ja tila Napsauttamalla painiketta hyv�ksyt yleiset ehdot.. <br> 
				<br /> 
				Hyv�ksyn Mobiilimaksun k�ytt�ehdot.
				<br />
				<a href="./tc"><strong>k�ytt�ehdot</strong></a>
				 
			</p>
		</div>
		</ng-view>
	</div>

	

	<script src="${pageContext.request.contextPath}/resources/mcgfinland/js/angular-route.js"></script>

	<script src="${pageContext.request.contextPath}/resources/mcgfinland/js/ng-alertify"></script>
</body>
</html>
