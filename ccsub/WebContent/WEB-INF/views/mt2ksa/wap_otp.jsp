<%@ page pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 

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
  <title>${mt2KSAServiceConfig.serviceName} Pin Page</title>
  <style>
 
.button {
  background-color: #00b359; /* Green */
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
<script type="text/javascript">
function exit(){
	window.location.href='${actelServiceConfig.portalUrl}';
	return false;
}



</script>
 </head>
<body>

<header>


			
			<div style="text-align:center;padding-top:10px;font-weight: bold">			
			  Subscribe ${mt2KSAServiceConfig.serviceName}
             ${mt2KSAServiceConfig.pricePointDesc}/${mt2KSAServiceConfig.validityDesc}            	
			</div>
			
			
			</header>
			<section>
			<div class="col-md-12 col-sm-12 col-lg-12" style="text-align:center;">
			<form id="otpform" method="post" action="${pageContext.request.contextPath}/cnt/mt2ksa/web/send/otp/validation">
			<p style="text-align:center;">
			<img src="${pageContext.request.contextPath}/resources/mt2ksa/${mt2KSAServiceConfig.lpImg}" 
			class="img-responsive" height="200" width="300"></img></p>
			<p style="text-align:center;font-size:14px;"><strong>${mt2KSAServiceConfig.serviceName} <br/> Welcome: ${msisdn}</strong>
			<p style="text-align:center;font-size:14px;"><strong>${otpinfo}</strong></p>
			<br/> 
			
			<p style="text-align:center;font-size:14px;">			
			Please enter the PIN Code you received to   <br/>activate your subscription
			</p>
						
			<div class="ok" style="max-width: 100%;padding-bottom: 15px;">
			<input type="text" name="pin" style="width:90%">
			<input type="hidden"  name="token" value="${token}"/>
			<input type="hidden"  name="msisdn" value="${msisdn}"/>
			</div>
			<br/><br/>
			<input type="submit" class="button" style="font-size: 15px;" 
			value="Confirm your subscription"/><br/><br/>
			<button class="button" style="font-size: 15px;" onclick="exit()">Exit</button>
			</form>
			</div> 
				 <p>
				
		
			 Kido Kingdom is a subscription service for which you would receive unlimited access to all premium 
			games.For ${mt2KSAServiceConfig.operatorNameDesc} Subscribers the service price is ${mt2KSAServiceConfig.pricePointDesc}/${mt2KSAServiceConfig.validityDesc}, 
			and to unsubscribe send ${mt2KSAServiceConfig.unsubKey} to ${mt2KSAServiceConfig.unsubShortCode} 
			15% will be added to the Postpaid Subscribers invoice.
			   To make use of this service, you must be more than 18 years old or have received permission 
			   from your parents or person who is authorized to pay your bill.
			
			   
			      </p>
			    
              
              					</section>

			 


 </body>
</html>
