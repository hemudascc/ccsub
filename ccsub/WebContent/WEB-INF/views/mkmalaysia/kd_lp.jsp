<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
<title>Kido Kingdom</title>
<link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/mkmlaysia/gogame/gogame_favicon.png"> 
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<script src="${pageContext.request.contextPath}/resources/js/jquery.js"></script>
<link rel = "stylesheet" href = "https://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css">  
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css">

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
 <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>  
  
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
<link  rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css">
<style type="text/css">
input[type="text"] {
    padding: 12px 20px;
    margin: 8px 0;
    box-sizing: border-box;
}
.banner {
    width: 100%;
    margin: auto;
}

body {
    font-family: Arial, Helvetica, sans-serif;
    margin:0;
padding:0;
height:70%;
line-height:1.1;
}
.banner img {	
    max-width: 100%;
    max-height:40%;
}
.buttonbar {
    width: 100%;
    margin: auto;
    text-align: center;
    padding-top: 5px;
}

.button {
    background-color: #0033cc;
    border: none;
    color: white;
    padding: 18px 20px;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    font-size: 15px;
    font-weight: bold;
    width: 235px;
    border-radius: 12px;
	
}

.button a {
    transition: 0.2s ease-in-out;
}
.ok {
    text-align: center;
}

</style>

<style type="text/css">


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
	max-width: 50%;
	height: auto;
}
html, body {
     font-size: 40px;      
     margin:0; /* remove default margin */
     padding:0; /* remove default padding */
     width:100%; /* take full browser width */
     height:100%; /* take full browser height*/
}
</style>

<script>
$(document).ready(function() {
	  function setHeight() {
	    windowHeight = $(window).innerHeight();
	    $('#conatiner').css('min-height', windowHeight);
	  };
	  //alert($('#conatiner').height());
	//  setHeight();
	});
	function check(){
		//alert(""+document.getElementById("opid").value);
		if(document.getElementById("opid").value=='DIGI'){
			//alert("true ::"+document.getElementById("opid").value);
			document.getElementById("frm").submit();
			return false;
		}
		return true;
	}
</script>
</head>
<body 
	style="background-color: #44ccfc; background-size: cover; font-family: 'Montserrat', sans-serif;">


	<section>
			<div class="col-md-12 col-sm-12 col-lg-12" >
		
		
		<p style="text-align:center;font-size:20px;font-weight:bold;">
		Kido Kingdom
		</p>
		<div class="text-center"
			>
			<img src="${pageContext.request.contextPath}/resources/mkmlaysia/kd_logo.png"
				class="img-responsive cardBG" >
		</div>
		
		
		<form id="frm" action="${pageContext.request.contextPath}/cnt/mkml/tocg"  >
		<p style="text-align:center;font-size:11px;font-weight:bold;">
		Please enter your mobile phone number:
		</p>
		<p style="text-align:center;font-size:20px;font-weight:bold;">
		
		<input type="hidden" name="token" value="${token}" >
				
<!-- 		<input type="hidden" name="adid" value="1" >	 -->
<!-- 		<input type="hidden" name="cmpid" value="149" >	 -->

		<input type="text" name="msisdn" placeholder="01" value="${msisdn}">
	</p>
	
		<div class="btn-wrapper text-center">
<!-- 				<button class="button"  style="white-space: nowrap">Subscribe Now!</button>		 -->
				<a class="button" href="sms:36222?body=ON KKD ${token}" onclick="return check();">
                                	Subscribe Now!</a>		
                                	
		</div>		 
		 <p style="text-align:center;font-size:10px;">
	   This service is an HTML5 mobile game subscription service; the game is compatible with most Smartphones with Cellular data.
		 </p>
		
			<p style="text-align:center;font-size:10px;">
			There is no subscription fee. For Digi Customers, 
			the price is RM5 (excl. SST)/SMS, 10 SMS/month, Max charge RM50/month. 
			Until you unsubscribe from this service. Normal Mobile operator network charges apply.
			 Cellular data needs to be enabled to download the content. Data charges are billed separately.
			  To unsubscribe send "STOP KKD" to 36222. Helpline 603-2164 3273: 9:00AM - 6:00PM, Mon-Fri. 
			  support@macrokiosk.com You acknowledge that you have read and accept the "Terms & Conditions" 
			  Powered by Macro Kiosk Berhad.
			
			</p>
		
        </form>
		
		
	</div>

	
</body>
</html>
