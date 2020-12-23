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
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>  
  <title></title>
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
.imgcontainer {
  position: relative;
  text-align: center;
  color: white;
}
.imgtextcentered {
  position: absolute;
  top: 90%;
  left: 50%;
  transform: translate(-50%, -90%);
  color:white;
 font-weight:boldze;
}

</style>
<script type="text/javascript">
function exit(){
	alert("exit")
	location.href='${actelServiceConfig.portalUrl}';
	return false;
}

</script>
 </head>
<body>

<header>
			
			<div style="text-align:center;padding-top:10px;font-weight: bold">			
		           <p>you can subscribe service Kido Kingdom by sending KK to 3368 </p>
			</div>
			
			
			</header>
			<section>
			<div class="col-md-12 col-sm-12 col-lg-12" style="text-align:center;">
			<div class="imgcontainer">
			<p style="text-align:center;"><img src="${pageContext.request.contextPath}/resources/mt2zainiraq/animal-fall.png" 
			class="img-responsive"
			 height="200" width="300"></img></p>
			    <div class="imgtextcentered" dir="rtl"><h2>240 د.ع للرسالة المستلمة يوميا</h2></div>
			 </div>
			   
				   <p style="text-align:center;font-size:14px;"><strong></strong></p>
				<div class="ok" style="max-width: 100%;padding-bottom: 15px;">
				
								
				<br/><br/>
			
			 </div>
			 <p>
			 <p>
			Kido Kingdom is a subscription service for which you would receive unlimited access to all premium 
			games. This subscription would be renewed at 240 IQD/daily
			 for subscribers of Zain, automatically until you unsubscribe. You can unsubscribe from this service
			  anytime by sending UKK to 3368
			   for Zain’s subscribers. 
			   To make use of this service, you must be more than 18 years old or have received permission 
			   from your parents or person who is authorized to pay your bill.
			      </p>
			</div> 
			
			</section>
			
 </body>
</html>
