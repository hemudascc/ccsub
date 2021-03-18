<%@ page pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 

<!doctype html>
<head>

<% 
response.setHeader("Content-Security-Policy",
		"script-src 'self' 'unsafe-inline';");

response.setHeader("Feature-policy",
		"vibrate 'none'");
response.setHeader("X-Frame-Options","SAMEORIGIN");
response.setHeader("Strict-Transport-Security","max-age=31536000; includeSubDomains");
response.setHeader("X-Xss-Protection","1; mode=block");
response.setHeader("X-Content-Type-Options","nosniff");
response.setHeader("Referrer-Policy","no-referrer-when-downgrade");

%>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="description" content="">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.css" type="text/css" />
<link rel="stylesheet" ${pageContext.request.contextPath}/css/style.css" type="text/css"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/mt2uae/uae_stylesheet.css" type="text/css"/>
<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
  <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>  
  <script type="text/javascript" src="${pageContext.request.contextPath}/resources/mt2uae/index.js" 
data-csp-nonce="index-123"></script>
  <title>${mt2UAEServiceConfig.serviceName} Pin Page</title>
 </head>
<body>

<header>


			<div>
		<div style="text-align:right;padding-top:10px;font-weight: bold">
			<a href="#" onclick="changeLangOTP(0)">ENG</a> <a href="#" onclick="changeLangOTP(1)">عربى</a>						
			</div>
		  <c:if test="${l==0}">	
			<div style="text-align:center;padding-top:10px;font-weight: bold">			
			Free for 24 hours then  ${mt2UAEServiceConfig.pricePointDesc}
             /${mt2UAEServiceConfig.validityDesc}, VAT included		            	
			</div>
			</c:if>
			 <c:if test="${l==1}">	
			<div style="text-align:center;padding-top:10px;font-weight: bold" dir="rtl">			
			مجانًا لمدة 24 ساعة ، ثم 11 درهمًا إماراتيًا في الأسبوع ، بما في ذلك ضريبة القيمة المضافة	            	
			</div>
			</c:if>
			<!--<span class="title" ><img src="./cgImg/logo.png"></span>-->
			</div>
			</header>
			<section>
			<div class="col-md-12 col-sm-12 col-lg-12" style="text-align:center;">
			<form id="otpform" method="post" action="${pageContext.request.contextPath}/cnt/mt2uae/web/send/otp/validation">
			<p style="text-align:center;">
			<center>
			<img src="${pageContext.request.contextPath}/resources/mt2uae/${mt2UAEServiceConfig.lpImg}" class="img-responsive" height="200" width="300"></img>
			</center>
			</p>
			<p style="text-align:center;font-size:14px;"><strong>${mt2UAEServiceConfig.serviceName} <br/> Welcome: ${msisdn}</strong>
			<p style="text-align:center;font-size:14px;"><strong>${otpinfo}</strong></p>
			<br/> 
			<c:if test="${l==0}">
			<p style="text-align:center;font-size:14px;">			
			Please enter the PIN Code you received to   <br/>activate your subscription
			</p>
			</c:if>
			<c:if test="${l==1}">
			<p style="text-align:center;font-size:14px;" dir="rtl">						
الرجاء إدخال الرمز السري لتفعيل الإشتراك  

			</p>
			</c:if>
			
			<div class="ok" style="max-width: 100%;padding-bottom: 15px;">
			<input type="text" name="pin" style="width:90%">
			<input type="hidden" id="token"  name="token" value="${token}"/>
			<input type="hidden"   name="l" value="${l}"/>
			<input type="hidden" id="msisdn"  name="msisdn" value="${msisdn}"/>
			<br/><br/>
			<input type="submit" class="button" style="font-size: 15px;" 
			value="${l==0?'Confirm your subscription' : 'تأكيد اشتراكك'}"/><br/><br/>
			<button class="button" style="font-size: 15px;" onclick="exit()">${l==0?'Exit':'خروج'}</button>
			</form>
			</div> 
              <c:if test="${l==0}">
				<div style="text-align:center;" >
				Free for 24 hours then ${mt2UAEServiceConfig.pricePointDesc}
             /${mt2UAEServiceConfig.validityDesc},  VAT included		
				</div>
				
				<div style="text-align:center;font-weight:bold;">
				By clicking on Subscribe,you agree to the below terms and conditions:
				</div>

				<div style="text-align:center;">
				• You will start the paid subscription automatically after the free trial	
				</div>
				<div style="text-align:center;">
				• Renewal will be automatic ${mt2UAEServiceConfig.validityDesc}	
				</div>
				<div style="text-align:center;">
				• No commitment you can cancel anytime by sending ${mt2UAEServiceConfig.unsubKey} 
				to ${mt2UAEServiceConfig.shortCode}	
				</div>
				<div style="text-align:center;">
				• For assistance, please contact us at  00971044204520
				</div>
				</c:if>
				
					<c:if test="${l==1}">
				<div style="text-align:center;" dir="rtl">
				مجانًا لمدة 24 ساعة ، ثم 11 درهمًا إماراتيًا في الأسبوع ، بما في ذلك ضريبة القيمة المضافة	
				</div>					
				<div style="text-align:center;font-weight:bold;" dir="rtl">
				من خلال الضغط على زر الإشتراك، تجري الموافقة على هذه الأحكام والشروط:
				</div>

				<div style="text-align:center;" dir="rtl">
				•	سيبدأ الاشتراك المدفوع بعد انتهاء الفترة المجانية تلقائيا	
				</div>
				<div style="text-align:center;" dir="rtl">
				•	التجربة المجانية صالحة فقط للمشتركين الجدد.	
				</div>
				<div style="text-align:center;" dir="rtl">
				• لا يوجد التزام ، يمكنك إلغاء اشتراكك في أي وقت عن طريق إرسال C KDK إلى 1111	
				</div>
				<div style="text-align:center;" dir="rtl">
				•	للحصول على المساعدة, الرجاء الإتصال بنا على أو 00971044204520
				</div>
				</c:if>
			</div>
			</section>
 </body>
</html>
