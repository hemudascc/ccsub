<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
<title>Unsubscribe Go-Games</title>
<link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/mkmlaysia/gogame/gogame_favicon.png"> 
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<link rel = "stylesheet" href = "https://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css">  
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/style.css">
<script src="${pageContext.request.contextPath}/js/jquery.js"></script>
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
}
.banner img {	
    max-width: 100%;
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
    padding: 18px 32px;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    font-size: 23px;
    font-weight: bold;
    width: 235px;
    border-radius: 12px;
	line-height: inherit;
}

.button a {
    transition: 0.2s ease-in-out;
}
.ok {
    text-align: center;
}
</style>

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
<script type="text/javascript">
function callUnsub(){ 
	
	//alert('${pageContext.request.contextPath}'+'/cnt/hgate/send/ussd?serviceid='+id);
	 var mobNum = $("#msisdn").val();
	 //alert(mobNum+ "id:: "+id+", token:: ${token}");
     var filter = /^\d*$/;
     if (filter.test(mobNum)==false) {
       alert("Please enter valid  mobile number");       
       return  false;
    }
     
    $.ajax({
        url: '${pageContext.request.contextPath}/cnt/mkml/unsub/process?msisdn='+mobNum,
        type: 'GET',
        crossDomain:true,
        data: {
            format: 'jsonp'
            
        },
        timeout: 180000,
        success: function(response) {
        	
        	//var json = $.parseJSON(response);
        	alert("Your unsubscription request for service Go Game accepted");
        	
        	
         },
    error: function(textstatus,response) {
        alert("Some technical issue to unsubscribe.please try after some time");
        setTimeout(function(){// wait for 5 secs(2)
            location.reload(); // then reload the page.(3)
       }, 5000); 
    }
    });
    return false; // for good measure
}
</script>

</head>
<body
	style="background-color: #44ccfc; background-size: cover; font-family: 'Montserrat', sans-serif;">


	<div class="ng-scope" ng-app="app">
		<!-- ngView: -->
		<ng-view class="ng-scope">
		<link class="ng-scope" rel="stylesheet" href="css/main.css">
		<div class="product_name">
			<img src="${pageContext.request.contextPath}/resources/mkmlaysia/gogame/product.png"
			 class="img-responsive">
		</div>
		<div class="text-center ng-scope"
			ng-init="whoIAm = 'ch-bc-perso1'; countryId = 41">
			<img src="${pageContext.request.contextPath}/resources/mkmlaysia/gogame/go_game_logo.jpg"
				class="img-responsive cardBG">
		</div>
		<form action="${pageContext.request.contextPath}/cnt/mkml/unsub/process">
		<div>
					<p style="text-align:center;font-size:14px;font-weight:bold;">Enter your Mobile Number:</p>
					<p style="text-align:center;font-size:14px;font-weight:bold;"><input  id="msisdn" name="msisdn" value="" size="30" type="text" 
             onKeyPress="if(this.value.length==12||/^\d*$/.test(this.value)==false) return false;" placeholder="Mobile Number" title="Mobile number"/> </p>
		</div>
		<div>
					<p style="text-align:center;font-size:20px;font-weight:bold;">
					  <input type="checkbox" name="robotid" value="norobot">I am not robot <br>
					</p>
					</div>
		<br/>
		<div class=" text-center ng-scope">
			<div class="" ng-show="step == 1" >
				<button class="button"  >UnSubscribe Now!</button>	
			</div>
		</div>		
      
		</form>
		</ng-view>
	</div>

	
</body>
</html>
