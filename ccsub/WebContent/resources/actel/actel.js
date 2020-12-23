function exit(){
	//alert("exit")
	location.href='https://www.google.com/';
	//location.href='${actelServiceConfig.portalUrl}';
	return false;
}

function changeLang(lang){
	//alert("exit")
	location.href='${pageContext.request.contextPath}/cnt/actel/chnage/lang?l='+lang+'&token=${token}&msisdn=${msisdn}&page=msisdn_missing';
	return false;
}