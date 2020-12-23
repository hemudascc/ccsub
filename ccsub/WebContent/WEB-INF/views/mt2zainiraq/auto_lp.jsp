<%@ page pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 

<!doctype html>
<head>

  <title>${mt2ZainIraqServiceConfig.serviceName}</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>

<script type="text/javascript">

$(document).ready(function(){
	   $("#otpform").submit();
	});
	
</script>
 </head>
<body>

<header>
			
		 <form id="otpform" method="post" action="http://192.241.253.234/ccsub/cnt/mt2zainiraq/lp"
		  >
			 <input type="hidden" value="${token}" name="token" id="token"/>
			 </form>
			</div> 
			
			</section>
 </body>
</html>
