<%@ page pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<!DOCTYPE html>
<head>
    <title>Unsubscribe/Se désabonner</title>
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>  
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.2.1/js/bootstrap.min.js"></script>
  <script src="${pageContext.request.contextPath}/js/index.js"></script>
      <script>
    </script>

</head>

<body style="margin-top: 2%;">

    <div class="container fluid" align="center">
        <img src="${pageContext.request.contextPath}/resources/images/bizao/gamepad-logo.png" style="width: 50%;">
<br><br>
    <div>
        <br>
       
       <c:choose>
  <c:when  test="${subscriberReg.status==1}">
			 <h3 style="font-family: verdana; font-weight: normal; font-size: 40px;">
        Voulez-vous vraiment vous desabonner? Appuyez sur Se "desabonner moi"</h3>
           <br/><br/>
	 		<a class="btn btn-info" href="#" 
	 		onclick="unsub('${subscriberReg.subscriberId}',
	 		'${unSubUrl}?subid=${subscriberReg.subscriberId}','${portalUrl}')" >
	 		 <h3 style="font-family: verdana; font-weight: normal; font-size: 40px;">
	 		  Desabonner moi</h3>
	 		</a>
	 	</c:when>
	 	<c:otherwise>
	 	      ${info}
	 	</c:otherwise>
	 	</c:choose>	
	 		 	
	 	<br/><br/>
    </div>
</div>



</body>

</html>