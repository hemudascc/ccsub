<%@page pageEncoding="UTF-8"%>

<!DOCTYPE html>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
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
<body>
<div class="container-wrap">
	<div align="center">
		<br><br><br><br>
	<img src="${pageContext.request.contextPath}/resources/images/bizao/game_pad_banner.jpg" style="width: 50%;" class="img-responsive">
	<br>
	
<br>
	
	
	<h2 align="center" style="color: #000000; font-family: arial; font-size: 16px;">
	<p style="text-align:center;font-size:16px;">
Pour souscrire à ${bizaoConfig.serviceName} Insérez votre numéro de téléphone pour recevoir le code à   
           <fmt:formatNumber type="number" maxFractionDigits="0" value="${bizaoConfig.pricePoint}" />   ${bizaoConfig.currencyDesc}/${bizaoConfig.validityDesc}. </p>
  
	</h2>
			 <br><br>
			 <a href="${cgUrl}" class="button" >Souscrire</a>
</div>
<div align="center">
 <p align="center">
        
 <h4>Termes et Conditions </h4>
        
1. En vous abonnant au service, vous acceptez les Termes et Conditions du service. 
        <br />
2. Vos data seront utilisées pour la navigation et les jeux sur le portail Game Pad.
        <br />
3. Votre abonnement sera automatiquement renouvelé jusqu'à votre désinscription. Les frais d'abonnement seront déduits de votre crédit de communication ou de votre facture mobile postpayée. 
        <br />

<!-- 5. Les jeux et catégories de jeux disponibles sur ce portail peuvent être ajoutés, modifiés ou supprimés à la discrétion de Creative Antenna Mobile sans aucune information préalable des utilisateurs. 
        <br />
6. L'utilisation de nos services ne vous confère aucun droit de propriété intellectuelle sur nos services ni sur le contenu auquel vous accédez. Vous ne pouvez utiliser le contenu de nos services que si vous obtenez l'autorisation de son propriétaire ou si la loi le permet. Ces conditions ne vous accordent pas le droit d'utiliser une marque ou un logo utilisé dans nos services. 
        <br />
7. Nous pouvons modifier les présentes conditions ou toute condition supplémentaire afin de refléter les modifications apportées à la loi ou à nos services offerts. Vous devriez regarder les termes et conditions  régulièrement. Nous publierons un avis de modification de ces conditions et le cas échéant un avis de modification des conditions supplémentaires dans le service applicable.-->
    </p> 
</div>
</div>
	</body>
  </html>