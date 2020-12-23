<%@page     pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=yes">
<meta http-equiv="cache-control" content="max-age=0">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="expires" content="Tue, 01 Jan 1980 1:00:00 GMT">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1,IE=9,IE=8,IE=7">
<title>WEB Consent</title>


	

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.css">
  <link rel="stylesheet" href="../css/style.css">
  <script src="${pageContext.request.contextPath}/js/jquery.js"></script>
  <script src="${pageContext.request.contextPath}/js/bootstrap.js">
  </script>
<style type="text/css">
	.button {
	width:99%;
	max-width:280px;
	background:#009ecf;
	font-size:20px;
	color:#fff;
	padding:15px 20px;
	border:none;
	border-radius:7px;
	margin:10px auto;
	display:table;
}

.terms{
	padding:0 20px;
}
.terms h3{
	margin-bottom:0px;
	font-size:17px;
	font-weight:500;
	color:#898989;
}
.terms p{
	font-size:14px;
	color:#898989;
	margin-bottom:5px;
}
 .priceText{
 display:block;
	text-align:center;
	font-size:14px;
	font-weight:200;
	color:#636365;
}
.input{
	border:1px solid #ababab;
	padding:7px 8px;
	font-size:18px;
	margin:15px auto 5px;
	color:#898989;
}


input[type=image] {
 max-width: 100%; 
 width: auto;
 height: 80%;
}


.button {
  padding: 15px 25px;
  font-size: 24px;
  text-align: center;
  cursor: pointer;
  outline: none;
  color: #fff;
  background-color: #0ca7fa;
  border: none;
  border-radius: 15px;
  box-shadow: 0 9px #999;
}

.button:hover {background-color: #3e8e41}

.button:active {
  background-color: #3e8e41;
  box-shadow: 0 5px #666;
  transform: translateY(4px);
}

</style>
<style>
			
	.linear-bg1{
		background: linear-gradient(to bottom, #1EFF00 0%,#404B3F 24%,#131513 50%,#404B3F 79%,#1EFF00 100%);
		color: #fff;
		font-weight: bold;
	}
	tr, th {
   
    text-align: center;
}
		</style>



  <script type="text/javascript">
  function onLoad( bgImg){
		
		$('#header').css("background-image", "url("+bgImg+")");  
	 }
  $(document).ready(function () { 
	  $(document).ajaxStart(function () {
	         $('#wait').show();
	     });
	     $(document).ajaxStop(function () {
	         $('#wait').hide();
	     });
	     $(document).ajaxError(function () {
	         $('#wait').hide();
	     });   
	 });
  
  </script>
	
	<script type="text/javascript">
        //<![CDATA[
            <%= source %>
        //]]>
    </script>
    
</head>
<body>
<div class="container" align="center">
 
                      <img src="${pageContext.request.contextPath}/resources/images/bizao/game_pad_banner.jpg"
                       style="width: 50%;height: 50%">
		<br><br>
							
           <form id="formId" action="${pageContext.request.contextPath}/cnt/bizao/send/billed" method="get">
						<p style="font-family: verdana; font-size: 20px;">Otp :
						<input id="otp" type="text" name="otp" value="${otp}">
						</p>
						<input id="msisdn" type="hidden" name="msisdn" value="${msisdn}">
						
						<input id="msisdnprefix" type="hidden" name="msisdnprefix" value="${msisdnprefix}">
					
					<input id="subscribe" type="submit" class="button"  
						 onclick="submitSubscriptionForm()"/>							
           </form>  
</div>
</body></html>