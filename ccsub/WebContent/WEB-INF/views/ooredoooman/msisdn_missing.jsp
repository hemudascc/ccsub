
<!doctype html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="description" content="">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/style.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>  
  <title>Msisdn Page</title>
  <style>
 
.button {
  background-color: #4caf50; /* Green */
  border: none;
  color: white;
  padding: 6px 32px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 23px;
  font-weight: bold;
  width: 90%;
  border-radius: 12px;
}

.button1 {
  background-color: #000000; /* Green */
  border: none;
  color: white;
  padding: 6px 32px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 23px;
  font-weight: bold;
  width: 30%;
  border-radius: 12px;
}

</style>
 </head>
<body>



			<!--<div style="text-align:center;padding-top:5px;font-weight:bold;font-size:8px;">
			<a href="javascript:void(0)" onclick="showhide(1);">English</a> | <a href="javascript:void(0)" onclick="showhide(2);">العربية</a>
			</div>-->

			<!--<header>
			
			<div style="text-align:center;padding-top:5px;font-weight: bold">
			<div id="eng1">
			Free for 24 hours then AED 11 per week VAT included	
			</div>
			<div id="arb1" style="display:none;">
			مجانًا لمدة يوم واحد ثم 11 دراهم أسبوعيًا ، بما في ذلك ضريبة القيمة المضافة
			</div>
			</div>
			</header>-->


			<section>
			<div class="col-md-12 col-sm-12 col-lg-12">
			 

			<p style="text-align:center;">
			<img src="${pageContext.request.contextPath}/resources/oordeoooman/banner1.png" 
			class="img-responsive" height="200" width="300"></img></p>
			
			<p style="text-align:center;font-size:14px;" id="eng2">Get Unlimited Access To Games</p>
				
			
	<p style="text-align:center;font-size:14px;" id="eng2"><strong>Gamezine App</strong> <br/> 
	    Enter your  Mobile Number To Subscribe</p>
			<div class="ok" style="max-width: 100%;padding-bottom: 15px;">
			<form method="post" action="${pageContext.request.contextPath}/cnt/ooman/web/send/otp">
                <input id="token" type="hidden" name="token" value="${token}">
			<input type="hidden" name="lang" id="lang" value="1">
			<center><input type="text" name="msisdn" id="msisdn" placeholder="968xxxxxxx" required
			 value="${msisdn}"><center>
			<br/><br/>
			<button type="submit" name="submit" class="button" style="font-size: 15px;">
			<span id="eng3">CLICK HERE TO SUBSCRIBE</span>
			
			</button>
			<p style="text-align:center;font-size:15px;" id="eng2">
			<strong>Only For Ooredoo clients OMR 1.25 / week(auto renewal)</strong></p>
			<!--<br/><br/>
			<button class="button1" style="font-size: 15px;">
			<span id="eng4">Exit</span>
			
			</button>-->

			</form>
			</div> 
			
				<div id="eng5" style="font-weight: normal;">

						
						
						<div style="text-align:center;font-weight:normal;">

				<!-- 	Welcome to <b>Gamezine</b> Service. 
						This service is available through Subscription and is available for most
						 Mobile phone. To make Use of this Service, you must be more than 
						 <b>18 year old</b> or have received permission from your parents or the person
						  who is authorized to pay your bill.Ooredoo Subscribers will be charged 
						  <b>OMR 1.25 </b>per week  automatically until you unsubscribe.
						   You can stop your subscription any time by sending <b>UNSUB GA </b>to <b>91186</b> 
						  Standard Ooredoo data charge applicable. -->
						  
						  Welcome to <b>Gamezine App </b> service.
						This service is available through subscription and is available for most
						mobile phone. To make use of this service, you must be more than
						<b>18 years old</b> or have received permission from your parents or the person
						 who is authorized to pay your bill. Ooredoo subscribers will be charged
						 <b>1.25 OMR</b> per week, automatically until you unsubscribe.
						  You can stop your subscription any time by sending <b>UNSUB GA</b> to <b>91186</b>.
						   Standard Ooredoo data charge applicable.


						</div>

						<!--<div style="text-align:center;">
						-You will start the paid subscription automatically 	
						</div>
						<div style="text-align:center;">
						-Renewal will be automatic every week	
						</div>
						<div style="text-align:center;">
						-No commitment you can cancel anytime by sending UNSUB GA to 91186	
						</div>
						<div style="text-align:center;">
						-For support,please contact xyz@abc.com

						</div>-->
			</div>


			
			</div>
			</section>

	


 </body>
</html>
