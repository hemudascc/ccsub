<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>

<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width initial-scale=1">
<title>GamePad</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/stylesheet.css">
 <script type="text/javascript">

</script>

</head>
<body>
        <div id="main">
         <h3>Gamepad  service </h3>
        <div class="banner"><img src="${pageContext.request.contextPath}/resources/images/wbd/game_pad_banner.jpg"></div>
        <div class="buttonbar">
     <form id="form-id" action="${pageContext.request.contextPath}/cnt/veoo/lpform" method="post">
                                <input type="hidden" name="param1" value="" />
                                <input type="hidden" value="${token}"  name="token">			                
                                <br>
                                <center>
                                <div class="button" style="text-align:center;" id="dvclick" >
									
                                	<a href="sms:${wintelBDServiceConfig.shortCode}?body=${wintelBDServiceConfig.keyword}">
                                	Write Sms</a>
                             
 </div>
                          </center>      
                        </form>
         
                        <h2>T&C</h2 >
            <p class="terms">
          You agree to the service by sending CS C to short code 16466 or by clicking on Write SMS Button. SMS charge applicable(2.55 TK.).

</p>
        </div>


    </div>
</body>
</html>

