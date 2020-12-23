<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html lang="en"><head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
  <title>Unsubcription</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
  <script src="${pageContext.request.contextPath}/js/jquery.js"></script>
  <script src="${pageContext.request.contextPath}/js/bootstrap.js">
  </script>
  	<style  type="text/css">
	.linear-bg1{
		 background: #E6E6FA;
		
		color: #00;
		font-weight: bold;
	}
	tr, th {
    text-align: center;
    }
    caption, th, td {
  padding: .2em .8em;
  border: 1px solid #E6E6FA;
}

caption {
  background: #E6E6FA;
  font-weight: bold;
  font-size: 1.1em;
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
 function unsub(id,unsubUrl){ 
	  //alert("unsub "+unsubUrl);
	//alert(id);
   // event.preventDefault(); 
    $.ajax({
        url: unsubUrl,
        //dataType: "jsonp",
        //jsonpCallback: 'callback',
        type: 'GET',
        //accepts: 'application/json',
        crossDomain:true,
        //jsonp:true,
        data: {
            format: 'jsonp'
            
        },
        timeout: 180000,
        success: function(response) {
        	
        	var json = $.parseJSON(response);
        	//alert(json);
        	alert(json.messgae);
        	$("#"+id).text(); //Response
        	 //alert(response);
            //alert(response.messgae);
        	setTimeout(function(){// wait for 5 secs(2)
                location.reload(); // then reload the page.(3)
           }, 5000); 
        },
    error: function(textstatus,response) {
        alert("Some technical issue to unsubscribe.please try after some time");
        setTimeout(function(){// wait for 5 secs(2)
            location.reload(); // then reload the page.(3)
       }, 5000); 
    }
    });
    return false; // for good measure
};

</script>
  
  </head>
<body background="${pageContext.request.contextPath}/images/background/BEAUTY_WORLD.png"
 onload="onLoad('${contentWebPrefixPath}${portal.portalBanner2}')">
		
		<div id="wait" style="display: none; width: 100%; height: 100%; top: 100px; left: 0px; position: fixed; z-index: 10000; text-align: center;">
            <img src="${pageContext.request.contextPath}/resources/images/ajaxloadingbar.gif" width="200" height="30" alt="Processing..." style="position: fixed; top: 50%; left: 50%;" />
         </div>

	<div class="container-fluid">
				<header id="header"  >
			<div class="container">
				<div class="row">
					<div class="col-sm-8">
						<h2 class="shadow"></h2></div>
					</div>
					<div class="col-sm-2" align="left">		
									
					</div>
				</div>
			</header>
</div>

  <form action="${pageContext.request.contextPath}/cnt/nxv/unsub">
		<table border="1" align="center" style="width:40%;text-align:center;font-size:15px;" >
		<caption>UnSubscription</caption>
		<tr height="40px">
		<td width="50%" align="right">Msisdn&nbsp;:</td>
		<td width="50%" align="left" >&nbsp;&nbsp;
		<input id="msisdn" type="text" name="msisdn" value="${msisdn}"> </td>
		</tr>		
		<tr height="40px">
		  <td colspan="2" align="center">		
		   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		   &nbsp;&nbsp;&nbsp;&nbsp; <input id="finnddetail" type="submit" value="Find Detail"></td>
		</tr>
	</table>
	<br/><br/>
	<c:if test="${msisdn!=null}">
		<table border="1" align="center" style="width:95%;text-align:center;font-size:15px;">
		<tr class="linear-bg1">
		<th >Msisdn</th>
		<th>Valid From</th>
		<th>Valid To</th>
		<th>Status</th>
		<th>Unsubscribe</th>
		</tr>
	  <c:if test="${subscriberRegList==null or subscriberRegList.size()<=0}">
		<tr>
	 		<td colspan="6">
	 		this mobile number is not subscribed any service for this portal
	 	  </td>
	 	</tr> 
			
		</c:if>
		
		<c:if test="${subscriberRegList!=null and subscriberRegList.size()>0}">
	  <c:forEach items="${subscriberRegList}" var="subscriberReg" >	 		
	 		<tr>
	 		<td>${subscriberReg.msisdn}</td>
<%-- 	 		<td>${mapServiceIdToService[subscriberReg.serviceId].serviceDesc}</td> --%>
	 		<td>${subscriberReg.validityFrom}</td>
	 		<td>${subscriberReg.validityTo}</td>	 	
	 		<td>${subscriberReg.statusDescp}</td>	 		
	 		<td id="${subscriberReg.subscriberId}">
	 		<c:if test="${subscriberReg.status==1}">
	 		
	 		<a href="#" onclick="unsub('${subscriberReg.subscriberId}',
	 		'${unSubUrl}?subid=${subscriberReg.subscriberId}')">
	 		Unsubscribe
	 		</a>
	 		</c:if>	 	
	 	  </td>
	 	</tr> 
			</c:forEach>	
		</c:if>
			
	</table>
	</c:if>
	
	</form>
	<br><br><br><br>
     <footer class="container-fluid text-center footer">
  <p></p>
  
  <p>Copyright © 2018</p>
</footer>
</body>
</html>			