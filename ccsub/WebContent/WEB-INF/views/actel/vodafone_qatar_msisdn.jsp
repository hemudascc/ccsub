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
	location.href='${pageContext.request.contextPath}/cnt/actel/chnage/lang?l='+lang+'&token=${token}&msisdn=${msisdn}&page=vodafone_qatar_msisdn';
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
			
			 <form id="otpform" method="post" action="${pageContext.request.contextPath}/cnt/actel/vodafone/web/send/otp">
				
				
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
				• If you are a new customer, you will have the service free for one day then you will be charged as per selected pack. – (This point varies upon each product. For those has free trial this will be added as the first point in T&C else not.)	
				</div>
				
				<div style="text-align:center;font-size:15px;" >
				• Your subscription will be automatically renewed until you cancel or unsubscribe.
				</div>
				<div style="text-align:center;font-size:15px;">				
				• You can unsubscribe any time through the site by clicking on “Profile” then “Unsubscribe” – (This point varies upon each product. There are services which doesn’t have the option to allow cancellation from the portal and for those this point will be removed from the T&C)
				</div>
				<div style="text-align:center;font-size:15px;">
				• You can also unsubscribe through SMS.	
				</div>
				<div style="text-align:center;font-size:15px;">
				• Vodafone Customer: STOP AKK to 97710
				</div>
				<div style="text-align:center;font-size:15px;">
				• You must be more than 18 years old to use this service.
				</div>
				
				<div style="text-align:center;font-size:15px;">
				• If you are less than 18 years old, you MUST receive permission from the authorized person who pay your bill.
				</div>
				
				<div style="text-align:center;font-size:15px;">
				• Standard data browsing casts will be applied.
				</div>
				
				<div style="text-align:center;font-size:15px;">
				• For support, please contact us at care@collectcent.com
				</div>
				
				</c:if>
				
					<c:if test="${l==1}">
				<div style="text-align:center;font-weight:bold;font-size:15px;" dir="rtl">
				الأحكام والشروط:
				</div>
					
				<div style="text-align:center;font-weight:bold;font-size:15px;" dir="rtl">
			    • إذا كنت عميلاً جديدًا ، فستحصل على الخدمة مجانًا ليوم واحد ، ثم ستتم محاسبتك وفقًا للحزمة المحددة. - (تختلف هذه النقطة حسب كل منتج. بالنسبة لأولئك الذين لديهم نسخة تجريبية مجانية ، ستتم إضافة هذه كنقطة أولى في T&C وإلا فلن.)
				</div>

				<div style="text-align:center;font-size:15px;" dir="rtl">
				• سيتم تجديد اشتراكك تلقائيًا حتى تقوم بإلغاء أو إلغاء الاشتراك.
				</div>
				<div style="text-align:center;font-size:15px;" dir="rtl">
					
				• يمكنك إلغاء الاشتراك في أي وقت من خلال الموقع بالنقر فوق "الملف الشخصي" ثم "إلغاء الاشتراك" - (تختلف هذه النقطة حسب كل منتج. هناك خدمات ليس لديها خيار السماح بالإلغاء من البوابة ولهؤلاء ستكون هذه النقطة تمت إزالته من T&C)
				</div>
				<div style="text-align:center;font-size:15px;" dir="rtl">
				• يمكنك أيضًا إلغاء الاشتراك من خلال الرسائل القصيرة.
				</div>
				<div style="text-align:center;font-size:15px;" dir="rtl">
				• عميل فودافون: STOP AKK to 97710
				</div>
				<div style="text-align:center;font-size:15px;" dir="rtl">
				• يجب أن يكون عمرك أكثر من 18 عامًا لاستخدام هذه الخدمة.
				</div>
				<div style="text-align:center;font-size:15px;" dir="rtl">
				• إذا كان عمرك أقل من 18 عامًا ، فيجب أن تتلقى إذنًا من الشخص المخول الذي يدفع فاتورتك.
				</div>
				<div style="text-align:center;font-size:15px;" dir="rtl">
				• سيتم تطبيق بيانات تصفح البيانات القياسية.
				</div>
				<div style="text-align:center;font-size:15px;" dir="rtl">
				• عميل فودافونللحصول على الدعم ، يرجى الاتصال بنا على care@collectcent.com
				</div>
				</c:if>
			</div>
			</section>
 </body>
</html>
