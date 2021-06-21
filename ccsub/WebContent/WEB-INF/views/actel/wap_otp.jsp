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
	location.href='${pageContext.request.contextPath}/cnt/actel/chnage/lang?l='+lang+'&token=${token}&msisdn=${msisdn}&page=wap_otp';
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
			<p>
			<c:if test="${l==0}">
			Free for 24 hours, then ${actelServiceConfig.price}/${actelServiceConfig.validityDesc} for Etisalat users 
			</c:if>
			<c:if test="${l==1}">
				<span dir="rtl">
				مجانًا لمدة 24 ساعة ، ثم ${actelServiceConfig.price} /<c:if test="${actelServiceConfig.validityDesc=='daily'}">يوم</c:if>
				<c:if test="${actelServiceConfig.validityDesc=='Weekly'}">أسبوع</c:if> لمستخدمي اتصالات
				</span>
			</c:if>
		</p>
			<form id="otpform" method="post" action="${pageContext.request.contextPath}/cnt/actel/web/send/otp/validation">
			<p style="text-align:center;">
			<img src="${pageContext.request.contextPath}/resources/actel/play_it_banner.png" class="img-responsive" height="200" width="300"></img></p>
			<p style="text-align:center;font-size:14px;"><strong>PLAY IT <br/> Welcome: ${msisdn}</strong>
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
			<input type="hidden"  name="token" value="${token}"/>
			<input type="hidden"  name="l" value="${l}"/>
			<input type="hidden"  name="msisdn" value="${msisdn}"/>
			<br/><br/>
			<input type="submit" class="button" style="font-size: 15px;" 
			value="${l==0?'Confirm your subscription' : 'تأكيد اشتراكك'}"/><br/><br/>
			<button class="button" style="font-size: 15px;" onclick="exit()">${l==0?'Exit':'خروج'}</button>
			</form>
			</div> 
			
					<div class="terms-condition">
		<p>
			<c:if test="${l==0}">
					<b>Terms and Conditions:</b>
			</c:if>
			<c:if test="${l==1}">
					<b  dir="rtl">الأحكام والشروط</b>
			</c:if>
		</p>
		<p>
			<b>-</b>
			<c:if test="${l==0}">
			Free for 24 hours then, you will be charged AED 
			${actelServiceConfig.price}/${actelServiceConfig.validityDesc} automatically.
			</c:if>
			<c:if test="${l==1}">
				<span dir="rtl">
				مجانًا لمدة 24 ساعة بعد ذلك ، سيتم تحصيل رسوم منك تلقائيًا ${actelServiceConfig.price} / 
				<c:if test="${actelServiceConfig.validityDesc=='daily'}">يوم</c:if>
				<c:if test="${actelServiceConfig.validityDesc=='Weekly'}">أسبوع</c:if> درهم إماراتي.
				</span>
			</c:if>
		</p>
			<p>
			<b>-</b>
			<c:if test="${l==0}">
				<span>No commitment, you can cancel any time by sending
					${actelServiceConfig.unsubKey} to
					${actelServiceConfig.shortCode}</span>
			</c:if>
			<c:if test="${l==1}">
				<span dir="rtl"> لا يوجد التزام يمكنك إلغاؤه في أي وقت بإرسال
					${actelServiceConfig.unsubKey} إلى
					${actelServiceConfig.shortCode}</span>
			</c:if>
		</p>
		
			<p>
			<c:if test="${l==0}">
				<b>-</b>
				<span>For support please contact etisalatnoc@altruistindia.com</span>
			</c:if>
			<c:if test="${l==1}">
				<span dir="rtl"> للحصول على الدعم ، يرجى الاتصال بـ
					etisalatnoc@altruistindia.com </span>
			</c:if>
		</p>
		
		<p>
			<b>-</b>
			<c:if test="${l==0}">
				<span > Free trial applicable only for first time subscriber.</span>
			</c:if>
			<c:if test="${l==1}">
			<span dir="rtl">
			نسخة تجريبية مجانية قابلة للتطبيق فقط للمشترك لأول مرة.
			</span>
			</c:if>
		</p>
		<p>
			<b>-</b>
			<c:if test="${l==0}">
				<span>Enjoy your Free trial for 24 hours</span>
			</c:if>
			<c:if test="${l==1}">
			<span dir="rtl">
			استمتع بالإصدار التجريبي المجاني لمدة 24 ساعة
			</span>
			</c:if>
		</p>
		<p>
			<b>-</b>
			<c:if test="${l==0}">
				<span>For complete T's &amp;C's 
				<a href="${pageContext.request.contextPath}/cnt/actel/tc?token=${token}&l=${l}">click here</a>
			</c:if>
			<c:if test="${l==1}">
				<span dir="rtl"> للحصول على T's &amp;C's كاملة 
				<a href="${pageContext.request.contextPath}/cnt/actel/tc?token=${token}&l=${l}">انقر هنا</a>
				</span>
			</c:if>
		</p>
	</div>
			           
   			</div>
			</section>

			 


 </body>
</html>
