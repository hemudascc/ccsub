<%@ page pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html>
<head>
<title>Subscription Page</title>
<link href="style.css" type="text/css" rel="stylesheet"/>


	
<style>
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
</head>

<body background="${pageContext.request.contextPath}/resources/images/bizao/bg_images.jpg" style="width: 90%; height: 100%;" align="center">

<div class="container wrap" style="margin-top: 400px;">
 <img src="${pageContext.request.contextPath}/resources/images/bizao/game_pad_banner.jpg"/>
<br><br>
	<h3 align="center" style="color: #ffffff; font-family: verdana; font-size: 14px;">
	Subscribe to ${bizaoConfig.serviceName} .
	Your account will be debited ${bizaoConfig.currency}${bizaoConfig.pricePointDesc}/semaine </h3>
    
	<button class="button">Subscribe Now!</button>
</div>
</body>
</html>