<%@ page pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 
<!DOCTYPE html>
<html><head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta name="viewport" content="width=device-width initial-scale=1">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/mt2uae/stylesheet.css">
<title>${mt2UAEServiceConfig.serviceName}</title>
</head>

<body class="termspage">
	<div id="mainbox">
		<div class="content">
         <h2>Terms and Conditions</h2>
         	 	<p>
         	 	
         	 	<c:if test="${l==0}">
				<div style="text-align:center;" >
				Free for 1 day then ${mt2UAEServiceConfig.pricePointDesc}
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
				مجانًا لمدة يوم واحد ثم 11 درهمًا إماراتيًا / الأسبوع ، شاملة ضريبة القيمة المضافة.	
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

			    </p>         
  </div>
	</div>


</body></html>