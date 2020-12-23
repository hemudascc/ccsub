<%@ page pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 

<!doctype html>
<head>

<meta charset="utf-8">

<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="description" content="">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/actel/actel.css">

<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
  <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>  
   <script src="${pageContext.request.contextPath}/resources/actel/actel.js"></script>  
   
  <title>${actelServiceConfig.packageName}</title>
  <style>
 
</style>
<script type="text/javascript">

function exit(){
	//alert("exit")
	location.href='https://www.google.com/';
	//location.href='${actelServiceConfig.portalUrl}';
	return false;
}

function changeLang(lang){
	//alert("exit")
	location.href='${pageContext.request.contextPath}/cnt/actel/chnage/lang?l='+lang+'&token=${token}&msisdn=${msisdn}&page=ooredooqatar_msisdn';
	return false;
}

</script>
 </head>
<body>

<header>
			
			<div style="text-align:right;padding-top:10px;font-weight: bold">
			<a href="#" onclick="changeLang(0)">ENG</a> <a href="#" onclick="changeLang(1)">عربى</a>						
			</div>
			</header>
			<section>
			<div class="col-md-12 col-sm-12 col-lg-12" style="text-align:center;">
			<p style="text-align:center;"><img src="${pageContext.request.contextPath}/resources/actel/animal-fall.png"
			  class="img-responsive"
			 height="200" width="300"></img></p>
			 <p style="text-align:center;font-size:14px;font-weight:bold;">
			     Kido Kingdom Service
			 </p>
			
			 <form id="otpform" method="post" action="${pageContext.request.contextPath}/cnt/actel/ooredoo/web/send/otp">
				
				
				   <p style="text-align:center;font-size:14px;"><strong>${otpinfo}</strong></p>
				<div class="ok" style="max-width: 100%;padding-bottom: 15px;">
				<input type="text" style="width:90%" name="msisdn" placeholder="Mobile Number" value="${msisdn}"/>
				<input type="hidden"  name="token" value="${token}"/>
				<input type="hidden"  name="l" value="${l}"/>
				
				 <div style="text-align:center;padding-top:10px;font-weight: bold">			
			      ${actelServiceConfig.priceDesc} ${actelServiceConfig.price}/${actelServiceConfig.validityDesc}(Auto-Renewal)	            	
			    </div>
			    
				<input type="submit"  class="button" style="font-size: 15px;" value="${l==0?'Subscribe Now':'إشترك'}"/><br/><br/>
				<input type="submit" class="button" style="font-size: 15px;" onclick="return  exit();" value="${l==0?'Exit':'خروج'}"/>
			 </form>
			</div> 
				
				<c:if test="${l==0}">
				<div style="text-align:center;font-weight:bold;font-size:15px;">
				Terms and Conditions:
				</div>

				<div style="text-align:center;font-size:15px;">				
				• Kido Kingdom is a subscription service which you would receive Game content	
				</div>
				
				<div style="text-align:center;font-size:15px;" >
				 • Subscription would be renewed at ${actelServiceConfig.priceDesc} 10/${actelServiceConfig.validityDesc}, automatically until you unsubscribe
				
				</div>
				
				<div style="text-align:center;font-size:15px;">
				
				
				 • You can unsubscribe from this service anytime, by sending ${actelServiceConfig.unsubKey} to ${actelServiceConfig.shortCode}.
				</div>
				<div style="text-align:center;font-size:15px;">
				• To make use of this service, you must be more than 18 years old or have received permission from your parents or person who is authorized to pay your bill	
				</div>
				<div style="text-align:center;font-size:15px;">
				• Additional data charges may apply
				</div>
				</c:if>
				
					<c:if test="${l==1}">
				<div style="text-align:center;font-weight:bold;font-size:15px;" dir="rtl">
				الأحكام والشروط:
				</div>
					
				<div style="text-align:center;font-weight:bold;font-size:15px;" dir="rtl">
			       Kido Kingdom هي خدمة اشتراك يمكنك من خلالها الوصول إلى خدمة الألعاب
				</div>

				<div style="text-align:center;font-size:15px;" dir="rtl">
				<c:if test="${actelServiceConfig.serviceId==113}">
				• سيتم تجديد الإشتراك بمعدل 10 ريال قطري / أسبوعيًا حتى يتم إلغاء الاشتراك
				</c:if>
				<c:if test="${actelServiceConfig.serviceId==114}">
				• يتم تجديد الاشتراك بمعدل 5 ريال قطري / 3 أيام حتى يتم إلغاء الاشتراك	
				</c:if>
				<c:if test="${actelServiceConfig.serviceId==115}">
				• 	يتم تجديد الاشتراك بمعدل 2 ريال قطري / يوم حتى يتم إلغاء الاشتراك
				</c:if>
				</div>
				<div style="text-align:center;font-size:15px;" dir="rtl">
					
				• يمكنك إلغاء الإشتراك من هذه الخدمة في أي وقت ، عن طريق إرسال ${actelServiceConfig.unsubKey} إلى 92071 .
				</div>
				<div style="text-align:center;font-size:15px;" dir="rtl">
				•	للإستفادة من هذه الخدمة ، يجب أن يكون عمرك أكثر من ١٨ عامًا أو كنت قد حصلت على إذن من والديك أو الشخص المصرح له بدفع فاتورتك
				</div>
				<div style="text-align:center;font-size:15px;" dir="rtl">
				•	قد يتم تطبيق رسوم بيانات إضافية
				</div>
				
				</c:if>
			</div>
			</section>
 </body>
</html>
