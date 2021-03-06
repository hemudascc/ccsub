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
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>  
  <title>${mt2UAEServiceConfig.serviceName}</title>
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
	location.href='${mt2UAEServiceConfig.portalUrl}';
	return false;
}

function changeLang(lang){
	//alert("exit")
	location.href='${pageContext.request.contextPath}/cnt/mt2uae/chnage/lang?l='+lang+'&token=${token}&msisdn=${msisdn}&page=jod_msisdn_missing';
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
			
		  <c:if test="${l==0}">	
			<div style="text-align:center;padding-top:10px;font-weight: bold">			
			${mt2UAEServiceConfig.pricePointDesc}
            /${mt2UAEServiceConfig.validityDesc}	 , VAT included	            	
			</div>
			</c:if>
			 <c:if test="${l==1}">	
			<div style="text-align:center;padding-top:10px;font-weight: bold" dir="rtl">			
			0.20 دينار أردني في اليوم ، بما في ذلك ضريبة القيمة المضافة	            	
			</div>
			</c:if>
			
			</div>
			</header>
			<section>
			<div class="col-md-12 col-sm-12 col-lg-12" style="text-align:center;">
			<p style="text-align:center;"><img src="${pageContext.request.contextPath}/resources/mt2uae/${mt2UAEServiceConfig.lpImg}" class="img-responsive"
			 height="200" width="300"></img></p>
			 <form id="otpform" method="post" action="${pageContext.request.contextPath}/cnt/mt2uae/web/send/otp">
				<c:if test="${l==0}">
				<p style="text-align:center;font-size:14px;"><strong>${mt2UAEServiceConfig.serviceName}</strong><br/>				
				 To subscribe in ${mt2UAEServiceConfig.serviceName} service,please enter mobile number and  click
				   <br/>on the below button to send you the PIN Code				   
				   </p>
				   </c:if>
				   
				   <c:if test="${l==1}">
				<p style="text-align:center;font-size:14px;" dir="rtl"><strong>${mt2UAEServiceConfig.serviceName}</strong><br/>				
			  للاشتراك في خدمة Kido Kingdom يرجى إدخال رقم الهاتف المحمول والضغط على زر "الاشتراك" لتلقي كلمة المرور
				   				   
				   </p>
				   </c:if>
				   
				   <p style="text-align:center;font-size:14px;"><strong>${otpinfo}</strong></p>
				<div class="ok" style="max-width: 100%;padding-bottom: 15px;">
				<p>
				<input type=text" style="width:10%" name="msisdnprefix" 
				value="${mt2UAEServiceConfig.msisdnPrefix}"
				 />
				<input type=text" style="width:50%" name="msisdn" placeholder="79xxxxxxx"
				  value="${msisdn}"/>
				  </p>
				<input type="hidden"  name="token" value="${token}"/>
				<input type="hidden"  name="l" value="${l}"/>
				<br/><br/>
				<input type="submit"  class="button" style="font-size: 15px;" value="${l==0?'Subscribe through PIN code':'الاشتراك من خلال رمز PIN'}"/><br/><br/>
				<input type="submit" class="button" style="font-size: 15px;" onclick="return  exit();" value="${l==0?'Exit':'خروج'}"/>
			 </form>
			</div> 
			
			<c:if test="${l==0}">
				<div style="text-align:center;" >
				 ${mt2UAEServiceConfig.pricePointDesc}
             /${mt2UAEServiceConfig.validityDesc},  VAT included		
				</div>
				
				<div style="text-align:center;font-weight:bold;">
				By clicking on Subscribe,you agree to the below terms and conditions:
				</div>

				<div style="text-align:center;">
				• You will start the paid subscription automatically 	
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
			    0.20 دينار أردني في اليوم ، بما في ذلك ضريبة القيمة المضافة
				</div>					
				<div style="text-align:center;font-weight:bold;" dir="rtl">
				من خلال الضغط على زر الإشتراك، تجري الموافقة على هذه الأحكام والشروط:
				</div>
		       <div style="text-align:center;" dir="rtl">
				• سيكون التجديد تلقائيًا يوميًا	
				</div>
				<div style="text-align:center;" dir="rtl">
				• لا يوجد التزام ، يمكنك إلغاء اشتراكك في أي وقت عن طريق إرسال U KK إلى 90910	
				</div>
				<div style="text-align:center;" dir="rtl">
				•	للحصول على المساعدة, الرجاء الإتصال بنا على أو 00971044204520
				</div>
				</c:if>

				
				
			</div>
			</section>
 </body>
</html>
