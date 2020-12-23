
<%@ page pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 

<!doctype html>
<head>
  <title>GamesPad</title>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="description" content="">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/bz/style.css">
 <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script type="text/javascript">
$(function(){
$('#msisdnprefix1,#stype').change(function(){
	//alert("change");
	$("#otpform").attr("action", "${pageContext.request.contextPath}/cnt/bz/change/msisdnprefix");
	$("#otpform").submit();
})});

${source}
</script>

  <style>
  input[type=text] {
  padding: 12px 20px;
  margin: 8px 0;
  box-sizing: border-box;
}
.button {
  background-color: #0ca7fa; /* Orange */
  border: none;
  color: white;	
  padding: 4px 32px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 23px;
  font-weight: bold;
  width: 280px;
  border-radius: 12px;
}
.ctr {
  display: block;
  margin-left: auto;
  margin-right: auto;
  width: 50%;
}
@media only screen and (max-width: 600px) {
 .ctr {
		  width: 100%;

 }
}
html, body {
    background-color: #c2d1f0 !important;
  }
</style>
 </head>
<body>

<header>
<!--<div class="banner" style="background-color:#0ca7fa;">
<span class="title" >Gamesclubpro</span>
</div>-->
</header>
<section>
<div class="col-md-12 col-sm-12 col-lg-12">
<!--<h4>Download club</h4>-->
<p style="text-align:center;"><img src="${pageContext.request.contextPath}/resources/bz/game_pad_banner2.jpg"
 class="img-responsive ctr" width="350" height="300"></img></p>
<form id="otpform" method="post" action="${pageContext.request.contextPath}/cnt/bz/send/otp">

<p style="text-align:center;font-size:16px;">
Pour souscrire à ${bizaoConfig.serviceName} Insérez votre numéro de téléphone pour recevoir le code à   
           <fmt:formatNumber type="number" maxFractionDigits="0" value="${bizaoConfig.pricePoint}" />   ${bizaoConfig.currencyDesc}/${bizaoConfig.validityDesc}. </p>
           <p style="text-align:center;font-size:16px;">renouvellement automatique</p>
              <p style="text-align:center;font-family: verdana; font-size: 20px;color:red">${otpinfo}
           </p> 
           
<p style="text-align:center;font-size:14px;font-weight:bold;">Téléphone:   

<select id="msisdnprefix1" name="msisdnprefix" class="select-css"> 
                <c:forEach items="${listMsisdnPrefix}" var="mPrefix">
						 <c:choose>
                          <c:when test="${mPrefix eq bizaoConfig.msisdnPrefix}">
						  <option selected="${mPrefix eq bizaoConfig.msisdnPrefix}" value="${mPrefix}">${mPrefix}</option>
						  </c:when>
						 <c:otherwise>
						  <option  value="${mPrefix}">${mPrefix}</option>
						  </c:otherwise>
                            </c:choose>
						 </c:forEach>

</select>&nbsp;&nbsp;	
                      <input id="cgtoken" type="hidden" name="cgtoken" value="${cgtoken}">
                     
                      <input id="uniqueid" type="hidden" name="uniqueid" value="${uniqueid}">
                      
                     <input type="text" name="msisdn" placeholder="" value="${msisdn}"></p>
<p style="text-align:center;font-size:14px;font-weight:bold;"> type de service
                     <select name="stype" id="stype">
						   <c:if test="${bizaoConfig.serviceType eq 'daily'}">						
						  <option selected="true" value="daily">jour</option>
						   <option value="weekly">semaine</option>
						</c:if>
						
						<c:if test="${bizaoConfig.serviceType eq 'weekly'}">						
						  <option  value="daily">jour</option>
						   <option selected="true" value="weekly">semaine</option>
						</c:if>
						 </select>				 
</p>


<div class="ok" ><button type="submit" name="save" value="envoyer le code pin" class="button" align="middle"
  style="background-color: red;">
envoyer le code pin</button>&nbsp;</div><br/>

</form>



<p style="text-align:center;font-size:14px;font-weight:bold;text-decoration:underline;">TERMES ET CONDITIONS</p>
<p style="text-align:center;font-size:12px; padding: 0px 3px 0px 3px;">1. En vous abonnant au service, vous acceptez les Termes et Conditions du service. </p>
<p style="text-align:center;font-size:12px; padding: 0px 3px 0px 3px;">2. Vos data seront utilisées pour la navigation et les jeux sur le portail Game Pad.  </p>
<p style="text-align:center;font-size:12px; padding: 0px 3px 0px 3px;">3. Votre abonnement sera automatiquement renouvelé jusqu'à votre désinscription. Les frais d'abonnement seront déduits de votre crédit de communication ou de votre facture mobile postpayée. </p>

</div>
</section>
  
 </body>
</html>
