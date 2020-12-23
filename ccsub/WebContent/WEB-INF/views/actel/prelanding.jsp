<%@ page pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!doctype html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="description" content="">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/bootstrap.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<title>${actelServiceConfig.packageName}</title>
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
	alert("exit")
	location.href='${actelServiceConfig.portalUrl}';
	return false;
}

function changeLang(lang){
	//alert("exit")
	location.href='${pageContext.request.contextPath}/cnt/actel/chnage/lang?l='+lang+'&token=${token}&msisdn=${msisdn}&page=prelanding';
	return false;
}

/* function handelSubmit(serviceId){
	//alert(serviceId);
	$("#serviceId").val(serviceId);
	$("#operatorSelectForm").submit();
	
} */

</script>
</head>
<body>

	<header>
		<div>
			<div style="text-align: right; padding-top: 10px; font-weight: bold">
				<div
					style="text-align: left; padding-top: 5px; font-weight: bold; font-size: 12px; padding-left: 20px;"></div>
				<a href="#" onclick="changeLang(0)">ENG</a> <a href="#"
					onclick="changeLang(1)">عربى</a>
			</div>

			<c:if test="${l==0}">
				<!-- <div style="text-align:center;padding-top:10px;font-weight: bold"> -->
				<div
					style="text-align: center; padding-top: 5px; font-weight: bold; font-size: 12px; padding-left: 20px;">
					Etisalat: Free for 24 hours, then AED 11/weekly, VAT including <br>
					DU: AED 9/weekly, VAT included
				</div>
			</c:if>
			<c:if test="${l==1}">
				<div
					style="text-align: center; padding-top: 10px; font-weight: bold"
					dir="rtl">
					اتصالات: مجانًا لمدة 24 ساعة ، ثم 11 درهمًا إماراتيًا في الأسبوع ،
					بما في ذلك ضريبة القيمة المضافة <br> 9 دو: درهم إماراتي لكل
					أسبوع ، شامل ضريبة القيمة المضافة
				</div>
			</c:if>

		</div>
	</header>
	<section>
		<div class="col-md-12 col-sm-12 col-lg-12" style="text-align: center;">
			<p style="text-align: center;">
				<img
					src="${pageContext.request.contextPath}/resources/actel/play_it_banner.png"
					class="img-responsive" height="200" width="300"></img>
			</p>
			<form id="operatorSelectForm" method="post"
				action="${pageContext.request.contextPath}/cnt/actel/tocg">
				<c:if test="${l==0}">
					<p style="text-align: center; font-size: 14px;">
						<strong>Play IT</strong><br /> Please select your operator.
					</p>
				</c:if>
				<c:if test="${l==1}">
					<p style="text-align: center; font-size: 14px;" dir="rtl">
						<strong>Play IT</strong><br /> يرجى تحديد المشغل الخاص بك.
						<!-- الرجاء الضغط على زر المشغل. -->
					</p>
				</c:if>

				<div class="ok" style="max-width: 100%; padding-bottom: 15px;">
					<input type="hidden" name="token" value="${token}" /> <input
						type="hidden" name="l" value="${l}" /> <input type="hidden"
						id="serviceId" name="serviceId" value="" /> 
					<a href="${pageContext.request.contextPath}/cnt/cmp?adid=1&cmpid=244&token=${token}" type="button" class="button" style="font-size: 15px;">
						${l==0?'DU':'دو'}</a>
					<br/>
					<br/>
					<a href="${pageContext.request.contextPath}/cnt/cmp?adid=2&cmpid=179&token=${token}" type="button" class="button" style="font-size: 15px;">
					 ${l==0?'Etisalat':'اتصالات'}
					 </a><br/>
				</div>
			</form>
		</div>
	</section>
</body>
</html>
