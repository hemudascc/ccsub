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
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/style.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>  
  <title>${actelServiceConfig.packageName} Pin Page</title>
  <style>
 
.button {
  background-color: #00b359; /* Green */
  border: none;
  color: white;
  padding: 6px 32px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 23px;
  font-weight: bold;
  width: 90%;
  border-radius: 12px;
}

.button1 {
  background-color: #000000; /* Green */
  border: none;
  color: white;
  padding: 6px 32px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 23px;
  font-weight: bold;
  width: 30%;
  border-radius: 12px;
}

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
	location.href='${pageContext.request.contextPath}/cnt/actel/chnage/lang?l='+lang+'&token=${token}&msisdn=${msisdn}&page=vodafone_qatar_wap_otp';
	return false;
}

</script>
 </head>
<body>

<header>


				<div>
	
		
			<div style="text-align:right;padding-top:10px;font-weight: bold">
			<a href="#" onclick="changeLang(0)">ENG</a> <a href="#" onclick="changeLang(1)">عربى</a>						
			</div>
			
			
			<!--<span class="title" ><img src="./cgImg/logo.png"></span>-->
			</div>
			</header>
			<section>
			<div class="col-md-12 col-sm-12 col-lg-12" style="text-align:center;">
			<form id="otpform" method="post" action="${pageContext.request.contextPath}/cnt/actel/vodafone/web/send/otp/validation">
			<p style="text-align:center;">
			<img src="${pageContext.request.contextPath}/resources/actel/animal-fall.png" class="img-responsive" height="200" width="300">
			</img></p>
			 <p style="text-align:center;font-size:14px;font-weight:bold;">
			     Kido Kingdom Service
			 </p>
			
			
			  <c:if test="${l==0}">
			  <div style="text-align:center;padding-top:10px;font-weight: bold" dir="ltr">			
			  Please enter the PIN received in the box below	
			  </div>
			  </c:if>
			   <c:if test="${l==1}">
			   <div style="text-align:center;padding-top:10px;font-weight: bold" dir="rtl">
			  الرجاء إدخال رقم التعريف الشخصي المستلم في المربع أدناه  
			  </div>
			  </c:if>          	
			
			<div class="ok" style="max-width: 100%;padding-bottom: 15px;">
			<input type="text" name="pin" style="width:90%">
			<input type="hidden"  name="token" value="${token}"/>
			<input type="hidden"  name="l" value="${l}"/>
			<input type="hidden"  name="msisdn" value="${msisdn}"/>
			<br>
			
			 <div style="text-align:center;padding-top:10px;font-weight: bold">			
			  ${actelServiceConfig.priceDesc} ${actelServiceConfig.price}/${actelServiceConfig.validityDesc}(Auto-Renewal)	            	
			</div>
			
			<input type="submit" class="button" style="font-size: 15px;" 
			value="${l==0?'Confirm' : 'تأكيد'}"/><br/><br/>
			<button class="button" style="font-size: 15px;" onclick="exit()">${l==0?'Exit':'خروج'}</button>
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
				• Subscription would be renewed at QAR 10.0/Weekly, automatically until you unsubscribe
				
				</div>
				
				<div style="text-align:center;font-size:15px;">
				
				
				• You can unsubscribe from this service anytime, by sending Unsub KK to 92071 for Ooredoo users. Vodafone users can send STOP AKK to 97710
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
			    • Kido Kingdom هي خدمة اشتراك تتلقى محتوى اللعبة
				</div>

				<div style="text-align:center;font-size:15px;" dir="rtl">
				• سيتم تجديد الاشتراك بمبلغ 10.0 ريال قطري / أسبوعيًا ، تلقائيًا حتى تقوم بإلغاء الاشتراك
				</div>
				<div style="text-align:center;font-size:15px;" dir="rtl">
					
				• يمكنك إلغاء الاشتراك في هذه الخدمة في أي وقت بإرسال Unsub KK إلى 92071 لمستخدمي Ooredoo. يمكن لمستخدمي Vodafone إرسال STOP AKK إلى 97710
				</div>
				<div style="text-align:center;font-size:15px;" dir="rtl">
				• للاستفادة من هذه الخدمة ، يجب أن يكون عمرك أكثر من 18 عامًا أو حصلت على إذن من والديك أو الشخص المخول بدفع فاتورتك
				</div>
				<div style="text-align:center;font-size:15px;" dir="rtl">
				• قد يتم تطبيق رسوم بيانات إضافية
				</div>
				
				</c:if>
			</section>
 </body>
</html>
