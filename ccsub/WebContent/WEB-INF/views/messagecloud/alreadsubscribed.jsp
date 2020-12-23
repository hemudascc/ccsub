<!DOCTYPE html>
<!-- saved from url=(0075)http://111.118.179.31/vigwap/layouts/collectcent/uk/WayOfLife/confirm.html# -->
<html><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>WayOfLife</title>
<meta name="viewport" content="width=device-width initial-scale=1">
<link rel="stylesheet" href="${pageContext.request.contextPath}/messagecloud/stylesheet.css">
</head>

<body>
<div id="main">
	<div id="terms">
	    <header><img src="${pageContext.request.contextPath}/messagecloud/WayOfLife-logo.png"></header>
        <div class="content">
        	<h1>Welcome to the WayOfLife</h1>
        	<h3>Please Enter Your Mobile Number</h3>
            <form id="form1" action="${pageContext.request.contextPath}/cnt/mc/checksub" method="post">
   				 <input type="text" name="msisdn" class="formStyle" maxlength="12" value="" required=""><br>
   				 <input type="hidden" name="token" class="formStyle" maxlength="12" value="${token}" required="">

      			  <a href="#" class="formButton" onclick="document.getElementById('form1').submit();">Continue</a>
 			</form>

	  </div>
             <footer>© Coypright 2019. WayOfLife</footer>
	</div>
    </div>



</body></html>