<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page pageEncoding="UTF-8" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width initial-scale=1">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/mcgza/stylesheet.css">
<title>MyLife</title>
</head>

<body>
	<div id="main">
		<div class="banner"><img src="${pageContext.request.contextPath}/resources/mcgza/banner.jpg"></div>
        <div id="textbox">
	<div class="subscribe">
           <div class="submitbox">
             <h4>New User?</h4>
             <a href="${pageContext.request.contextPath}/cnt/mcgza/tocg?token=${token}" id="reset" 
             style="text-decoration:none !important; background: none; border: none;">
             <div class="sub-bttn">
             <img src="${pageContext.request.contextPath}/resources/mcgza/cta.png"></div>
             </a>
            </div>
        
            <p class="title">Subscribe to MyLife for <b class="bold">R7 per Day</b>
             <div class="input"><input name="check" type="checkbox" value="value"> I accept the Terms and Conditions of Service.</div>
            <div class="details">
    
            	<p><b>MyLife</b> is a subscription service which contains Various Lifestyle & Fitness Content - Workouts, Martial Arts, Healthy Eating & More…</p>
               
                <h2><a href="${pageContext.request.contextPath}/cnt/mcgza/termscond?token=${token}">Terms & Conditions</a></h2>
              
                 <p>Collectcent Digital Media Pvt Ltd is a member of WASPA and is bound by the WASPA Code of Conduct.
Customers have the right to approach WASPA to lodge a complaint in accordance with the WASPA complaints procedure. Collectcent Digital Media Pvt Ltd may be required to share information relating to a service or a customer with WASPA for the purpose of resolving a complaint. <b>WASPA website: www.waspa.org.za</b></p>
            </div>
        </div>        
     <h3><a href="${pageContext.request.contextPath}/cnt/mcgza/alreadysub?token=${token}">Already Subscribed User?</a></h3>
    </div>
	</div>

</body>
</html>
