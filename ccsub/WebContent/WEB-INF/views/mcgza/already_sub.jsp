<!DOCTYPE html>
<html><head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<title>MyLife</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/mcgza/stylesheet.css">
</head>

<body class="termspage">
<div id="mainbox">
	<div id="terms">
	    <header><img src="${pageContext.request.contextPath}/resources/mcgza/MyLife-logo.png"></header>
        <div class="box">
        	<h1>Welcome to the MyLife</h1>
            <h2><b class="bold">R7 per Day</b></h2>
        	<h2>Enter the mobile number that you used to complete the payment to access the content portal</h2>
            <form action="${pageContext.request.contextPath}/cnt/mcgza/checksub">
   				 <input type="text" name="msisdn" class="formStyle" placeholder="Msisdn"
   				   required=""><br>
   				   <input type="hidden" name="token" value="${token}" />   				
      			  <input type="submit"  class="formButton" value="Continue">
 			</form>          
	  </div>
             <footer>� Coypright 2019. MyLife</footer>
	</div>
    </div>


</body></html>